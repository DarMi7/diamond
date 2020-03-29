package com.dm.core.parser;

import com.dm.core.SqlParserTable;
import java.util.Map;
import net.sf.jsqlparser.statement.Statement;

/**
 *
 * @author zy
 *
 */
public class UpdateSqlParser extends SqlParserTemplate {

	@Override
	protected Statement convertParserType(Statement statement) {
		return null;
	}

	@Override
	protected void parserParameters(Statement statement, Map<String, SqlParserTable> tableMap) throws InterruptedException {

	}
}
