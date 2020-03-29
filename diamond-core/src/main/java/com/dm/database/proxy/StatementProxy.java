package com.dm.database.proxy;

import com.dm.constant.DmConstants;
import com.dm.core.DmLocalContext;
import com.dm.core.SqlParserTask;
import com.dm.database.AbstractStatementProxy;
import com.dm.util.StringUtils;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author zy
 * @param <T> the type parameter
 * Statement proxy
 */
public class StatementProxy<T extends Statement> extends AbstractStatementProxy<T> {

    public StatementProxy(Connection connectionWrapper, T targetStatement, String targetSQL)
        throws SQLException {
        super(connectionWrapper, targetStatement, targetSQL);
    }

    public StatementProxy(Connection connectionWrapper, T targetStatement) throws SQLException {
        this(connectionWrapper, targetStatement, null);
    }

    @Override
    public ConnectionProxy getConnectionProxy() {
        return (ConnectionProxy) super.getConnectionProxy();
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        return targetStatement.executeQuery(sql);
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        addTask(sql);
        return targetStatement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
    }


    @Override
    public boolean execute(String sql) throws SQLException {
        addTask(sql);
        return targetStatement.execute(sql, Statement.RETURN_GENERATED_KEYS);
    }


    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        addTask(sql);
        return targetStatement.executeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        addTask(sql);
        return targetStatement.executeUpdate(sql, columnIndexes);
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        addTask(sql);
        return targetStatement.executeUpdate(sql,columnNames);
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        addTask(sql);
        return targetStatement.execute(sql, autoGeneratedKeys);
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        addTask(sql);
        return targetStatement.execute(sql, columnIndexes);
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        addTask(sql);
        return targetStatement.execute(sql, columnNames);
    }

    @Override
    public int[] executeBatch() throws SQLException {
        addTask(null);
    return targetStatement.executeBatch();
    }

    protected void addTask(ArrayList<Object>[] parameters, String sql, String IncIdValue) {
        SqlParserTask task = addTask(parameters, sql);
        if (task != null) {
            task.setPkIncValue(IncIdValue);
        }
    }

    protected SqlParserTask addTask(ArrayList<Object>[] parameters, String sql) {
        SqlParserTask task = addTask(sql);
        DmLocalContext cur = DmLocalContext.cur();
        if (cur != null && cur.isSyncFlag() && task != null) {
            task.setParameters(parameters);
        }
        return task;
    }

    protected SqlParserTask addTask(String sql) {
        SqlParserTask task = null;
        if (excludeSelectSql(sql)) {
            DmLocalContext.addTask((task = new SqlParserTask(sql)));
        }
        return task;
    }

    private boolean excludeSelectSql(String sql) {
        return StringUtils.isNotBlank(sql) && !sql.toLowerCase().startsWith(DmConstants.SQL_SELECT_PREFIX);
    }
}
