/**
 *    Copyright 2009-2017 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.huangmaojie.read.mybatis.logger.adaptor.jdk14;

import com.huangmaojie.read.mybatis.logger.adaptor.Log;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Clinton Begin
 * @see org.apache.ibatis.logging.jdk14.Jdk14LoggingImpl
 */
public class Jdk14LoggingImpl implements Log {

  // 底层封装的java.util.logging.Logger对象
  private final Logger log;

  public Jdk14LoggingImpl(String clazz) {
    // 初始化java.util.logging.Logger对象
    log = Logger.getLogger(clazz);
  }

  @Override
  public boolean isDebugEnabled() {
    return log.isLoggable(Level.FINE);
  }

  @Override
  public boolean isTraceEnabled() {
    return log.isLoggable(Level.FINER);
  }

  @Override
  public void error(String s, Throwable e) {
    // 将请求委托给了java.util.logging.Logger对象的对应方法
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
