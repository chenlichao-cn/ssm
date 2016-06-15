/*
 * Copyright 2016-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.chenlichao.web.ssm.support.mybatis.providers;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.util.StringUtil;

import java.util.Set;

/**
 * SQL生成器
 *
 * <br>author: 陈立朝
 * <br>date: 16/5/9 下午3:55
 * <br>version: V1.0.0
 */
public class SSMMapperProvider extends MapperTemplate{

    public SSMMapperProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 插入不为null的字段,这段代码比较复杂，这里举个例子
     * CountryU生成的insertSelective方法结构如下：
     * <pre>
     &lt;bind name="countryname_bind" value='@java.util.UUID@randomUUID().toString().replace("-", "")'/&gt;
     INSERT INTO country_u
     &lt;trim prefix="(" suffix=")" suffixOverrides=","&gt;
     &lt;if test="id != null"&gt;id,&lt;/if&gt;
     countryname,
     &lt;if test="countrycode != null"&gt;countrycode,&lt;/if&gt;
     &lt;/trim&gt;
     VALUES
     &lt;trim prefix="(" suffix=")" suffixOverrides=","&gt;
     &lt;if test="id != null"&gt;#{id,javaType=java.lang.Integer},&lt;/if&gt;
     &lt;if test="countryname != null"&gt;#{countryname,javaType=java.lang.String},&lt;/if&gt;
     &lt;if test="countryname == null"&gt;#{countryname_bind,javaType=java.lang.String},&lt;/if&gt;
     &lt;if test="countrycode != null"&gt;#{countrycode,javaType=java.lang.String},&lt;/if&gt;
     &lt;/trim&gt;
     </pre>
     * 这段代码可以注意对countryname的处理
     *
     * @param ms SQL配置
     * @return SQL
     */
    public String insert(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);

        //先处理主键, 添加必要的辅助节点
        processIndentity(columnList, sql, ms);

        sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        for (EntityColumn column : columnList) {
            if (!column.isInsertable()) {
                continue;
            }
            if (StringUtil.isNotEmpty(column.getSequenceName()) || column.isIdentity() || column.isUuid()) {
                sql.append(column.getColumn()).append(",");
            } else {
                sql.append(SqlHelper.getIfNotNull(column, column.getColumn() + ",", isNotEmpty()));
            }
        }
        sql.append("</trim>");
        sql.append("<trim prefix=\"VALUES(\" suffix=\")\" suffixOverrides=\",\">");
        for (EntityColumn column : columnList) {
            if (!column.isInsertable()) {
                continue;
            }
            //优先使用传入的属性值,当原属性property!=null时，用原属性
            //自增的情况下,如果默认有值,就会备份到property_cache中,所以这里需要先判断备份的值是否存在
            if (column.isIdentity()) {
                sql.append(SqlHelper.getIfCacheNotNull(column, column.getColumnHolder(null, "_cache", ",")));
            } else {
                //其他情况值仍然存在原property中
                sql.append(SqlHelper.getIfNotNull(column, column.getColumnHolder(null, null, ","), isNotEmpty()));
            }
            //当属性为null时，如果存在主键策略，会自动获取值，如果不存在，则使用null
            //序列的情况
            if (StringUtil.isNotEmpty(column.getSequenceName())) {
                sql.append(SqlHelper.getIfIsNull(column, getSeqNextVal(column) + " ,", isNotEmpty()));
            } else if (column.isIdentity()) {
                sql.append(SqlHelper.getIfCacheIsNull(column, column.getColumnHolder() + ","));
            } else if (column.isUuid()) {
                sql.append(SqlHelper.getIfIsNull(column, column.getColumnHolder(null, "_bind", ","), isNotEmpty()));
            }
        }
        sql.append("</trim>");
        return sql.toString();
    }


    /**
     * 插入全部, 包括Null值, 不使用数据库默认值, 这段代码比较复杂，这里举个例子
     * CountryU生成的insert方法结构如下：
     * <pre>
     &lt;bind name="countryname_bind" value='@java.util.UUID@randomUUID().toString().replace("-", "")'/&gt;
     INSERT INTO country_u(id,countryname,countrycode) VALUES
     &lt;trim prefix="(" suffix=")" suffixOverrides=","&gt;
     &lt;if test="id != null"&gt;#{id,javaType=java.lang.Integer},&lt;/if&gt;
     &lt;if test="id == null"&gt;#{id,javaType=java.lang.Integer},&lt;/if&gt;
     &lt;if test="countryname != null"&gt;#{countryname,javaType=java.lang.String},&lt;/if&gt;
     &lt;if test="countryname == null"&gt;#{countryname_bind,javaType=java.lang.String},&lt;/if&gt;
     &lt;if test="countrycode != null"&gt;#{countrycode,javaType=java.lang.String},&lt;/if&gt;
     &lt;if test="countrycode == null"&gt;#{countrycode,javaType=java.lang.String},&lt;/if&gt;
     &lt;/trim&gt;
     </pre>
     *
     * @param ms 原始SQL语句
     * @return 修正后的SQL语句
     */
    public String insertWithNull(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);

        //先处理主键, 添加必要的辅助节点
        processIndentity(columnList, sql, ms);

        sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.insertColumns(entityClass, false, false, false));
        sql.append("<trim prefix=\"VALUES(\" suffix=\")\" suffixOverrides=\",\">");
        for (EntityColumn column : columnList) {
            if (!column.isInsertable()) {
                continue;
            }
            //优先使用传入的属性值,当原属性property!=null时，用原属性
            //自增的情况下,如果默认有值,就会备份到property_cache中,所以这里需要先判断备份的值是否存在
            if (column.isIdentity()) {
                sql.append(SqlHelper.getIfCacheNotNull(column, column.getColumnHolder(null, "_cache", ",")));
            } else {
                //其他情况值仍然存在原property中
                sql.append(SqlHelper.getIfNotNull(column, column.getColumnHolder(null, null, ","), isNotEmpty()));
            }
            //当属性为null时，如果存在主键策略，会自动获取值，如果不存在，则使用null
            //序列的情况
            if (StringUtil.isNotEmpty(column.getSequenceName())) {
                sql.append(SqlHelper.getIfIsNull(column, getSeqNextVal(column) + " ,", false));
            } else if (column.isIdentity()) {
                sql.append(SqlHelper.getIfCacheIsNull(column, column.getColumnHolder() + ","));
            } else if (column.isUuid()) {
                sql.append(SqlHelper.getIfIsNull(column, column.getColumnHolder(null, "_bind", ","), isNotEmpty()));
            } else {
                //当null的时候，如果不指定jdbcType，oracle可能会报异常，指定VARCHAR不影响其他
                sql.append(SqlHelper.getIfIsNull(column, column.getColumnHolder(null, null, ","), isNotEmpty()));
            }
        }
        sql.append("</trim>");
        return sql.toString();
    }

    // 处理主键, 根据需要添加<bind />和<selectKey />节点
    private void processIndentity(Set<EntityColumn> columnList, StringBuilder sql, MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        //Identity列只能有一个
        boolean hasIdentityKey = false;

        for (EntityColumn column : columnList) {
            if (!column.isInsertable()) {
                continue;
            }
            if (StringUtil.isNotEmpty(column.getSequenceName())) {
            } else if (column.isIdentity()) {
                //这种情况下,如果原先的字段有值,需要先缓存起来,否则就一定会使用自动增长
                //这是一个bind节点
                sql.append(SqlHelper.getBindCache(column));
                //如果是Identity列，就需要插入selectKey
                //如果已经存在Identity列，抛出异常
                if (hasIdentityKey) {
                    //jdbc类型只需要添加一次
                    if (column.getGenerator() != null && column.getGenerator().equals("JDBC")) {
                        continue;
                    }
                    throw new RuntimeException(ms.getId() + "对应的实体类" + entityClass.getCanonicalName() + "中包含多个MySql的自动增长列,最多只能有一个!");
                }
                //插入selectKey
                newSelectKeyMappedStatement(ms, column);
                hasIdentityKey = true;
            } else if (column.isUuid()) {
                //uuid的情况，直接插入bind节点
                sql.append(SqlHelper.getBindValue(column, getUUID()));
            }
        }
    }


    /**
     * 通过主键删除
     *
     * @param ms 映射配置
     */
    public String deleteById(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        return SqlHelper.deleteFromTable(entityClass, tableName(entityClass)) +
                SqlHelper.wherePKColumns(entityClass);
    }

    /**
     * 通过主键更新全部字段
     *
     * @param ms 映射配置
     */
    public String updateWithNull(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.updateSetColumns(entityClass, null, false, false));
        sql.append(SqlHelper.wherePKColumns(entityClass));
        //System.out.println(sql.toString());
        return sql.toString();
    }

    /**
     * 通过主键更新不为null的字段
     *
     * @param ms 映射配置
     */
    public String update(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.updateSetColumns(entityClass, null, true, isNotEmpty()));
        sql.append(SqlHelper.wherePKColumns(entityClass));
        //System.out.println(sql.toString());
        return sql.toString();
    }

    /**
     * 根据主键进行查询
     *
     * @param ms
     */
    public String get(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        //将返回值修改为实体类型
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectAllColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.wherePKColumns(entityClass));
        return sql.toString();
    }

    /**
     * 根据Example查询总数
     *
     * @param ms
     * @return
     */
    public String selectCountByExample(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectCount(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.exampleWhereClause());
        return sql.toString();
    }

    /**
     * 根据Example删除
     *
     * @param ms
     * @return
     */
    public String deleteByExample(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.deleteFromTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.exampleWhereClause());
        return sql.toString();
    }


    /**
     * 根据Example查询
     *
     * @param ms
     * @return
     */
    public String selectByExample(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        //将返回值修改为实体类型
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append("<if test=\"distinct\">distinct</if>");
        //支持查询指定列
        sql.append(SqlHelper.exampleSelectColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.exampleWhereClause());
        sql.append(SqlHelper.exampleOrderBy(entityClass));
        return sql.toString();
    }

    /**
     * 根据Example查询
     *
     * @param ms
     * @return
     */
    public String selectByExampleAndRowBounds(MappedStatement ms) {
        return selectByExample(ms);
    }

    /**
     * 根据Example更新非null字段
     *
     * @param ms
     * @return
     */
    public String updateByExample(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass), "example"));
        sql.append(SqlHelper.updateSetColumns(entityClass, "record", true, isNotEmpty()));
        sql.append(SqlHelper.updateByExampleWhereClause());
        return sql.toString();
    }

    /**
     * 根据Example更新
     *
     * @param ms
     * @return
     */
    public String updateByExampleWithNull(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass), "example"));
        sql.append(SqlHelper.updateSetColumns(entityClass, "record", false, false));
        sql.append(SqlHelper.updateByExampleWhereClause());
        return sql.toString();
    }
}
