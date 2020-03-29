package com.dm.core;

import com.dm.constant.DmConstants;
import com.dm.core.parser.SqlParserStreamFactory;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author zy
 *	sql任务数据管道解析
 */
public class PipelineContext implements InitializingBean {

	public static TaskThreadPoolExecutor taskThreadPoolExecutor;

	private static  Pipeline pipeline;

	public PipelineContext() {
	}

	@Override
	public void afterPropertiesSet() {
		init();
	}

	private void init() {
		taskThreadPoolExecutor = new TaskThreadPoolExecutor(2, 2, 0,
				TimeUnit.SECONDS, new LinkedBlockingQueue<>(128), new NamedThreadFactory(DmConstants.TASK_THREAD_NAME_FORMAT));
			pipeline = new Pipeline(SqlParserStreamFactory.getSqlParserStream());
	}

	public static Pipeline getPipeline() {
		return pipeline;
	}
}
