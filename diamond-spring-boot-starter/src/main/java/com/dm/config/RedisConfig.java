package com.dm.config;

import com.dm.properties.DmProperties;
import com.dm.util.JedisUtil;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zy
 */
@Configuration
@EnableConfigurationProperties({DmProperties.class})
public class RedisConfig implements InitializingBean, DisposableBean {
    @Autowired
    private DmProperties dmProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        JedisUtil.setExpireSeconds(dmProperties.getExpireSeconds());
        JedisUtil.init(dmProperties.getRedisAddress());
    }

    @Override
    public void destroy() throws Exception {
        JedisUtil.close();
    }

}