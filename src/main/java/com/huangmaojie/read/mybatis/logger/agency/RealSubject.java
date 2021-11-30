package com.huangmaojie.read.mybatis.logger.agency;

/**
 * @author huangmaojie
 * @date 2021/11/30
 */
public class RealSubject implements Subject {

    @Override
    public void operation() {
        System.out.println("this is realSubject operation");
    }
}

