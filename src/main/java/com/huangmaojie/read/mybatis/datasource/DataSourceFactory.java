/**
 *    Copyright 2009-2015 the original author or authors.
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
package com.huangmaojie.read.mybatis.datasource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author Clinton Begin
 * @see org.apache.ibatis.datasource.DataSourceFactory
 */
public interface DataSourceFactory {

  //设置 DataSource 的相关属性，一般紧跟在初始化完成之后
  void setProperties(Properties props);
  //获取 DataSource 对象
  DataSource getDataSource();

}
