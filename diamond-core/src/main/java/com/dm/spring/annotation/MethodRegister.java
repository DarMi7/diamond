/*
package com.dm.spring.annotation;

import com.dm.util.ClassScanner;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

*/
/**
 * @author zy
 * 存储有EnableDmAsync注解的方法
 *//*

public class MethodRegister implements InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodRegister.class);

	private static List<String> asyncMathodCache = new LinkedList<>();

	@Override
	public void afterPropertiesSet() {
		add();
	}

	public void add() {
		List<String> allClasses = ClassScanner.scanAllClasses();
		allClasses.stream().forEach(e -> {
			try {
				Class<?> aClass = Class.forName(e);
				if (aClass != null) {
					Method[] declaredMethods = aClass.getDeclaredMethods();
					Arrays.stream(declaredMethods).forEach(m -> {
						EnableDmAsync annotation = m.getAnnotation(EnableDmAsync.class);
						if (annotation != null) {
							asyncMathodCache.add(m.getName());
						}
					});
				}
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();

			}
		});
		LOGGER.info("add EnableDmAsync annotation method: [{}]", asyncMathodCache);
	}

	public static List<String> getAsyncMathodCache() {
		return asyncMathodCache;
	}
}

*/
