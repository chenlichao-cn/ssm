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
package cn.chenlichao.web.ssm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller基类
 *
 * <br>author: 陈立朝
 * <br>date: 16/6/12 下午7:59
 * <br>version: V1.0.0
 * <br>Copyright：Copyright © 2016 Chen Lichao. All rights reserved.
 */
public abstract class BaseController {

    /** 成功消息键 */
    public static final String SUCCESS_MESSAGE_KEY = "SSM.ACTION.SUCCESS";
    /** 警告消息键 */
    public static final String WARNING_MESSAGE_KEY = "SSM.ACTION.WARNING";
    /** 错误消息键 */
    public static final String ERROR_MESSAGE_KEY = "SSM.ACTION.ERROR";

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private ThreadLocal<HttpServletRequest> localRequest = new ThreadLocal<>();

    private ThreadLocal<HttpServletResponse> localResponse = new ThreadLocal<>();

    @ModelAttribute
    public void populateAttributes(HttpServletRequest request, HttpServletResponse response) {
        localRequest.set(request);
        localResponse.set(response);
    }

    /**
     * 添加全局成功信息
     *
     * @param message 信息内容
     */
    public void addActionSuccess(String message) {
        addActionMessage(SUCCESS_MESSAGE_KEY, message);
    }

    /**
     * 添加全局警告信息
     *
     * @param message 警告内容
     */
    public void addActionWarning(String message) {
        addActionMessage(WARNING_MESSAGE_KEY, message);
    }

    /**
     * 添加全局错误信息
     *
     * @param message 错误内容
     */
    public void addActionError(String message) {
        addActionMessage(ERROR_MESSAGE_KEY, message);
    }

    private void addActionMessage(String key, String message) {
        localRequest.get().setAttribute(key, message);
    }
}
