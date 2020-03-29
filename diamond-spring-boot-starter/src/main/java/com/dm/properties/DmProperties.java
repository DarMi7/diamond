package com.dm.properties;

import com.dm.constant.StarterConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


/**
 *
 * @author zy
 * dm configuration
 */
@ConfigurationProperties(prefix = StarterConstants.DM_CONFIG)
public class DmProperties {
    /**
     * Whether use JDK proxy instead of CGLIB proxy
     */
    private boolean useJdkProxy = false;
    /**
     * parallel parser parser and async data to redis tasks size
     */
    //private int parallelSize = 3;
    /**
     * The name of the table that needs to be synchronized
     */
    private String[] asyncTables;
    /**
     * the size of per SqlParser task queue
     */
   // private int taskBq = 100;

    private String redisAddress;

    private int expireSeconds;

    public boolean useJdkProxy() {
        return useJdkProxy;
    }

    public void setUseJdkProxy(boolean useJdkProxy) {
        this.useJdkProxy = useJdkProxy;
    }

/*
    public int getParallelSize() {
        return parallelSize;
    }

    public void setParallelSize(int parallelSize) {
        this.parallelSize = parallelSize;
    }
*/

    public String[] getAsyncTables() {
        return asyncTables;
    }

    public void setAsyncTables(String[] asyncTables) {
        this.asyncTables = asyncTables;
    }

 /*   public int getTaskBq() {
        return taskBq;
    }

    public void setTaskBq(int taskBq) {
        this.taskBq = taskBq;
    }*/

    public String getRedisAddress() {
        return redisAddress;
    }

    public void setRedisAddress(String redisAddress) {
        this.redisAddress = redisAddress;
    }

    public boolean isUseJdkProxy() {
        return useJdkProxy;
    }

    public int getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(int expireSeconds) {
        this.expireSeconds = expireSeconds;
    }
}
