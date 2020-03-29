package com.dm.database.proxy;

import com.dm.core.DmLocalContext;
import com.dm.disruptor.SeriesData;
import com.dm.disruptor.SeriesDataEventQueueHelper;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 *
 * @author zy
 * connection proxy
 */
public class ConnectionProxy implements Connection {
	/**
	 * data source proxy
	 */
	protected DataSourceProxy dataSourceProxy;
	/**
	 * target connection
	 */
	protected Connection targetConnection;
	/**
	 * event provider
	 */
	public SeriesDataEventQueueHelper seriesDataEventQueueHelper;

	public ConnectionProxy(DataSourceProxy dataSourceProxy, Connection targetConnection) {
		this.dataSourceProxy = dataSourceProxy;
		this.targetConnection = targetConnection;
	}

	public Connection getTargetConnection() {
		return targetConnection;
	}

	@Override
	public Statement createStatement() throws SQLException {
		return targetConnection.createStatement();
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		PreparedStatement targetPreparedStatement = null;
		if (targetPreparedStatement == null) {
			targetPreparedStatement = getTargetConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		}
		return new PreparedStatementProxy(this, targetPreparedStatement, sql);
	}

	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		return targetConnection.prepareCall(sql);
	}

	@Override
	public String nativeSQL(String sql) throws SQLException {
		return targetConnection.nativeSQL(sql);
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		targetConnection.setAutoCommit(autoCommit);
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		return targetConnection.getAutoCommit();
	}

	@Override
	public void commit() throws SQLException {
		targetConnection.commit();
		DmLocalContext cur = DmLocalContext.cur();
		if (cur != null) {
			SeriesDataEventQueueHelper.Instance().publishEvent(new SeriesData(cur.getTasks()));
		}
	}

	@Override
	public void rollback() throws SQLException {
		targetConnection.rollback();
		DmLocalContext cur = DmLocalContext.cur();
		cur = null;
	}

	@Override
	public void close() throws SQLException {
		targetConnection.close();
	}

	@Override
	public boolean isClosed() throws SQLException {
		return targetConnection.isClosed();
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		return targetConnection.getMetaData();
	}

	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
		targetConnection.setReadOnly(readOnly);
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		return targetConnection.isReadOnly();
	}

	@Override
	public void setCatalog(String catalog) throws SQLException {
		targetConnection.setCatalog(catalog);
	}

	@Override
	public String getCatalog() throws SQLException {
		return targetConnection.getCatalog();
	}

	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		targetConnection.setTransactionIsolation(level);
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		return targetConnection.getTransactionIsolation();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return targetConnection.getWarnings();
	}

	@Override
	public void clearWarnings() throws SQLException {
		targetConnection.clearWarnings();
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		Statement statement = targetConnection.createStatement(resultSetType, resultSetConcurrency);
		return new StatementProxy<>(this, statement);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
			throws SQLException {
	/*	PreparedStatement preparedStatement = targetConnection.prepareStatement(sql, resultSetType,
				resultSetConcurrency);*/
		PreparedStatement preparedStatement = targetConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		return new PreparedStatementProxy(this, preparedStatement, sql);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		return targetConnection.prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return targetConnection.getTypeMap();
	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		targetConnection.setTypeMap(map);
	}

	@Override
	public void setHoldability(int holdability) throws SQLException {
		targetConnection.setHoldability(holdability);
	}

	@Override
	public int getHoldability() throws SQLException {
		return targetConnection.getHoldability();
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		return targetConnection.setSavepoint();
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		return targetConnection.setSavepoint(name);
	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		targetConnection.rollback(savepoint);
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		targetConnection.releaseSavepoint(savepoint);
	}
	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		Statement statement = targetConnection.createStatement(resultSetType, resultSetConcurrency,
				resultSetHoldability);
		return new StatementProxy<>(this, statement);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
											  int resultSetHoldability) throws SQLException {
		PreparedStatement preparedStatement = targetConnection.prepareStatement(sql, resultSetType,
				resultSetConcurrency, resultSetHoldability);
		return new PreparedStatementProxy(this, preparedStatement, sql);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
										 int resultSetHoldability) throws SQLException {
		return targetConnection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		PreparedStatement preparedStatement = targetConnection.prepareStatement(sql, autoGeneratedKeys);
		return new PreparedStatementProxy(this, preparedStatement, sql);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		PreparedStatement preparedStatement = targetConnection.prepareStatement(sql, columnIndexes);
		return new PreparedStatementProxy(this, preparedStatement, sql);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		PreparedStatement preparedStatement = targetConnection.prepareStatement(sql, columnNames);
		return new PreparedStatementProxy(this, preparedStatement, sql);
	}


	@Override
	public Clob createClob() throws SQLException {
		return targetConnection.createClob();
	}

	@Override
	public Blob createBlob() throws SQLException {
		return targetConnection.createBlob();
	}

	@Override
	public NClob createNClob() throws SQLException {
		return targetConnection.createNClob();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		return targetConnection.createSQLXML();
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		return targetConnection.isValid(timeout);
	}

	@Override
	public void setClientInfo(String name, String value) throws SQLClientInfoException {
		targetConnection.setClientInfo(name, value);
	}

	@Override
	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		targetConnection.setClientInfo(properties);
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		return targetConnection.getClientInfo(name);
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		return targetConnection.getClientInfo();
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		return targetConnection.createArrayOf(typeName, elements);
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
		return targetConnection.createStruct(typeName, attributes);
	}

	@Override
	public void setSchema(String schema) throws SQLException {
		targetConnection.setSchema(schema);
	}

	@Override
	public String getSchema() throws SQLException {
		return targetConnection.getSchema();
	}

	@Override
	public void abort(Executor executor) throws SQLException {
		targetConnection.abort(executor);
	}

	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
		targetConnection.setNetworkTimeout(executor, milliseconds);
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		return targetConnection.getNetworkTimeout();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return targetConnection.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return targetConnection.isWrapperFor(iface);
	}
}
