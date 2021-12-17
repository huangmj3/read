package com.huangmaojie.read.mybatis.logger.agency.jdkdaynamic;

import com.huangmaojie.read.mybatis.logger.agency.demo.RealSubject;
import com.huangmaojie.read.mybatis.logger.agency.demo.Subject;

import java.lang.reflect.Proxy;

/**
 * @author huangmaojie
 * @date 2021/10/28
 */
public class JdkDynamicDemo {

    public static void main(String[] args) {
        RealSubject subject = new RealSubject();
        ClassLoader classLoader = subject.getClass().getClassLoader();
        Class<?>[] interfaces = subject.getClass().getInterfaces();
        LogHandler logHandler = new LogHandler(subject);
        Subject proxy = (Subject) Proxy.newProxyInstance(classLoader, interfaces, logHandler);
        proxy.select();
    }
}
