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

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 使用Integer类型主键的实体类
 *
 * <br>author: 陈立朝
 * <br>date: 16/5/9 下午3:28
 * <br>version: V1.0.0
 */
public class IntegerEntity implements BaseEntity<Integer>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @OrderBy("DESC")
    private Integer id;

    /**
     * 去的ID属性值
     *
     * @return ID属性值
     */
    @Override
    public Integer getId() {
        return this.id;
    }

    /**
     * 设置ID属性值
     *
     * @param id 属性值
     */
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 默认的toString方法
     *
     * @return 实体对象的字符串表示法
     */
    @Override
    public String toString() {
        Class<?> clazz = getClass();
        String simpleName = clazz.getSimpleName();
        Method[] methods = clazz.getMethods();
        StringBuilder sb = new StringBuilder(simpleName).append("{");
        for (Method method : methods) {
            String name = method.getName();
            if (name.startsWith("get")) {
                String propertyName = name.substring(3,4).toLowerCase() + name.substring(4);
                String value = null;
                try {
                    Object valueObj = method.invoke(this);
                    if (valueObj != null) {
                        value = valueObj.toString();
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    // 忽略调用失败导致的异常
                }
                if (value != null) {
                    sb.append(propertyName).append("=").append(value).append(",");
                }
            }
        }
        // 删除最后一个逗号
        if (sb.charAt(sb.length() - 1) == ',') {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("}");
        return sb.toString();
    }
}
