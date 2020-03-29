package com.dm.core;

import com.dm.core.parser.SqlParser;
import com.dm.database.SQLType;
import com.dm.util.JedisUtil;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zy
 * 处理任务的管道
 */
public class Pipeline {

	private static final Logger LOGGER = LoggerFactory.getLogger(Pipeline.class);

	private BlockingQueue<RedisTask> bq = new LinkedBlockingQueue(100);

	/**
	 * sql解析器集合
	 */
	private List<SqlParser> sqlParsers;

	public Pipeline() {

	}

	public Pipeline(List<SqlParser> sqlParsers) {
		this.sqlParsers = sqlParsers;
		start();
	}

	public void process(SqlParserTask task) {
		sqlParsers.stream().forEach(s -> s.parser(task, this));
	}

	public void start() {
		PipelineContext.taskThreadPoolExecutor.execute(() -> {
			try {
				runWorker();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
	}

	private void runWorker() throws InterruptedException {
		RedisTask task;
		while ((task = getTask()) != null) {
			String key = task.getKey();
			SQLType sqlType = task.getSqlType();
			switch (sqlType) {
				case DELETE:
					if (JedisUtil.del(key) > 0) {
						LOGGER.info("Redis success to delete key:[{}]", key);
					} else {
						LOGGER.info("Redis fail to delete the key: [{}]", key);
					}
					break;
				case INSERT:
					if ("OK".equals(JedisUtil.setStringValue(key,task.getJson()))) {
						LOGGER.info("Redis success to insert key:[{}]", key);
					} else {
						LOGGER.info("Redis fail to insert the key: [{}]", key);
					}
					break;
				default:

			}
		}
	}

	private RedisTask getTask() throws InterruptedException {
		while (true) {
			RedisTask take = bq.take();
			if (take != null) {
				return take;
			}
		}
	}

	public BlockingQueue getBq() {
		return bq;
	}
}
