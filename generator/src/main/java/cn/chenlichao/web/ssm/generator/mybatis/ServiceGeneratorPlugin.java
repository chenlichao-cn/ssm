package cn.chenlichao.web.ssm.generator.mybatis;

import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.StringUtility;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * <br>author: 陈立朝
 * <br>date: 16/6/15 下午1:33
 * <br>version: V1.0.0
 * <br>Copyright：Copyright © 2016 Jlzx. All rights reserved.
 */
public class ServiceGeneratorPlugin extends PluginAdapter {

    private String targetPackage;

    private String targetProject;

    private boolean enableSubPackages = false;

    @SuppressWarnings("Duplicates")
    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        String tPackage = properties.getProperty("targetPackage");
        if (StringUtility.stringHasValue(tPackage)) {
            this.targetPackage = tPackage;
        } else {
            throw new RuntimeException("ServiceGeneratorPlugin缺少targetPackage属性");
        }
        String tProject = properties.getProperty("targetProject");
        if (StringUtility.stringHasValue(tProject)) {
            this.targetProject = tProject;
        } else {
            throw new RuntimeException("ServiceGeneratorPlugin缺少targetProject属性");
        }
        String enableSubP = properties.getProperty("enableSubPackages");
        if (StringUtility.isTrue(enableSubP)) {
            this.enableSubPackages = true;
        }
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {

        // 获取entity name和PrimaryKey type
        FullyQualifiedTable fullyQualifiedTable = introspectedTable.getFullyQualifiedTable();
        String entityName = fullyQualifiedTable.getDomainObjectName();
        String entityType = introspectedTable.getBaseRecordType();
        List<IntrospectedColumn> keyColumns = introspectedTable.getPrimaryKeyColumns();
        if (keyColumns == null || keyColumns.size() != 1) {
            throw new RuntimeException("只支持拥有唯一主键的表, 且不能为联合主键");
        }
        String pkClassName = keyColumns.get(0).getFullyQualifiedJavaType().getShortName();

        // 构造服务接口和实现类的全限定类名
        String subPackage = fullyQualifiedTable.getSubPackage(enableSubPackages);
        StringBuilder interfaceNameBuilder = new StringBuilder(targetPackage);
        StringBuilder implClassNameBuilder = new StringBuilder(targetPackage);
        if (StringUtility.stringHasValue(subPackage)) {
            interfaceNameBuilder.append(".").append(subPackage);
            implClassNameBuilder.append(".").append(subPackage);
        }
        interfaceNameBuilder.append(".").append(entityName).append("Service");
        implClassNameBuilder.append(".impl.").append(entityName).append("ServiceImpl");
        String interfaceName = interfaceNameBuilder.toString();
        String implClassName = implClassNameBuilder.toString();

        FullyQualifiedJavaType serviceInterfaceType = new FullyQualifiedJavaType(interfaceName);
        introspectedTable.setAttribute("serviceInterfaceType", serviceInterfaceType);
        FullyQualifiedJavaType serviceImplType = new FullyQualifiedJavaType(implClassName);

        // 构造服务接口
        Interface serviceInterface = new Interface(serviceInterfaceType);
        serviceInterface.setVisibility(JavaVisibility.PUBLIC);
        serviceInterface.addSuperInterface(new FullyQualifiedJavaType("BaseService<" + entityName + ", " + pkClassName + ">"));
        serviceInterface.addImportedType(new FullyQualifiedJavaType("cn.chenlichao.web.ssm.service.BaseService"));
        serviceInterface.addImportedType(new FullyQualifiedJavaType(entityType));

        // 构造服务实现类
        TopLevelClass serviceImpl = new TopLevelClass(serviceImplType);
        serviceImpl.setVisibility(JavaVisibility.PUBLIC);
        serviceImpl.setSuperClass("BaseServiceImpl<" + entityName + ", " + pkClassName + ">");
        serviceImpl.addImportedType("cn.chenlichao.web.ssm.service.impl.BaseServiceImpl");
        serviceImpl.addImportedType(entityType);
        serviceImpl.addImportedType(serviceInterfaceType);
        serviceImpl.addImportedType("org.springframework.context.annotation.Primary");
        serviceImpl.addImportedType("org.springframework.stereotype.Service");
        serviceImpl.addAnnotation("@Primary");
        serviceImpl.addAnnotation("@Service(\"" + entityName.substring(0,1).toLowerCase()
                + entityName.substring(1) + "Service\")");
        serviceImpl.addSuperInterface(serviceInterfaceType);

        // 服务实现类, 引入mapper
        FullyQualifiedJavaType mapperType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
        String mapperTypeName = mapperType.getShortName();
        serviceImpl.addImportedType(mapperType);
        Field mapperField = new Field();
        mapperField.setType(mapperType);
        mapperField.setVisibility(JavaVisibility.PRIVATE);
        mapperField.setName(mapperTypeName.substring(0,1).toLowerCase() + mapperTypeName.substring(1));
        mapperField.addAnnotation("@Autowired");
        serviceImpl.addField(mapperField);
        serviceImpl.addImportedType("org.springframework.beans.factory.annotation.Autowired");

        List<GeneratedJavaFile> result = new ArrayList<>();
        result.add(new GeneratedJavaFile(serviceInterface,
                targetProject,
                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                context.getJavaFormatter()));
        result.add(new GeneratedJavaFile(serviceImpl,
                targetProject,
                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                context.getJavaFormatter()));

        return result;
    }
}
