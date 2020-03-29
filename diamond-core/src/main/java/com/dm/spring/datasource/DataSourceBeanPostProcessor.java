package com.dm.spring.datasource;

import com.dm.database.proxy.DataSourceProxy;
import com.dm.exception.UnexpectedException;
import com.dm.util.SpringProxyUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

/**
 * @author zy
 * data source bean post processor
 */
public class DataSourceBeanPostProcessor implements BeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceBeanPostProcessor.class);

    private boolean useJdkProxy;


    public DataSourceBeanPostProcessor(boolean useJdkProxy) {
        this.useJdkProxy = useJdkProxy;
    }

    /**
     * create datasource proxy object
     * @param bean
     * @param beanName
     * @return proxy data source
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DataSource && !(bean instanceof DataSourceProxy)) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Auto proxy of [{}]", beanName);
            }
            return proxyDataSource(bean);
        }
        return bean;
    }

    /**
     * if you've created a DataSourceProxy bean it will throw exception
     * @param bean
     * @param beanName
     * @return bean
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof DataSourceProxy) {
            throw new UnexpectedException("Auto proxy of DataSource can't be enabled as you've created a DataSourceProxy bean." +
                "Please consider removing DataSourceProxy bean or disabling auto proxy of DataSource.");
        }
        return bean;
    }

    /**
     * proxy data source
     *
     * @param originBean
     * @return proxied datasource
     */
    private Object proxyDataSource(Object originBean) {
        DataSourceProxy dataSourceProxy = DataSourceProxyHolder.get().putDataSource((DataSource) originBean);
        if (this.useJdkProxy) {
            return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    SpringProxyUtils.getAllInterfaces(originBean),
                    (proxy, method, args) -> handleMethodProxy(dataSourceProxy, method, args, originBean));
        } else {
            return Enhancer.create(originBean.getClass(),
                    (MethodInterceptor) (proxy, method, args, methodProxy) -> handleMethodProxy(dataSourceProxy, method, args, originBean));
        }

    }

    /**
     * handle method proxy
     *
     * @param dataSourceProxy
     * @param method
     * @param args
     * @param originBean
     * @return proxied datasource
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private Object handleMethodProxy(DataSource dataSourceProxy, Method method, Object[] args, Object originBean) throws InvocationTargetException, IllegalAccessException {
        Method m = BeanUtils.findDeclaredMethod(DataSourceProxy.class, method.getName(), method.getParameterTypes());
        if (null != m) {
            return m.invoke(dataSourceProxy, args);
        } else {
            boolean oldAccessible = method.isAccessible();
            try {
                method.setAccessible(true);
                return method.invoke(originBean, args);
            } finally {
                //recover the original accessible for security reason
                method.setAccessible(oldAccessible);
            }
        }
    }
}
