package com.dm.spring.datasource;

import com.dm.database.proxy.DataSourceProxy;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;

/**
 * data source proxy holder
 *
 * @author zy
 */
public class DataSourceProxyHolder {
    /**
     * initial capacity
     */
    private static final int MAP_INITIAL_CAPACITY = 4;
    /**
     * dataSource cache
     */
    private ConcurrentHashMap<DataSource, DataSourceProxy> dataSourceProxyMap;

    private DataSourceProxyHolder() {
        dataSourceProxyMap = new ConcurrentHashMap<>(MAP_INITIAL_CAPACITY);
    }


    public ConcurrentHashMap<DataSource, DataSourceProxy> getDataSourceProxyMap() {
        return dataSourceProxyMap;
    }

    /**
     * the type holder
     */
    private static class Holder {
        private static final DataSourceProxyHolder INSTANCE;
        static {
            INSTANCE = new DataSourceProxyHolder();
        }

    }

    /**
     * Get DataSourceProxyHolder instance
     *
     * @return the INSTANCE of DataSourceProxyHolder
     */
    public static DataSourceProxyHolder get() {
        return Holder.INSTANCE;
    }

    /**
     * Put dataSource
     *
     * @param dataSource
     * @return dataSourceProxy
     */
    public DataSourceProxy putDataSource(DataSource dataSource) {
        return this.dataSourceProxyMap.computeIfAbsent(dataSource, DataSourceProxy::new);
    }

    /**
     * Get dataSourceProxy
     *
     * @param dataSource
     * @return dataSourceProxy
     */
    public DataSourceProxy getDataSourceProxy(DataSource dataSource) {
        return this.dataSourceProxyMap.get(dataSource);
    }

}
