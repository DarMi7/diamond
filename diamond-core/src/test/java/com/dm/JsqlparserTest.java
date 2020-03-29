package com.dm;

import com.dm.util.StringUtils;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Database;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

/**
 * @author zy
 */
public class JsqlparserTest {
	public static void main(String[] args) throws JSQLParserException {
		insertIntoDemo();
	}

	public static void insertIntoDemo() throws JSQLParserException {
		CCJSqlParserManager pm = new CCJSqlParserManager();

		StringBuffer stringBuffer = new StringBuffer();
		//insert into metrics_03 select pk,money,0 from metrics_01
		//stringBuffer.append("insert into tbl_name1(col1,col2) select col3,col4 from tbl_name2");


/*		stringBuffer.append(" INSERT INTO FAE_CFG_CRI_AMOUNTTYPE (")
		.append(" PK_AMOUNTTYPECRITERION,")
		.append(" PK_AMOUNTTYPE,")
		.append(" AMOUNTTYPE_CODE")
		.append(" )")
		.append(" SELECT")
		.append(" B.PK_AMOUNTTYPECRITERION,")
		.append(" C.PK_AMOUNTTYPE,C.CODE AMOUNTTYPE_CODE")
		.append(" FROM FAE_AMOUNTTYPECRITERION_B  A INNER JOIN  FAE_AMOUNTTYPECRITERION B")
		.append(" ON A.PK_AMOUNTTYPECRITERION=B.PK_AMOUNTTYPECRITERION")
		.append(" LEFT JOIN FAE_AMOUNTTYPE C")
		.append(" ON A.PK_AMOUNTTYPE=C.PK_AMOUNTTYPE")
		.append(" WHERE NVL(A.DR,0)=0")
		.append(" AND NVL(B.ISENABLE,'Y')='Y' AND NVL(B.DR,0)=0")
		.append(" AND NVL(C.ISENABLE,'Y')='Y' AND NVL(C.DR,0)=0");*/



	/*	stringBuffer.append(" INSERT INTO FAE_VOUCHER_B")
				.append(" PK_AMOUNTTYPECRITERION,")
				.append(" PK_AMOUNTTYPE,")
				.append(" AMOUNTTYPE_CODE")
				.append(" )")
				.append(" WITH FAE_ASSFLOW_B_TEMP AS (")
				.append("  SELECT")
				.append(" T1.RECORD_DT ,")
				.append(" T1.DEAL_DT ,")
				.append(" T1.DEAL_CODE ")
				.append(" CASE WHEN T1.CDFLAG='C' THEN  DEAL_AMT")
				.append(" WHEN T1.CDFLAG='D' THEN -DEAL_AMT")
				.append(" ELSE 0 END DEAL_AMT")
				.append(" ON A.PK_AMOUNTTYPE=C.PK_AMOUNTTYPE")
				.append(" WHERE NVL(A.DR,0)=0")
				.append(" AND NVL(B.ISENABLE,'Y')='Y' AND NVL(B.DR,0)=0")
				.append(" AND NVL(C.ISENABLE,'Y')='Y' AND NVL(C.DR,0)=0");*/

		//stringBuffer.append("update user set name = 'zy' where id =1;");
		//stringBuffer.append("delete user where addr = stree1  and id = sdadsad and age =11 and z = 232;");

		stringBuffer.append("insert into user (username, password) values(1,2);");
		String sql = stringBuffer.toString();
		Statement statement = pm.parse(new StringReader(sql));


		System.out.println("parser:" + sql);
		if (statement instanceof Insert) {
			// 获得Update对象
			Insert istatement = (Insert) statement;
			// 获得表名
			System.out.println("table:" + istatement.getTable());

			List<Column> columns = istatement.getColumns();

			List<Column> setColumns = ((Insert) statement).getSetColumns();

			if (null == columns) {
				columns = null;//获取所有的表字段
			}

			//
			ItemsList itemsList = istatement.getItemsList();
			if (itemsList instanceof ExpressionList) {
				ExpressionList exItemsList = (ExpressionList)itemsList;
				List<Expression> expressions = exItemsList.getExpressions();
			}
			Select sele = istatement.getSelect();
			PlainSelect body = (PlainSelect) sele.getSelectBody();
			String selectStr = body.toString();

			System.out.println("-------");
			// 获得where条件表达式
			Expression where = null;
			if (istatement.getSetExpressionList().size() != 0) {
				where = istatement.getSetExpressionList().get(0);
				// 初始化接收获得到的字段信息
				StringBuffer allColumnNames = new StringBuffer();
				// BinaryExpression包括了整个where条件，
				// 例如：AndExpression/LikeExpression/OldOracleJoinBinaryExpression
				if (where instanceof BinaryExpression) {
					allColumnNames = getColumnName((BinaryExpression) (where), allColumnNames);
					System.out.println("allColumnNames:" + allColumnNames.toString());
				}
			}

		}

		if (statement instanceof Update) {
			Update istatement = (Update) statement;
			List<Table> tables = istatement.getTables();
			// 获得where条件表达式
			Expression where = null;
			List<Expression> expressions = istatement.getExpressions();
			Expression where1 = istatement.getWhere();

			if (expressions.size() != 0) {
				where = expressions.get(0);
				// 初始化接收获得到的字段信息
				StringBuffer allColumnNames = new StringBuffer();
				// BinaryExpression包括了整个where条件，
				// 例如：AndExpression/LikeExpression/OldOracleJoinBinaryExpression
				if (where instanceof BinaryExpression) {
					allColumnNames = getColumnName((BinaryExpression) (where), allColumnNames);
					System.out.println("allColumnNames:" + allColumnNames.toString());
				}
			}
		}

		Delete deStatement = (Delete) statement;
		Table table = deStatement.getTable();
		String name = table.getName();
		Database database = table.getDatabase();
		String databaseName = database.getDatabaseName();
		Expression where = deStatement.getWhere();
		if (where != null) {
			if (where instanceof BinaryExpression) {
				List<Map> pv = new ArrayList<>(32);
				List<Map> pkValue = getPKValue((BinaryExpression) (where), "id", pv);
				System.out.println("allColumnNames:" + pkValue);
			}

		}
	}

