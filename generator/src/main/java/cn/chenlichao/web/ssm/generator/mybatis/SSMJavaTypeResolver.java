package cn.chenlichao.web.ssm.generator.mybatis;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.JavaTypeResolver;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * <br>author: 陈立朝
 * <br>date: 16/6/16 上午9:01
 * <br>version: V1.0.0
 * <br>Copyright：Copyright © 2016 Jlzx. All rights reserved.
 */
public class SSMJavaTypeResolver extends JavaTypeResolverDefaultImpl {

    protected Map<String, JdbcTypeInformation> typeNameMap;

    public SSMJavaTypeResolver() {
        super();

    }
}
