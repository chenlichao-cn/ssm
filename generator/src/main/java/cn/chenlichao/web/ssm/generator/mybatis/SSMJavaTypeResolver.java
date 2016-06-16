package cn.chenlichao.web.ssm.generator.mybatis;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;
import org.mybatis.generator.internal.util.StringUtility;

import java.lang.reflect.Field;
import java.sql.Types;
import java.util.Properties;

/**
 * <br>author: 陈立朝
 * <br>date: 16/6/16 上午9:01
 * <br>version: V1.0.0
 * <br>Copyright：Copyright © 2016 Jlzx. All rights reserved.
 */
public class SSMJavaTypeResolver extends JavaTypeResolverDefaultImpl {
    @Override
    public void addConfigurationProperties(Properties properties) {
        super.addConfigurationProperties(properties);

        ClassLoader classLoader = getClass().getClassLoader();
        if (classLoader == null) {
            return;
        }
        Field[] types = Types.class.getDeclaredFields();
        for (Field type : types) {
            String name = type.getName();
            if (properties.containsKey(name)) {
                String targetType = properties.getProperty(name);
                if (StringUtility.stringHasValue(targetType)) {
                    try {
                        classLoader.loadClass(targetType);
                        typeMap.put(type.getInt(null), new JdbcTypeInformation(name, new FullyQualifiedJavaType(targetType)));
                    } catch (ClassNotFoundException e) {
                        warnings.add("目标JavaType类找不到");
                    } catch (IllegalAccessException e) {
                        warnings.add("设定类型映射失败: " + e.getMessage());
                    }
                }
            }
        }
    }
}
