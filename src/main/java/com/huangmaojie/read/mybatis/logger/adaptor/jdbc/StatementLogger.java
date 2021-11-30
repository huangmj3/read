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
import java.sql.Statement;

/**
 * Statement proxy to add logging
 *
 * @author Clinton Begin
 * @author Eduardo Macarron
 * @see org.apache.ibatis.logging.jdbc.StatementLogger
 *
 */
public final class StatementLogger extends BaseJdbcLogger implements InvocationHandler {

    private final Statement statement;

    private StatementLogger(Statement stmt, Log statementLog, int queryStack) {
        super(statementLog, queryStack);
        this.statement = stmt;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
        // 为EXECUTE_METHODS集合中的方法、getResultSet()等方法提供代理
        try {
            // 如采调用的是从Object继承的方法，则直接调用，不做任何其他处理
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, params);
            }
            // 调用了EXECUTE_METHODS集合中的方法
            if (EXECUTE_METHODS.contains(method.getName())) {
                if (isDebugEnabled()) {
                    // 日志输出，输出的是参数值以及参数类型
                    debug(" Executing: " + removeBreakingWhitespace((String) params[0]), true);
                }
                if ("executeQuery".equals(method.getName())) {
                    // 如果调用executeQuery()方法，则为ResultSet创建代理对象
                    ResultSet rs = (ResultSet) method.invoke(statement, params);
                    return rs == null ? null : ResultSetLogger.newInstance(rs, statementLog, queryStack);
                } else {
                    // 不是executeQuery()方法则直接返回结果
                    return method.invoke(statement, params);
                }
            } else if ("getResultSet".equals(method.getName())) {
                // 如果调用getResultSet()方法，则为ResultSet建代理对象
                ResultSet rs = (ResultSet) method.invoke(statement, params);
                return rs == null ? null : ResultSetLogger.newInstance(rs, statementLog, queryStack);
            } else {
                return method.invoke(statement, params);
            }
        } catch (Throwable t) {
            throw ExceptionUtil.unwrapThrowable(t);
        }
    }

    /**
     * Creates a logging version of a Statement
     *
     * @param stmt - the statement
     * @return - the proxy
     */
    public static Statement newInstance(Statement stmt, Log statementLog, int queryStack) {
        InvocationHandler handler = new StatementLogger(stmt, statementLog, queryStack);
        ClassLoader cl = Statement.class.getClassLoader();
        return (Statement) Proxy.newProxyInstance(cl, new Class[]{Statement.class}, handler);
    }

    /**
     * return the wrapped statement
     *
     * @return the statement
     */
    public Statement getStatement() {
        return statement;
    }

}
