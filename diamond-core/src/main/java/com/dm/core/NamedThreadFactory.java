package com.dm.core;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zy
 */
public class NamedThreadFactory implements ThreadFactory {

	private final ThreadGroup threadGroup;

	public final String namePrefix;

	private AtomicInteger poolNumber = new AtomicInteger(1);


	public NamedThreadFactory(String name) {
		SecurityManager s = System.getSecurityManager();
		threadGroup = (s != null) ? s.getThreadGroup() :
				Thread.currentThread().getThreadGroup();
		if (null == name || "".equals(name.trim())) {
			name = "pool";
		}
		namePrefix = name + "-" +
				poolNumber.getAndIncrement();
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(threadGroup, r,
				namePrefix,
				0);
		if (t.isDaemon()) {
			t.setDaemon(false);
		}
		if (t.getPriority() != Thread.NORM_PRIORITY) {
			t.setPriority(Thread.NORM_PRIORITY);
		}
		return t;
	}
}