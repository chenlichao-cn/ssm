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

import java.util.Collections;
import java.util.List;

/**
 * 分页结果集
 *
 * <br>author: 陈立朝
 * <br>date: 16/6/12 上午10:09
 * <br>version: V1.0.0
 * <br>Copyright：Copyright © 2016 Chen Lichao. All rights reserved.
 */
public class PageResults<E extends BaseEntity> {

    /** 当前页数据结果集 */
    private final List<E> results;
    /** 数据总数 */
    private final int totalCount;
    /** 分页参数 */
    private final PageParams<E> pageParams;

    public PageResults(int totalCount, List<E> results, PageParams<E> pageParams) {
        this.totalCount = totalCount;
        this.results = Collections.unmodifiableList(results);
        this.pageParams = pageParams;
    }

    /**
     * 获取记录总数
     *
     * @return 记录总数
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * 获取当前页结果集
     *
     * @return 当前页结果集
     */
    public List<E> getResults() {
        return results;
    }

    /**
     * 获取分页参数
     *
     * @return 分页参数
     */
    public PageParams<E> getPageParams() {
        return pageParams;
    }

    /**
     * 获取分页参数简写
     *
     * @return 分页参数
     * @see #getPageParams()
     */
    public PageParams<E> getPp() {
        return pageParams;
    }

    /**
     * 获取总页数
     *
     * @return 总页数
     */
    public int getPageCount() {
        return (totalCount + pageParams.getPageSize() - 1) / pageParams.getPageSize();
    }

    @Override
    public String toString() {
        return "PageResults{" +
                "results=" + results +
                ", totalCount=" + totalCount +
                ", pageParams=" + pageParams +
                '}';
    }
}
