package com.dm;

import com.dm.core.SqlParserTask;
import java.util.Arrays;

/**
 * @author zy
 */
public class DisruptorTest {
	static final int MAXIMUM_CAPACITY = 1 << 30;

	public static void main(String[] args) {
/*
		SeriesDataEventQueueHelper seriesDataEventQueueHelper= new SeriesDataEventQueueHelper();
		List<SqlParserTask> datas = new ArrayList<>();
		datas.add(new SqlParserTask("test"));
		seriesDataEventQueueHelper.publishEvent(new SeriesData(datas));
*/

		Arrays.asList(new Object[]{null}).stream().forEach(e -> System.out.println(e.toString()));
		SqlParserTask delete = new TaskBuilder().getBuilder().buildSqlType("delete");
	}
}
