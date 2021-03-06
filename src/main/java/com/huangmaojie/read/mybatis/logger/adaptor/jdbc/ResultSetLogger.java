/**
 * Copyright 2009-2019 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.huangmaojie.read.mybatis.logger.adaptor.jdbc;

import com.huangmaojie.read.mybatis.logger.adaptor.Log;
import org.apache.ibatis.reflection.ExceptionUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;

/**
 * ResultSet proxy to add logging
 *
 * @author Clinton Begin
 * @author Eduardo Macarron
 * @see org.apache.ibatis.logging.jdbc.ResultSetLogger
 *
 */
public final class ResultSetLogger extends BaseJdbcLogger implements InvocationHandler {
    // 记录了超大长度的类型
    private static Set<Integer> BLOB_TYPES = new HashSet<>();
    // 是否是ResultSet结果集的第一行
    private boolean first = true;
    // 统计行数
    private int rows;
    // 真正的ResultSet对象
    private final ResultSet rs;
    // 记录了起大字段的列编号
    private final Set<Integer> blobColumns = new HashSet<>();

    static {
        // 添加BLOB、CLOB、LONGNVARCHAR、LONGVARBINARY等类型
        BLOB_TYPES.add(Types.BINARY);
        BLOB_TYPES.add(Types.BLOB);
        BLOB_TYPES.add(Types.CLOB);
        BLOB_TYPES.add(Types.LONGNVARCHAR);
        BLOB_TYPES.add(Types.LONGVARBINARY);
        BLOB_TYPES.add(Types.LONGVARCHAR);
        BLOB_TYPES.add(Types.NCLOB);
        BLOB_TYPES.add(Types.VARBINARY);
    }

    private ResultSetLogger(ResultSet rs, Log statementLog, int queryStack) {
        super(statementLog, queryStack);
        this.rs = rs;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
        try {
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, params);
            }
            Object o = method.invoke(rs, params);
            if ("next".equals(method.getName())) {
                // 针对ResultSet.next()方法的处理
                if ((Boolean) o) {
                    rows++;
                    if (isTraceEnabled()) {
                        ResultSetMetaData rsmd = rs.getMetaData();
                        // 获取数据集的列数
                        final int columnCount = rsmd.getColumnCount();
                        // 如果是第一行数据 ，则输出表头
                        if (first) {
                            first = false;
                            // 除了输出表头，还会填充blobColumns集合，记录超大类型的列
                            printColumnHeaders(rsmd, columnCount);
                        }
                        // 输出该行记录，注意会过滤掉blobColumns中记录的列，这些列的数据较大，不会输出到日志
                        printColumnValues(columnCount);
                    }
                } else {
                    // 遍历完ResultSet之后，会输出主函数
                    debug("     Total: " + rows, false);
                }
            }
            // 清空BaseJdbcLogger中的column*集合
            clearColumnInfo();
            return o;
        } catch (Throwable t) {
            throw ExceptionUtil.unwrapThrowable(t);
        }
    }

    private void printColumnHeaders(ResultSetMetaData rsmd, int columnCount) throws SQLException {
        StringBuilder row = new StringBuilder();
        row.append("   Columns: ");
        for (int i = 1; i <= columnCount; i++) {
            if (BLOB_TYPES.contains(rsmd.getColumnType(i))) {
                blobColumns.add(i);
            }
            String colname = rsmd.getColumnLabel(i);
            row.append(colname);
            if (i != columnCount) {
                row.append(", ");
            }
        }
        trace(row.toString(), false);
    }

    private void printColumnValues(int columnCount) {
        StringBuilder row = new StringBuilder();
        row.append("       Row: ");
        for (int i = 1; i <= columnCount; i++) {
            String colname;
            try {
                if (blobColumns.contains(i)) {
                    colname = "<<BLOB>>";
                } else {
                    colname = rs.getString(i);
                }
            } catch (SQLException e) {
                // generally can't call getString() on a BLOB column
                colname = "<<Cannot Display>>";
            }
            row.append(colname);
            if (i != columnCount) {
                row.append(", ");
            }
        }
        trace(row.toString(), false);
    }

    /**
     * Creates a logging version of a ResultSet
     *
     * @param rs - the ResultSet to proxy
     * @return - the ResultSet with logging
     */
    public static ResultSet newInstance(ResultSet rs, Log statementLog, int queryStack) {
        InvocationHandler handler = new ResultSetLogger(rs, statementLog, queryStack);
        ClassLoader cl = ResultSet.class.getClassLoader();
        return (ResultSet) Proxy.newProxyInstance(cl, new Class[]{ResultSet.class}, handler);
    }

    /**
     * Get the wrapped result set
     *
     * @return the resultSet
     */
    public ResultSet getRs() {
        return rs;
    }

}
