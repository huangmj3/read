package com.huangmaojie.read.mybatis;

import org.apache.ibatis.logging.Log;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author huangmaojie
 * @date 2021/11/30
 */
public class Jdk14LoggingImpl implements Log {

    // 底层封装的Java.util.logging.Logger对象
    private final Logger log;

    public Jdk14LoggingImpl(String clazz) {
        // 初始化java.util.logging.Logger对象
        log = Logger.getLogger(clazz);
    }

    @Override
    public boolean isDebugEnabled() {
        // 将请求全部委托给了java.util.logging.Logger对象的相应方法
        return log.isLoggable(Level.FINE);
    }

    @Override
    public boolean isTraceEnabled() {
        return log.isLoggable(Level.FINER);
    }

    @Override
    public void error(String s, Throwable e) {
        log.log(Level.SEVERE, s, e);
    }

    @Override
    public void error(String s) {
        log.log(Level.SEVERE, s);
    }

    @Override
    public void debug(String s) {
        log.log(Level.FINE, s);
    }

    @Override
    public void trace(String s) {
        log.log(Level.FINER, s);
    }

    @Override
    public void warn(String s) {
        log.log(Level.WARNING, s);
    }

}
