package com.dm.core;

import java.sql.SQLType;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author zy
 * parser parser task
 */
public class SqlParserTask {
	/**
	 * parser type
	 */
	private SQLType sqlType;
	/**
	 * target parser
	 */
	private String boundSql;
	/**
	 * parser parameters
	 */
	private ArrayList<Object>[] parameters;
	/**
	 * the table's primary key
	 */
	private String pkKey;
	/**
	 * the reid key prefix
	 */
	private String keyPrefix;

	private String pkIncValue;

	public SqlParserTask() {
	}

	public String getPkKey() {
		return pkKey;
	}

	public SqlParserTask(String boundSql) {
		this.boundSql = boundSql;
	}


	public SqlParserTask(ArrayList<Object>[] parameters, String boundSql) {
		this.parameters = parameters;
		this.boundSql = boundSql;
	}

	public void setSqlType(SQLType sqlType) {
		this.sqlType = sqlType;
	}

	public void setBoundSql(String boundSql) {
		this.boundSql = boundSql;
	}

	public SqlParserTask(ArrayList<Object>[] parameters) {
		this.parameters = parameters;
	}

	public String getBoundSql() {
		return boundSql;
	}

	public void setParameters(ArrayList<Object>[] parameters) {
		this.parameters = parameters;
	}

	public ArrayList<Object>[] getParameters() {
		return parameters;
	}

	public String getPkIncValue() {
		return pkIncValue;
	}

	public void setPkIncValue(String pkIncValue) {
		this.pkIncValue = pkIncValue;
	}

	@Override
	public String toString() {
		return "SqlParserTask{" +
				"sqlType=" + sqlType +
				", boundSql='" + boundSql + '\'' +
				", parameters=" + Arrays.toString(parameters) +
				", pkKey='" + pkKey + '\'' +
				", keyPrefix='" + keyPrefix + '\'' +
				", pkIncValue='" + pkIncValue + '\'' +
				'}';
	}
}

