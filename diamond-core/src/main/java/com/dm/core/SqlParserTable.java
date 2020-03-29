package com.dm.core;

import java.util.List;

/**
 * @author zy
 * 数据库表相关信息
 */
public class SqlParserTable {

	private String databaseName;

	private String tableName;

	private String pk;

	private String fieldsJsonString;

	private List<String> fields;

	public String getFieldsJsonString() {
		return fieldsJsonString;
	}

	public void setFieldsJsonString(String fields) {
		this.fieldsJsonString = fields;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	@Override
	public String toString() {
		return "TableContext{" +
				"databaseName='" + databaseName + '\'' +
				", tableName='" + tableName + '\'' +
				", pk='" + pk + '\'' +
				", fieldsJsonString='" + fieldsJsonString + '\'' +
				'}';
	}
}