package com.huangmaojie.read.mybatis.resource.resolverutil;

/**
 * @author huangmaojie
 * @date 2021/11/30
 */
public class CustomTest implements ResolverUtil.Test {
    @Override
    public boolean matches(Class<?> type) {
        return false;
    }
}
