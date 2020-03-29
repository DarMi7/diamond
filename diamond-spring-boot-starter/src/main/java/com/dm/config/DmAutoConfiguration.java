package com.dm.config;

import com.dm.core.DatabaseMeta;
import com.dm.core.PipelineContext;
import com.dm.interceptor.DmSyncAspect;
import com.dm.properties.DmProperties;
import com.dm.spring.datasource.DataSourceBeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static com.dm.constant.StarterConstants.BEAN_NAME_DATA_SOURCE_BEAN_POST_PROCESSOR;

/**
 * @author zy
 */
@EnableConfigurationProperties({DmProperties.class})
@Configuration
@Import({RedisConfig.class, DmSyncAspect.class})
public class DmAutoConfiguration {
    @Bean(BEAN_NAME_DATA_SOURCE_BEAN_POST_PROCESSOR)
    @ConditionalOnMissingBean(DataSourceBeanPostProcessor.class)
    public DataSourceBeanPostProcessor dataSourceBeanPostProcessor(DmProperties dmProperties) {
        return new DataSourceBeanPostProcessor(dmProperties.useJdkProxy());
    }

    @Bean
    public PipelineContext pipelineContext() {
        return new PipelineContext();
    }

    @Bean
    public DatabaseMeta tablesContext(DmProperties dmProperties) {
        return new DatabaseMeta(dmProperties.getAsyncTables());
    }

  /*  @Autowired
    @ConditionalOnBean(RedisTemplate.class)
    public RedisUtil redisUtil(@Qualifier("stringRedisTemplate") RedisTemplate template) {
        return new RedisUtil(template);
    }*/

}

