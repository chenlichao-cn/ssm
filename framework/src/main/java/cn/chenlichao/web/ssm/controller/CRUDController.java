package cn.chenlichao.web.ssm.controller;

import cn.chenlichao.web.ssm.dao.entity.BaseEntity;

import java.io.Serializable;

/**
 * <br>author: 陈立朝
 * <br>date: 16/6/15 下午4:33
 * <br>version: V1.0.0
 * <br>Copyright：Copyright © 2016 Jlzx. All rights reserved.
 */

public abstract class CRUDController<E extends BaseEntity<PK>, PK extends Serializable> extends BaseController {
}
