package com.huangmaojie.read.mybatis.logger.agency.demo;

import com.huangmaojie.read.mybatis.logger.agency.jdkdaynamic.LogHandler;

import java.lang.reflect.Proxy;

public class MyJdkProxyTest {

    public static void main(String[] args) {
        RealSubject subject = new RealSubject();
        ClassLoader classLoader = subject.getClass().getClassLoader();
        Class<?>[] interfaces = subject.getClass().getInterfaces();
        LogHandler logHandler = new LogHandler(subject);
        Subject proxy = (Subject) Proxy.newProxyInstance(classLoader, interfaces, logHandler);
        proxy.select();
    }
}
