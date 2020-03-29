package com.dm.core.parser;

import com.dm.core.Pipeline;
import com.dm.core.SqlParserTask;

/**
 *
 * @author zy
 */
public interface SqlParser {
	/**
	 * 解析sql语句
	 * @param pipeline
	 */
	void parser(SqlParserTask sqlParserTask, Pipeline pipeline);
}
