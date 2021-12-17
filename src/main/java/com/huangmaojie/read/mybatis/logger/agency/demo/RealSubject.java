package com.huangmaojie.read.mybatis.logger.agency.demo;

/**
 * @author huangmaojie
 * @date 2021/12/7
 */
public class RealSubject implements Subject {

    @Override
    public void select() {
        System.out.println("Hello World");
    }
}
