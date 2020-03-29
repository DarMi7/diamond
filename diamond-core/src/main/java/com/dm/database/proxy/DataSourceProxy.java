package com.dm.database.proxy;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author zy
 * data source proxy
 */
public class DataSourceProxy implements DataSource {
	/**
	 * target data source
	 */
	protected DataSource targetDataSource;

	public DataSourceProxy() {

	}

	public DataSourceProxy(DataSource targetDataSource) {
		this.targetDataSource = targetDataSource;
	}

	@Override
	public ConnectionProxy getConnection() throws SQLException {
		Connection targetConnection = targetDataSource.getConnection();
		return new ConnectionProxy(this, targetConnection);
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		Connection targetConnection = targetDataSource.getConnection(username, password);
		return new ConnectionProxy(this, targetConnection);
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return targetDataSource.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return targetDataSource.isWrapperFor(iface);
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return targetDataSource.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		targetDataSource.setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		targetDataSource.setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return targetDataSource.getLoginTimeout();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return targetDataSource.getParentLogger();
	}
}
