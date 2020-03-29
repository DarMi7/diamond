/*
package com.dm.database;

import StatementProxy;
import SQLRecognizer;
import java.parser.SQLException;
import java.parser.Statement;
import java.util.concurrent.Executor;
import org.springframework.jdbc.core.StatementCallback;

*/
/**
 *
 * @author zy
 *  execute template
 *//*


public class ExecuteTemplate {


    public static <T, S extends Statement> T execute(StatementProxy<S> statementProxy,
                                                     Object... args) throws SQLException {

		S targetStatement = statementProxy.getTargetStatement();

		targetStatement.execute(args);
		if (!RootContext.inGlobalTransaction() && !RootContext.requireGlobalLock()) {
            // Just work as original statement
            return statementCallback.execute(statementProxy.getTargetStatement(), args);
        }

        if (sqlRecognizer == null) {
            sqlRecognizer = SQLVisitorFactory.get(
                    statementProxy.getTargetSQL(),
                    statementProxy.getConnectionProxy().getDbType());
        }
        Executor<T> executor;
        if (sqlRecognizer == null) {
            executor = new PlainExecutor<>(statementProxy, statementCallback);
        } else {
            switch (sqlRecognizer.getSQLType()) {
                case INSERT:
                    executor = new InsertExecutor<>(statementProxy, statementCallback, sqlRecognizer);
                    break;
                case UPDATE:
                    executor = new UpdateExecutor<>(statementProxy, statementCallback, sqlRecognizer);
                    break;
                case DELETE:
                    executor = new DeleteExecutor<>(statementProxy, statementCallback, sqlRecognizer);
                    break;
                case SELECT_FOR_UPDATE:
                    executor = new SelectForUpdateExecutor<>(statementProxy, statementCallback, sqlRecognizer);
                    break;
                default:
                    executor = new PlainExecutor<>(statementProxy, statementCallback);
                    break;
            }
        }
        T rs;
        try {
            rs = executor.execute(args);
        } catch (Throwable ex) {
            if (!(ex instanceof SQLException)) {
                // Turn other exception into SQLException
                ex = new SQLException(ex);
            }
            throw (SQLException)ex;
        }
        return rs;
    }
}
*/
