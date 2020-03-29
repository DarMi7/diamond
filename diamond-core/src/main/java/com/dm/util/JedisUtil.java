package com.dm.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class JedisUtil {
    private static Logger logger = LoggerFactory.getLogger(JedisUtil.class);
    private static String address;
    private static ShardedJedisPool shardedJedisPool;
    private static ReentrantLock INSTANCE_INIT_LOCL = new ReentrantLock(false);
    private static int expireSeconds;

    public JedisUtil() {
    }

    public static void init(String add) {
        address = add;
        getInstance();
    }

    private static ShardedJedis getInstance() {
        if (shardedJedisPool == null) {
            try {
                if (INSTANCE_INIT_LOCL.tryLock(2L, TimeUnit.SECONDS)) {
                    try {
                        if (shardedJedisPool == null) {
                            JedisPoolConfig config = new JedisPoolConfig();
                            config.setMaxTotal(200);
                            config.setMaxIdle(50);
                            config.setMinIdle(8);
                            config.setMaxWaitMillis(10000L);
                            config.setTestOnBorrow(true);
                            config.setTestOnReturn(false);
                            config.setTestWhileIdle(true);
                            config.setTimeBetweenEvictionRunsMillis(30000L);
                            config.setNumTestsPerEvictionRun(10);
                            List<JedisShardInfo> jedisShardInfos = new LinkedList();
                            String[] addressArr = address.split(",");

                            for(int i = 0; i < addressArr.length; ++i) {
                                JedisShardInfo jedisShardInfo = new JedisShardInfo(addressArr[i]);
                                jedisShardInfos.add(jedisShardInfo);
                            }

                            shardedJedisPool = new ShardedJedisPool(config, jedisShardInfos);
                            logger.info(">>>>>>>>>>> dm, JedisUtil.ShardedJedisPool init success.");
                        }
                    } finally {
                        INSTANCE_INIT_LOCL.unlock();
                    }
                }
            } catch (InterruptedException var9) {
                logger.error(var9.getMessage(), var9);
            }
        }

        if (shardedJedisPool == null) {
            throw new NullPointerException(">>>>>>>>>>> dm, JedisUtil.ShardedJedisPool is null.");
        } else {
            ShardedJedis shardedJedis = shardedJedisPool.getResource();
            return shardedJedis;
        }
    }

    public static void close() throws IOException {
        if (shardedJedisPool != null) {
            shardedJedisPool.close();
        }

    }

    private static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;

        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            byte[] var4 = bytes;
            return var4;
        } catch (Exception var14) {
            logger.error(var14.getMessage(), var14);
        } finally {
            try {
                oos.close();
                baos.close();
            } catch (IOException var13) {
                logger.error(var13.getMessage(), var13);
            }

        }

        return null;
    }

    private static Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;

        try {
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object var3 = ois.readObject();
            return var3;
        } catch (Exception var13) {
            logger.error(var13.getMessage(), var13);
        } finally {
            try {
                bais.close();
            } catch (IOException var12) {
                logger.error(var12.getMessage(), var12);
            }

        }

        return null;
    }

    public static String setStringValue(String key, String value) {
        String result = null;
        ShardedJedis client = getInstance();
        try {
            if (expireSeconds == -1) {
                result = client.set(key, value);
            } else {
                result = client.setex(key, -1, value);
            }
        } catch (Exception var9) {
            logger.error(var9.getMessage(), var9);
        } finally {
            if (client != null) {
                client.close();
            }

        }

        return result;
    }

    public static String setObjectValue(String key, Object obj, int seconds) {
        String result = null;
        ShardedJedis client = getInstance();

        try {
            result = client.setex(key.getBytes(), seconds, serialize(obj));
        } catch (Exception var9) {
            logger.error(var9.getMessage(), var9);
        } finally {
            if (client != null) {
                client.close();
            }

        }

        return result;
    }

    public static String getStringValue(String key) {
        String value = null;
        ShardedJedis client = getInstance();

        try {
            value = client.get(key);
        } catch (Exception var7) {
            logger.error(var7.getMessage(), var7);
        } finally {
            if (client != null) {
                client.close();
            }

        }

        return value;
    }

    public static Object getObjectValue(String key) {
        Object obj = null;
        ShardedJedis client = getInstance();

        try {
            byte[] bytes = client.get(key.getBytes());
            if (bytes != null && bytes.length > 0) {
                obj = unserialize(bytes);
            }
        } catch (Exception var7) {
            logger.error(var7.getMessage(), var7);
        } finally {
            if (client != null) {
                client.close();
            }

        }

        return obj;
    }

    public static Long del(String key) {
        Long result = null;
        ShardedJedis client = getInstance();
        try {
            result = client.del(key);
        } catch (Exception var7) {
            logger.error(var7.getMessage(), var7);
        } finally {
            if (client != null) {
                client.close();
            }

        }

        return result;
    }

    public static Long incrBy(String key, int i) {
        Long result = null;
        ShardedJedis client = getInstance();

        try {
            result = client.incrBy(key, (long)i);
        } catch (Exception var8) {
            logger.error(var8.getMessage(), var8);
        } finally {
            if (client != null) {
                client.close();
            }

        }

        return result;
    }

    public static boolean exists(String key) {
        Boolean result = null;
        ShardedJedis client = getInstance();

        try {
            result = client.exists(key);
        } catch (Exception var7) {
            logger.error(var7.getMessage(), var7);
        } finally {
            if (client != null) {
                client.close();
            }

        }

        return result;
    }

    public static long expire(String key, int seconds) {
        Long result = null;
        ShardedJedis client = getInstance();

        try {
            result = client.expire(key, seconds);
        } catch (Exception var8) {
            logger.error(var8.getMessage(), var8);
        } finally {
            if (client != null) {
                client.close();
            }

        }

        return result;
    }

    public static long expireAt(String key, long unixTime) {
        Long result = null;
        ShardedJedis client = getInstance();

        try {
            result = client.expireAt(key, unixTime);
        } catch (Exception var9) {
            logger.error(var9.getMessage(), var9);
        } finally {
            if (client != null) {
                client.close();
            }

        }

        return result;
    }

    public static void setExpireSeconds(int expireSeconds) {
        JedisUtil.expireSeconds = expireSeconds;
    }
}