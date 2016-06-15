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
package cn.chenlichao.web.ssm.dao.entity;

import java.io.Serializable;

/**
 * 实体类顶级接口
 *
 * <br>author: 陈立朝
 * <br>date: 16/5/9 下午3:25
 * <br>version: V1.0.0
 */
public interface BaseEntity<PK extends Serializable> {

    /**
     * 获取ID属性值
     * @return ID属性值
     */
    PK getId();

    /**
     * 设置ID属性值
     * @param id 属性值
     */
    void setId(PK id);
}
