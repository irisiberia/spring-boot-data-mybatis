package com.example.springbootdatamybatis.mozart.dynamic.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:01
 **/
public class DynamicDataSource extends AbstractRoutingDataSource {
    /**
     * Determine the current lookup key. This will typically be implemented to check a thread-bound transaction context.
     * <p>
     * Allows for arbitrary keys. The returned key needs to match the stored lookup key type, as resolved by the
     * {@link #resolveSpecifiedLookupKey} method.
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DbContextHolder.getDbType();
    }
}
