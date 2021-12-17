package com.huangmaojie.read.mybatis.logger.agency.jdkdaynamic;

/**
 * @author huangmaojie
 * @date 2021/12/7
 */
public class UserServiceImpl implements UserService {

    @Override
    public void select() {
        System.out.println("查询 selectById");
    }
}
