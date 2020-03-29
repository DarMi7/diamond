package com.dm;

import com.dm.core.SqlParserTask;
import java.sql.SQLType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zy
 * @Description:
 */
public class TaskBuilder {

	private static SqlParserTask sqlParserTask;

	public SqlParserTask buildSqlType(String sql) {
		sqlParserTask.setBoundSql(sql);
		return sqlParserTask;
	}

	public SqlParserTask buildBoundSql(SQLType sql) {
		sqlParserTask.setSqlType(sql);
		return sqlParserTask;
	}

	public TaskBuilder getBuilder() {
		sqlParserTask = new SqlParserTask();
		return new TaskBuilder();
	}

	public static void main(String[] args) {
		Map<String, String> map1 = new HashMap<>();
		Map<String, String> map2 = new HashMap<>();
		Map<String, String> map3 = new HashMap<>();
		map1.put("id_1","e-11");
		map2.put("ids_1","e-22");
		map3.put("name","e-33");
		ArrayList<Map<String, String>> objects = new ArrayList<>();

		objects.add(map1);
		objects.add(map2);
		objects.add(map3);
		String pk = "name";
		String pkValue = objects.stream().filter(e -> e.get(pk) != null).map(e -> e.get(pk)).findFirst().get();


		System.out.println(pkValue);


	}
}
