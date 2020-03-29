package com.dm.core;

import java.util.LinkedList;
import java.util.List;

/**
 * @author zy
 * 存储当前线程相关的sql解析任务
 */
public class DmLocalContext {

	private final static ThreadLocal<DmLocalContext> currentLocal = new InheritableThreadLocal<>();

	/**
	 * transaction commit flag
	 */
	private boolean transactionCommit = false;
	/**
	 * async task
	 */
	private List<SqlParserTask> tasks = new LinkedList<>();
	/**
	 * enable config
	 */
	private boolean syncFlag;


	public DmLocalContext(boolean syncFlag) {
		this.syncFlag = syncFlag;
	}

	public DmLocalContext() {

	}

	public static void initLocalThread() {
		currentLocal.set(new DmLocalContext(true));
	}

	/**
	 *
	 * @return current variable
	 */
	public static DmLocalContext cur() {
		return currentLocal.get();
	}

	/**
	 * add task for current context
	 * @param task
	 * @return dmLocalContext
	 */
	public static DmLocalContext addTask(SqlParserTask task) {
		DmLocalContext cur = DmLocalContext.cur();
		if (cur == null) {
			cur = new DmLocalContext();
			currentLocal.set(cur);
		}
		cur.tasks.add(task);
		return cur;
	}

	/**
	 * transaction commit flag
	 * @return boolean
	 */
	public static boolean transactionCommit() {
		DmLocalContext cur = cur();
		if (cur != null) {
			return cur.transactionCommit;
		}
		return false;
	}

	public List<SqlParserTask> getTasks() {
		return tasks;
	}

	public boolean isSyncFlag() {
		return syncFlag;
	}

	public void setSyncFlag(boolean syncFlag) {
		this.syncFlag = syncFlag;
	}
}
