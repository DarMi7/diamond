/*
package com.dm.config;

import com.dm.constant.StarterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

*/
/**
 *
 * @author zy
 * spring cloud configuration
 *//*

@ConfigurationProperties(prefix = StarterConstants.DM_CONFIG)
public class SpringCloudConfiguration implements ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringCloudConfiguration.class);
    */
/**
     * spring application name
     *//*

    private static final String SPRING_APPLICATION_NAME_KEY = "spring.application.name";
    */
/**
     * service group
     *//*

    private static final String DEFAULT_SPRING_CLOUD_SERVICE_GROUP_POSTFIX = "-dm-service-group";
    */
/**
     * application id
      *//*

    private String applicationId;
    */
/**
     * application context
     *//*

    private ApplicationContext applicationContext;

    */
/**
     * Gets application id.
     *
     * @return the application id
     *//*

    public String getApplicationId() {
        if (null == applicationId) {
            applicationId = applicationContext.getEnvironment().getProperty(SPRING_APPLICATION_NAME_KEY);
        }
        return applicationId;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
*/
