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
package cn.chenlichao.web.ssm.test;

/**
 * <br>author: 陈立朝
 * <br>date: 16/6/14 上午9:32
 * <br>version: V1.0.0
 * <br>Copyright：Copyright © 2016 Chen Lichao. All rights reserved.
 */
public class Main {

    public static void main(String[] args) {
        String name = "system/user/view";
        String templateName = name.substring(0, name.lastIndexOf("/") + 1) + "template";
        for(;;) {
            System.out.println(templateName);
            if (templateName.lastIndexOf("/") == -1) {
                break;
            }
            templateName = templateName.substring(0, templateName.lastIndexOf("/"));
            templateName = templateName.substring(0, templateName.lastIndexOf("/") + 1) + "template";

        }
    }
}
