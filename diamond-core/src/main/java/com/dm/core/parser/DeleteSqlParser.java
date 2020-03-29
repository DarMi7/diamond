package com.dm.core.parser;

import com.dm.constant.DmConstants;
import com.dm.core.DatabaseMeta;
import com.dm.core.RedisTask;
import com.dm.core.SqlParserTable;
import com.dm.database.SQLType;
import com.dm.util.CollectionUtils;
import com.dm.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;

/**
 *
 * @author zy
 * delete类型的sql解析器
 */
public class DeleteSqlParser extends SqlParserTemplate {

	public DeleteSqlParser() {

	}

	@Override
	protected Delete convertParserType(Statement statement) {
		if (statement instanceof Delete) {
			Delete deStatement = (Delete) statement;
			return deStatement;
		}
		return null;
	}

	@Override
	protected void parserParameters(Statement statement, Map<String, SqlParserTable> SqlParserTable) throws InterruptedException {
		Delete deStatement = (Delete) statement;
		Table table = deStatement.getTable();
		String tableName = table.getName();
		String tableKey = DatabaseMeta.getDatabaseName().concat(DmConstants.UNDERLINE).concat(tableName);
		SqlParserTable sqlParserTable = SqlParserTable.get(tableKey);
		Expression where = deStatement.getWhere();
		if (where == null) {
			return ;
		}
		String redisKey = null;
		String pk = sqlParserTable.getPk();
		if (where instanceof BinaryExpression) {
			String pkValue = null;
			List<Map<String, String>> pkValueMap = getPKValue(where,new ArrayList<>(10));
			ArrayList<Object>[] parameters = task.getParameters();
			if (CollectionUtils.isEmpty(parameters)) {
				//采用的拼接sql方式
				pkValue = pkValueMap.stream().filter(e -> e.get(pk) != null).map(e -> e.get(pk)).findFirst().get();
			} else {
				//采用预编译执行方式
				int size = pkValueMap.size();
				ArrayList<Object> parameter = parameters[0];
				for (int i = 0; i < size; i++) {
					Map<String, String> pv = pkValueMap.get(i);
					if (StringUtils.isNotBlank(pv.get(pk))) {
						pkValue = parameter.get(i).toString();
						break;
					}
				}

			}
			redisKey = tableKey.concat(DmConstants.UNDERLINE).concat(pkValue);

		}
		BlockingQueue bq = pipeline.getBq();
		bq.put(new RedisTask(redisKey, SQLType.DELETE));
	}

}