	private static List<Map> getPKValue(Expression expression, String pk, List<Map> pv) {
		if (StringUtils.isBlank(pk)) {
			return null;
		}
		Expression temp = expression;
		String columnName = null;
		if (temp instanceof BinaryExpression) {
			Expression leftExpression = ((BinaryExpression) temp).getLeftExpression();
			if (leftExpression instanceof BinaryExpression) {
				getPKValue(leftExpression, pk, pv);
			} else if (leftExpression instanceof Column) {
				columnName = ((Column) leftExpression).getColumnName();
			}
			Expression rightExpression = ((BinaryExpression) temp).getRightExpression();
			if (rightExpression instanceof BinaryExpression) {
				getPKValue(rightExpression, pk, pv);
			} else {
				Map<String, String> p = new HashMap<>(2);
				p.put(columnName, rightExpression.toString());
				pv.add(p);
			}
		}
		return pv;

	}

	public static void updateDemo() throws JSQLParserException {
		CCJSqlParserManager pm = new CCJSqlParserManager();

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("update ac_operator op ");
		stringBuffer.append("set op.errcount=(");
		stringBuffer.append("(select case when op1.errcount is null then 0 else op1.errcount end as errcount ");
		stringBuffer.append("from ac_operator op1 ");
		stringBuffer.append("where op1.loginname = '中国' )+1");
		stringBuffer.append("),lastlogin='中国' ");
		stringBuffer.append("where PROCESS_ID=");
		stringBuffer.append("(select distinct g.id from tempTable g where g.ID='中国')");
		stringBuffer.append("and columnName2 = '890' and columnName3 = '678' and columnName4 = '456'");

		Statement statement = pm.parse(new StringReader(stringBuffer.toString()));

		if (statement instanceof Update) {
			// 获得Update对象
			Update updateStatement = (Update) statement;
			// 获得表名
			System.out.println("table:" + updateStatement.getTables().get(0).getName());

			List<Column> columns = updateStatement.getColumns();

			for (Column column : columns) {
				System.out.println(column.getColumnName());
			}
			// 获得where条件表达式
			Expression where = updateStatement.getWhere();
			// 初始化接收获得到的字段信息
			StringBuffer allColumnNames = new StringBuffer();
			// BinaryExpression包括了整个where条件，
			// 例如：AndExpression/LikeExpression/OldOracleJoinBinaryExpression
			if (where instanceof BinaryExpression) {
				allColumnNames = getColumnName((BinaryExpression) (where), allColumnNames);
				System.out.println("allColumnNames:" + allColumnNames.toString());
			}
		}
	}

	/**
	 * 获得where条件字段中列名，以及对应的操作符 @Title: getColumnName @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param @param expression @param @param
	 * allColumnNames @param @return 设定文件 @return StringBuffer 返回类型 @throws
	 */
	private static StringBuffer getColumnName(Expression expression, StringBuffer allColumnNames) {

		String columnName = null;
		if (expression instanceof BinaryExpression) {
			// 获得左边表达式
			Expression leftExpression = ((BinaryExpression) expression).getLeftExpression();
			// 如果左边表达式为Column对象，则直接获得列名
			if (leftExpression instanceof Column) {
				// 获得列名
				columnName = ((Column) leftExpression).getColumnName();
				allColumnNames.append(columnName);
				allColumnNames.append(":");
				// 拼接操作符
				allColumnNames.append(((BinaryExpression) expression).getStringExpression());
				// allColumnNames.append("-");
			}
			// 否则，进行迭代
			else if (leftExpression instanceof BinaryExpression) {
				getColumnName((BinaryExpression) leftExpression, allColumnNames);
			}

			// 获得右边表达式，并分解
			Expression rightExpression = ((BinaryExpression) expression).getRightExpression();
			if (rightExpression instanceof BinaryExpression) {
				Expression leftExpression2 = ((BinaryExpression) rightExpression).getLeftExpression();
				if (leftExpression2 instanceof Column) {
					// 获得列名
					columnName = ((Column) leftExpression2).getColumnName();
					allColumnNames.append("-");
					allColumnNames.append(columnName);
					allColumnNames.append(":");
					// 获得操作符
					allColumnNames.append(((BinaryExpression) rightExpression).getStringExpression());
				}
			}
		}
		return allColumnNames;
	}


}
