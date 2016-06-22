package cn.chenlichao.web.ssm.controller;

import cn.chenlichao.web.ssm.service.PageParams;
import cn.chenlichao.web.ssm.service.PageResults;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * MVC - Model封装
 *
 * <br>author: 陈立朝
 * <br>date: 16/6/16 上午11:05
 * <br>version: V1.0.0
 * <br>Copyright：Copyright © 2016 Jlzx. All rights reserved.
 */
public class WrapModel<T> {

    /** 单一实体对象 */
    private T entity;

    /** 分页查询结果 */
    private PageResults<T> pageResults;

    /** 分页查询条件 */
    private PageParams<T> pageParams;

    private Map<String, Object> attributes;

    private String viewName;

    public WrapModel() {
        pageParams = new PageParams<>();
        pageResults = new PageResults<>(0, Collections.<T>emptyList(), pageParams);
        attributes = new HashMap<>();
    }

    public T getEntity() {
        return entity;
    }

    /**
     * 获取单一对象简写
     *
     * @return 单一实体对象
     */
    public T getE() {
        return entity;
    }

    public WrapModel<T> setEntity(T entity) {
        this.entity = entity;
        return this;
    }

    public PageResults<T> getPageResults() {
        return pageResults;
    }

    /**
     * 获取分页查询结果集简写
     *
     * @return 分页查询结果集
     */
    public PageResults<T> getPr() {
        return pageResults;
    }

    public WrapModel<T> setPageResults(PageResults<T> pageResults) {
        this.pageResults = pageResults;
        this.pageParams = pageResults.getPageParams();
        return this;
    }

    public PageParams<T> getPageParams() {
        return pageParams;
    }

    /**
     * 获取分页查询参数简写
     *
     * @return 分页查询参数
     */
    public PageParams<T> getPp() {
        return pageParams;
    }

    public WrapModel<T> setPageParams(PageParams<T> pageParams) {
        this.pageParams = pageParams;
        return this;
    }

    /**
     * 添加自定义属性
     *
     * @param name 属性名称
     * @param value 属性值
     */
    public void putAttribute(String name, Object value) {
        this.attributes.put(name, value);
    }

    /**
     * 获取自定义属性集合
     *
     * @return 自定义属性集合
     */
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * 获取自定义属性集合的简写
     *
     * @return 自定义属性集合
     */
    public Map<String, Object> getAttrs() {
        return attributes;
    }

    public String getViewName() {
        return viewName;
    }

    public WrapModel setViewName(String viewName) {
        this.viewName = viewName;
        return this;
    }
}
