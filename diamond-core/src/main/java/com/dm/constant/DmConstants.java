package com.dm.constant;

/**
 * @author zy
 * Constants
 */
public class DmConstants {
	/**
	 * default charset
	 */
	public static final String DEFAULT_CHARSET_NAME = "UTF-8";
	/**
	 * ring buffer size
	 */
	public static final int RING_BUFFER_SIZE = 1024;

	public static final String DIS_THREAD_NAME_FORMAT = "dm-dis-cons";

	public static final String TASK_THREAD_NAME_FORMAT = "dm-par-task";

	public static final String SQL_SELECT_PREFIX = "select";

	public static final String DATABASE_PRODUCT_NAME = "MySQL";

	public static final String COMMA = "\\,";

	public static final String UNDERLINE = "_";

	public static final String PERCENT = "%";

	public static final int INITIAL_CAPACITY = 64;

	public static final int JVM_PROCESSORS = Runtime.getRuntime().availableProcessors() >> 1;

	public static final int MAXIMUM_CAPACITY = JVM_PROCESSORS < 2 ? 1 : (JVM_PROCESSORS >> 1);

}
