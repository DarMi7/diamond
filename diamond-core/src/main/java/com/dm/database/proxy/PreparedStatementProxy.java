package com.dm.database.proxy;

import com.dm.database.AbstractPreparedStatementProxy;
import com.dm.database.ParametersHolder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author zy
 * prepared statement proxy
 */
public class PreparedStatementProxy extends AbstractPreparedStatementProxy
    implements PreparedStatement, ParametersHolder {

    @Override
    public ArrayList<Object>[] getParameters() {
        return parameters;
    }

    public PreparedStatementProxy(Connection connectionProxy, Statement targetStatement,
                                  String targetSQL) throws SQLException {
        super(connectionProxy, targetStatement, targetSQL);
    }

    @Override
    public boolean execute() throws SQLException {
        boolean executeRs= targetStatement.execute();
        addTask(parameters, boundSql, getIncId());
        return executeRs;
    }

    private String getIncId() throws SQLException {
        ResultSet rs = targetStatement.getGeneratedKeys();
        String incId = null;
        if (rs.next()) {
            incId = rs.getString(1);
        }
        return incId;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        return targetStatement.executeQuery();
    }

    @Override
    public int executeUpdate() throws SQLException {
        addTask(parameters,boundSql);
        return targetStatement.executeUpdate();
    }
}
