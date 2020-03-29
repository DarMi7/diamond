package com.dm.core;

import java.util.List;

/**
 * @author zy
 */
public class TableContextBuilder{

		private SqlParserTable tableContext;

		public TableContextBuilder getBuilder(){
			this.tableContext = new SqlParserTable();
			return this;
		}

		public TableContextBuilder buildDbName(String dbName) {
			tableContext.setDatabaseName(dbName);
			return this;
		}

		public TableContextBuilder buildTableName(String tableName) {
			tableContext.setTableName(tableName);
			return this;
		}

		public TableContextBuilder buildPk(String pk) {
			tableContext.setPk(pk);
			return this;
		}

		public SqlParserTable build() {
			return tableContext;
		}

	public TableContextBuilder buildFieldsJsonString(String fieldsJsonString) {
		tableContext.setFieldsJsonString(fieldsJsonString);
		return this;
	}

	public TableContextBuilder buildFields(List<String> fields) {
		tableContext.setFields(fields);
		return this;
	}
}