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

/**
 * 分页查询条件
 *
 * <br>author: 陈立朝
 * <br>date: 16/6/12 上午10:19
 * <br>version: V1.0.0
 * <br>Copyright：Copyright © 2016 Chen Lichao. All rights reserved.
 */
public class PageParams<E> {

    /** 页码 key */
    public static final String PAGE_INDEX_KEY = "pageIndex";
    /** 页大小 key */
    public static final String PAGE_SIZE_KEY = "pageSize";
    /** 排序字段 */
    public static final String ORDER_BY_KEY = "orderBy";
    /** 是否升序 */
    public static final String IS_ASC_KEY = "isAsc";
    /** 默认页码 */
    public static final int DEFAULT_PAGE_INDEX = 1;
    /** 默认页大小 */
    public static final int DEFAULT_PAGE_SIZE = 30;

    /** 查询参数 */
    private E paramEntity;
    /** 当前页码, 默认为第 1 页 */
    private int pageIndex = DEFAULT_PAGE_INDEX;
    /** 页大小, 默认为每页 30 条 */
    private int pageSize = DEFAULT_PAGE_SIZE;
    /** 排序字段 */
    private String orderBy = "id";
    /** 是否升序排序, 默认为降序排序 */
    private boolean asc = false;

    /**
     * 获取查询条件实体
     *
     * @return 条件实体
     */
    public E getParamEntity() {
        return paramEntity;
    }

    /**
     * 获取查询条件实体简写
     *
     * @return 条件实体
     * @see #getParamEntity()
     */
    public E getPe() {
        return paramEntity;
    }

    /**
     * 设定查询条件实体
     * @param paramEntity 条件实体
     */
    public void setParamEntity(E paramEntity) {
        this.paramEntity = paramEntity;
    }

    /**
     * 获取当前页码
     *
     * @return 当前页码
     */
    public int getPageIndex() {
        return pageIndex;
    }

    /**
     * 设置当前页码
     *
     * @param pageIndex 当前页码
     */
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * 获取页大小
     *
     * @return 页大小
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置页大小
     *
     * @param pageSize 页大小
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 获取排序字段
     *
     * @return 排序字段名称
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * 设置排序字段, 支持多字段排序, 以半角逗号(,)分隔
     *
     * @param orderBy 排序字段
     */
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * 是否升序排序, 默认为降序
     *
     * @return true: 升序, false: 降序
     */
    public boolean isAsc() {
        return asc;
    }

    /**
     * 设置是否升序排序
     * @param asc true: 升序排序, false: 降序排序
     */
    public void setAsc(boolean asc) {
        this.asc = asc;
    }
}
