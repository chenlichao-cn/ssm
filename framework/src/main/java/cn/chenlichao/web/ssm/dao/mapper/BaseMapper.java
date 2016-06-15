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
package cn.chenlichao.web.ssm.dao.mapper;

import cn.chenlichao.web.ssm.dao.entity.BaseEntity;
import cn.chenlichao.web.ssm.support.mybatis.providers.SSMMapperProvider;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.base.delete.DeleteMapper;
import tk.mybatis.mapper.common.base.select.SelectAllMapper;
import tk.mybatis.mapper.common.base.select.SelectCountMapper;
import tk.mybatis.mapper.common.base.select.SelectMapper;
import tk.mybatis.mapper.common.example.DeleteByExampleMapper;
import tk.mybatis.mapper.common.example.SelectByExampleMapper;
import tk.mybatis.mapper.common.example.SelectCountByExampleMapper;

import java.io.Serializable;

/**
 * 通用Mapper基础方法定义接口, 其他的mapper接口需继承自此接口, 方能获得通用的方法
 *
 * <br>author: 陈立朝
 * <br>date: 16/5/9 下午3:41
 * <br>version: V1.0.0
 */
public interface BaseMapper<E extends BaseEntity<PK>, PK extends Serializable> extends
        DeleteMapper<E>, DeleteByExampleMapper<E>, SelectAllMapper<E>,
        SelectCountMapper<E>, SelectMapper<E>, SelectCountByExampleMapper<E>,
        SelectByExampleMapper<E> {

    /**
     * 将一个实体插入数据库, null的属性不会插入, 将使用数据库的默认值
     * @param record 数据实体
     * @return 成功返回1, 失败返回0
     */
    @InsertProvider(type = SSMMapperProvider.class, method="dynamicSQL")
    int insert(E record);

    /**
     * 将一个实体插入数据库, null的属性也会插入, 不使用数据库的默认值
     * @param record 数据实体
     * @return 成功返回1, 失败返回0
     */
    @InsertProvider(type = SSMMapperProvider.class, method="dynamicSQL")
    int insertWithNull(E record);

    /**
     * 根据主键字段进行删除，方法参数必须包含完整的主键属性
     *
     * @param id 实体ID
     * @return 成功返回1, 失败返回0
     */
    @DeleteProvider(type = SSMMapperProvider.class, method = "dynamicSQL")
    int deleteById(PK id);

    /**
     * 根据主键进行更新, 不更新null值
     *
     * @param record 实体对象
     * @return 成功返回1, 失败返回0
     */
    @UpdateProvider(type = SSMMapperProvider.class, method = "dynamicSQL")
    int update(E record);

    /**
     * 根据主键进行更新, null值也将更新到数据库
     *
     * @param record 实体对象
     * @return 成功返回1, 失败返回0
     */
    @UpdateProvider(type = SSMMapperProvider.class, method = "dynamicSQL")
    int updateWithNull(E record);

    /**
     * 根据example给定条件, 更新record中不为null的属性
     *
     * @param record 实体对象, 提供待更新值
     * @param example 限定条件
     * @return 数据库中更新的记录数
     */
    @UpdateProvider(type = SSMMapperProvider.class, method = "dynamicSQL")
    int updateByExample(@Param("record") E record, @Param("example") Object example);

    /**
     * 根据example给定条件, 更新record中的全部属性, null也将更新到数据库中
     *
     * @param record 实体对象, 提供待更新值
     * @param example 限定条件
     * @return 数据库中更新的记录数
     */
    @UpdateProvider(type = SSMMapperProvider.class, method = "dynamicSQL")
    int updateByExampleWithNull(@Param("record") E record, @Param("example") Object example);

    /**
     * 根据主键ID, 获取唯一记录
     *
     * @param id 主键ID
     * @return ID为指定ID的记录, 找不到时返回null
     */
    @SelectProvider(type = SSMMapperProvider.class, method = "dynamicSQL")
    E get(PK id);

}
