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
package cn.chenlichao.web.ssm.test.service;

import cn.chenlichao.web.ssm.service.impl.BaseServiceImpl;
import cn.chenlichao.web.ssm.test.domain.UcUser;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * <br>author: 陈立朝
 * <br>date: 16/6/12 下午5:54
 * <br>version: V1.0.0
 * <br>Copyright：Copyright © 2016 Chen Lichao. All rights reserved.
 */
@Service("userService")
@Primary
public class UcUserServiceImpl extends BaseServiceImpl<UcUser, Integer> implements UcUserService {
}
