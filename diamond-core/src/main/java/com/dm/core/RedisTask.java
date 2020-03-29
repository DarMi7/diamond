package com.dm.core;

import com.dm.database.SQLType;

/**
 * @author zy
 *  redie同步任务参数
 */
public class RedisTask {

	private SQLType sqlType;

	private String key;

	private String json;

	public RedisTask(String redisKey, SQLType sqlType) {
		key = redisKey;
		this.sqlType = sqlType;
	}

	public RedisTask(String key, String json, SQLType sqlType) {
		this(key,sqlType);
		this.json = json;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public SQLType getSqlType() {
		return sqlType;
	}

	@Override
	public String toString() {
		return "RedisTask{" +
				"key='" + key + '\'' +
				", json='" + json + '\'' +
				'}';
	}
}