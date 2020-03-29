package com.dm.core.parser;

import com.dm.core.DatabaseMeta;
import com.dm.core.Pipeline;
import com.dm.core.PipelineContext;
import com.dm.core.SqlParserTable;
import com.dm.core.SqlParserTask;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;

/**
 * @author zy
 * @Description:
 */
public abstract class SqlParserTemplate implements SqlParser, Runnable {
	//sql解析任务
	protected SqlParserTask task;
	//解析管道
	protected Pipeline pipeline;

	public SqlParserTemplate() {
	}

	public SqlParserTemplate(SqlParserTask task, Pipeline pipeline) {
		this.task = task;
		this.pipeline = pipeline;
	}

	@Override
	public void parser(SqlParserTask task, Pipeline pipeline) {
		this.task = task;
		this.pipeline = pipeline;
		PipelineContext.taskThreadPoolExecutor.execute(this);
	}

	@Override
	public void run() {
		try {
			SqlTypeParser(getStatement());
		} catch (JSQLParserException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取解析sql对象
	 *
	 * @return
	 * @throws JSQLParserException
	 */
	private Statement getStatement() throws JSQLParserException {
		String boundSql = this.task.getBoundSql();
		CCJSqlParserManager pm = new CCJSqlParserManager();
		return pm.parse(new StringReader(boundSql));
	}

	/**
	 * @param statement
	 * @throws InterruptedException
	 */
	protected void SqlTypeParser(Statement statement) throws InterruptedException {
		Statement convertStatement = convertParserType(statement);
		if (convertStatement == null) {
			return;
		}
		Map<String, SqlParserTable> tableCache = DatabaseMeta.getTableCache();
		parserParameters(convertStatement, tableCache);

	}

	/**
	 * 转换为对应的处理类型
	 * @param statement
	 * @return statement
	 */
	protected abstract Statement convertParserType(Statement statement);

	/**
	 * 解析参数到bq
	 * @param statement
	 * @param tableMap
	 * @throws InterruptedException
	 */
	protected abstract void parserParameters(Statement statement, Map<String, SqlParserTable> tableMap) throws InterruptedException;


	/**
	 * 获取sql参数中的键值
	 *
	 * @param expression
	 * @param pv
	 * @return list
	 */
	protected List<Map<String, String>> getPKValue(Expression expression, List<Map<String, String>> pv) {
		Expression temp = expression;
		String columnName = null;
		if (temp instanceof BinaryExpression) {
			Expression leftExpression = ((BinaryExpression) temp).getLeftExpression();
			if (leftExpression instanceof BinaryExpression) {
				getPKValue(leftExpression, pv);
			} else if (leftExpression instanceof Column) {
				columnName = ((Column) leftExpression).getColumnName();
			}
			Expression rightExpression = ((BinaryExpression) temp).getRightExpression();
			if (rightExpression instanceof BinaryExpression) {
				getPKValue(rightExpression, pv);
			} else {
				Map<String, String> p = new HashMap<>(2);
				p.put(columnName, rightExpression.toString());
				pv.add(p);
			}
		}
		return pv;

	}

}
