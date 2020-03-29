package com.dm.core;

import com.dm.constant.DmConstants;
import com.dm.database.proxy.DataSourceProxy;
import com.dm.spring.datasource.DataSourceProxyHolder;
import com.dm.util.CollectionUtils;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;


/**
 * @author zy
 * 缓存表的主键和所有字段等信息
 */
public class DatabaseMeta implements InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseMeta.class);

	private String[] asyncTables;

	private static Map<String, SqlParserTable> tableCache = new ConcurrentHashMap<>(DmConstants.INITIAL_CAPACITY);

	private static String databaseName;

	public DatabaseMeta() {
	}

	public DatabaseMeta(String[] asyncTables) {
		this.asyncTables = asyncTables;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}

	/**
	 * 缓存表的基本信息
	 * @throws SQLException
	 */
	private void init() throws SQLException {
		DataSourceProxyHolder dataSourceProxyHolder = DataSourceProxyHolder.get();
		ConcurrentHashMap<DataSource, DataSourceProxy> dataSourceProxyMap = dataSourceProxyHolder.getDataSourceProxyMap();
		for (DataSource d:dataSourceProxyMap.values()) {
			Connection connection = d.getConnection();
			databaseName= connection.getCatalog();
			LOGGER.info("TablesContext init: database name: [{}]", databaseName);
			//获取数据库元数据信息
			DatabaseMetaData metaData = connection.getMetaData();
			String databaseProductName = metaData.getDatabaseProductName();
			if (!DmConstants.DATABASE_PRODUCT_NAME.equals(databaseProductName)) {
				continue;
			}
			if (CollectionUtils.isEmpty(asyncTables)){
				LOGGER.warn("The configuration of synchronized tables is null");
				return;
			}
			LOGGER.info("synchronized tables are:[{}]", Arrays.toString(asyncTables));
			Arrays.stream(asyncTables).forEach(e -> {
				try {
					//获取主键信息
					ResultSet pk = metaData.getPrimaryKeys(databaseName, metaData.getUserName(), e);
					while (pk.next()) {
						String tableName = pk.getObject(3).toString();

						ResultSet rs = metaData.getColumns(databaseName, DmConstants.PERCENT, tableName,  DmConstants.PERCENT);
						//获取所有表的字段
						Map<String,Null> columnNameMap = new HashMap<>(DmConstants.INITIAL_CAPACITY);
						List<String> fields = new LinkedList<>();
						while (rs.next()) {
							String column_name = rs.getString("COLUMN_NAME");
							columnNameMap.put(column_name, Null.get());
							fields.add(column_name);
						}
						String fieldJson = new Gson().toJson(columnNameMap);

						SqlParserTable tableContext = new TableContextBuilder().getBuilder()
								.buildDbName(databaseName)
								.buildTableName(tableName)
								.buildPk(pk.getObject(4).toString())
								.buildFieldsJsonString(fieldJson)
								.buildFields(fields)
								.build();
						//缓存表的基本信息
						tableCache.put(databaseName.concat(DmConstants.UNDERLINE).concat(tableName),
								tableContext);

					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			});
			LOGGER.info("init table context:[{}]",tableCache);
		}

	}

	public static Map<String, SqlParserTable> getTableCache() {
		return tableCache;
	}

	public static String getDatabaseName() {
		return databaseName;
	}
}
