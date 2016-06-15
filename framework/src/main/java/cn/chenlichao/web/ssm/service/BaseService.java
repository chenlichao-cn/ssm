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
package cn.chenlichao.web.ssm.service;

import cn.chenlichao.web.ssm.dao.entity.BaseEntity;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 基础服务接口
 *
 * <br>author: 陈立朝
 * <br>date: 16/6/12 上午9:24
 * <br>version: V1.0.0
 * <br>Copyright：Copyright © 2016 Chen Lichao. All rights reserved.
 */
public interface BaseService<E extends BaseEntity<PK>, PK extends Serializable> {

    /**
     * 保存对象实体, 忽略null值
     *
     * @param entity 对象实体
     * @return 保存后的唯一主键
     */
    PK save(E entity);

    /**
     * 批量保存对象实体, 忽略null值
     *
     * @param entities 对象实体集合
     * @return 保存成功数
     */
    int save(Collection<E> entities);

    /**
     * 保存对象实体, null值将一同入库
     *
     * @param entity 对象实体
     * @return 保存后的唯一主键
     */
    PK saveWithNull(E entity);

    /**
     * 批量保存对象实体, null值将一同入库
     *
     * @param entities 对象实体集合
     * @return 保存成功数
     */
    int saveWithNull(Collection<E> entities);

    /**
     * 根据实体主键值进行更新, 只更新非NULL值
     *
     * @param entity 主键不为空的实体对象
     * @return 0: 未找到主键对应的数据; 1: 成功更新;
     */
    int update(E entity);

    /**
     * 根据实体主键值进行更新, 实体中的null值会覆盖数据库数据
     *
     * @param entity 主键不为空的实体对象
     * @return 0: 未找到主键对应的数据; 1: 成功更新;
     */
    int updateWithNull(E entity);

    /**
     * 根据查询条件更新数据库, 只更新非null值
     *
     * @param example 查询条件
     * @param entity 实体数据, 只更新非null值
     * @return 更新的记录数
     */
    int updateByExample(Example example, E entity);

    /**
     * 根据查询条件更新数据库, 实体中的null值将覆盖数据库数据
     *
     * @param example 查询条件
     * @param entity 实体数据, null值覆盖数据库内容
     * @return 更新的记录数
     */
    int updateByExampleWithNull(Example example, E entity);

    /**
     * 删除指定主键的数据
     *
     * @param id 主键值
     * @return 0: 未找到主键对应的数据, 1: 删除成功
     */
    int delete(PK id);

    /**
     * 批量删除数据
     *
     * @param ids 主键值列表
     * @return 删除成功数
     */
    int delete(Collection<PK> ids);

    /**
     * 根据传入的条件实体删除数据, 忽略null值
     *
     * @param entity 条件实体
     * @return 删除成功数
     */
    int delete(E entity);

    /**
     * 删除满足查询条件的数据
     *
     * @param example 查询条件
     * @return 删除成功数
     */
    int deleteByExample(Example example);

    /**
     * 获取指定主键值的数据
     *
     * @param id 主键值
     * @return 主键对应的数据实体, 找不到数据返回null
     */
    E get(PK id);

    /**
     * 查询满足条件实体的数据
     *
     * @param entity 条件实体
     * @return 满足条件的数据集合, 找不到时返回空集合
     */
    List<E> select(E entity);

    /**
     * 查询满足条件的数据
     *
     * @param example 查询条件
     * @return 满足条件的数据集合, 找不到时返回空结合
     */
    List<E> selectByExample(Example example);

    /**
     * 获取全部记录
     *
     * @return 全部记录集合
     */
    List<E> selectAll();

    /**
     * 统计满足实体条件的记录数
     *
     * @return 满足条件的记录数
     */
    int selectCount(E entity);

    /**
     * 统计满足条件的记录数
     *
     * @param example 查询条件
     * @return 满足条件的记录数
     */
    int selectCountByExample(Example example);

    /**
     * 分页查询
     *
     * @param pageParams 分页查询参数
     * @return 分页结果集
     */
    PageResults<E> queryPage(PageParams<E> pageParams);

    /**
     * 根据example条件分页查询
     *
     * @param pageParams 分页参数
     * @param example 查询条件
     * @return 分页结果集
     */
    PageResults<E> queryPageByExample(PageParams<E> pageParams, Example example);

}
