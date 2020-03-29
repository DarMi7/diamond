package com.dm.core.parser;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zy
 * 获取sql的解析器
 */
public class SqlParserStreamFactory {
	public static List<SqlParser> getSqlParserStream() {
		List<SqlParser> parsers = new ArrayList<>();
		parsers.add(new DeleteSqlParser());
		parsers.add(new InsertSqlParser());
		parsers.add(new UpdateSqlParser());
		return parsers;
	}
}
