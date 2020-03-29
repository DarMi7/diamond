package com.dm.core.parser;

import com.dm.constant.DmConstants;
import com.dm.core.DatabaseMeta;
import com.dm.core.RedisTask;
import com.dm.core.SqlParserTable;
import com.dm.database.SQLType;
import com.dm.util.CollectionUtils;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;

/**
 *
 * @author zy
 *insert 类型的sql解析器
 */
public class InsertSqlParser extends SqlParserTemplate {

	@Override
	protected Statement convertParserType(Statement statement) {
		if(statement instanceof Insert) {
			Insert inStatement = (Insert) statement;
			return inStatement;
		}
		return null;
	}

	@Override
	protected void parserParameters(Statement statement, Map<String, SqlParserTable> SqlParserTable) throws InterruptedException {
		Insert inStatement = (Insert) statement;
		Table table = inStatement.getTable();
		String tableName = table.getName();
		String tableKey = DatabaseMeta.getDatabaseName().concat(DmConstants.UNDERLINE).concat(tableName);
		SqlParserTable sqlParserTable = SqlParserTable.get(tableKey);
		ItemsList itemsList = inStatement.getItemsList();
		List<String> fields = sqlParserTable.getFields();

		String redisKey = null;
		String pk = sqlParserTable.getPk();
		int size = fields.size();
		Map<String, String> fieldsMap = new HashMap<>(32);
		ArrayList<Object>[] parameters = task.getParameters();
		List<Object> parameterValues = getParameterValues(parameters, itemsList, new ArrayList<>(32));
		if (CollectionUtils.isNotEmpty(parameters) && parameterValues.size() >= size) {
			//1.采用预编译的形式
			redisKey = combineFields(parameterValues, redisKey, fieldsMap, tableKey, pk, fields, size);
		} else {
			//2.采用sql拼接形式
			List<Column> columns = inStatement.getColumns();
			List<String> columnName = columns.stream().map(e -> e.getColumnName()).collect(Collectors.toList());
			if (columnName.contains(pk)) {
				//1.指定插入的主键;
				redisKey = combineFields(parameterValues, redisKey, fieldsMap, tableKey, pk, fields, columns.size());
			} else {
				//2.没有指定特定主键，采用自动增长方式
				redisKey = combineFields(parameterValues, fieldsMap, tableKey, pk, columns);
			}
		}
		pipeline.getBq().put(new RedisTask(redisKey,new Gson().toJson(fieldsMap), SQLType.INSERT));
	}

	private String combineFields(List<Object> parameterValues, String redisKey, Map<String, String> fieldsMap, String tableKey, String pk, List<String> fields, int size) {
		for (int i = 0; i < size; i++) {
			String key = fields.get(i);
			String value = String.valueOf(parameterValues.get(i));
			fieldsMap.put(key, value);
			if (key.equals(pk)) {
				redisKey = tableKey.concat(DmConstants.UNDERLINE).concat(value);
			}
		}
		return redisKey;
	}

	private String combineFields(List<Object> parameterValues, Map<String, String> fieldsMap, String tableKey, String pk, List<Column> columns) {
		String redisKey;
		String pkIncValue = task.getPkIncValue();
		int columnsSize = columns.size();
		for (int i = 0; i < columnsSize; i++) {
			fieldsMap.put(columns.get(i).getColumnName(), String.valueOf(parameterValues.get(i)));
		}
		fieldsMap.put(pk, pkIncValue);
		redisKey = tableKey.concat(DmConstants.UNDERLINE).concat(pkIncValue);
		return redisKey;
	}

	/**
	 * 获取插入参数
	 * @param itemsList
	 * @param parameterValues
	 * @return List<Object> parameterValues
	 */
	private List<Object> getParameterValues(ArrayList<Object>[] parameters, ItemsList itemsList, List<Object> parameterValues) {
		if (CollectionUtils.isNotEmpty(parameters)) {
			//预编译形式获取参数
			for (List<Object> p: parameters) {
				parameterValues.addAll(p);
			}
		} else {
			//从sql中解析获取参数
			if (itemsList instanceof ExpressionList) {
				ExpressionList exItemsList = (ExpressionList)itemsList;
				List<Expression> expressions = exItemsList.getExpressions();
				List<Object> collect = expressions.stream().collect(Collectors.toList());
				parameterValues.addAll(collect);
			}
		}
		return parameterValues;
	}
}
