package com.dm.interceptor;

/**
 * @author zy
 */

import com.dm.core.DmLocalContext;
import com.dm.spring.annotation.EnableDmAsync;
import java.lang.reflect.Method;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 *
 * @author zy
 * 注解拦截
 */
@Component
@Aspect
public class DmSyncAspect {

	@Pointcut("@annotation(com.dm.spring.annotation.EnableDmAsync)")
	public void syncService() {

	}

	@Before("syncService()")
	public void before(JoinPoint point) {
		MethodSignature methodSignature = (MethodSignature) point.getSignature();
		Method method = methodSignature.getMethod();
		EnableDmAsync enableDmAsyncAn = method.getAnnotation(EnableDmAsync.class);
		if (enableDmAsyncAn != null) {
			DmLocalContext.initLocalThread();
		}
	}
}