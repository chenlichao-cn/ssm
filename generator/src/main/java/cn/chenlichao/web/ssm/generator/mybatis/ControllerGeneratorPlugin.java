package cn.chenlichao.web.ssm.generator.mybatis;

import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * <br>author: 陈立朝
 * <br>date: 16/6/15 下午1:33
 * <br>version: V1.0.0
 * <br>Copyright：Copyright © 2016 Jlzx. All rights reserved.
 */
public class ControllerGeneratorPlugin extends PluginAdapter {

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
        StringBuilder nameBuilder = new StringBuilder(targetPackage);
        if (StringUtility.stringHasValue(subPackage)) {
            nameBuilder.append(".").append(subPackage);
        }
        nameBuilder.append(".").append(entityName).append("Controller");
        String name = nameBuilder.toString();

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(name);

        // 构造服务实现类
        TopLevelClass controller = new TopLevelClass(type);
        controller.setVisibility(JavaVisibility.PUBLIC);
        controller.setSuperClass("CRUDController<" + entityName + ", " + pkClassName + ">");
        controller.addImportedType("cn.chenlichao.web.ssm.controller.CRUDController");
        controller.addImportedType(entityType);
        controller.addImportedType("org.springframework.stereotype.Controller");
        controller.addAnnotation("@Controller");

        Object serviceTypeObj = introspectedTable.getAttribute("serviceInterfaceType");
        if (serviceTypeObj != null && serviceTypeObj instanceof FullyQualifiedJavaType) {
            FullyQualifiedJavaType serviceType = (FullyQualifiedJavaType) serviceTypeObj;
            controller.addImportedType(serviceType);
            String serviceName = serviceType.getShortName();

            Field service = new Field();
            service.setVisibility(JavaVisibility.PRIVATE);
            service.setType(serviceType);
            service.setName(serviceName.substring(0, 1).toLowerCase() + serviceName.substring(1));
            service.addAnnotation("@Autowired");
            controller.addField(service);
            controller.addImportedType("org.springframework.beans.factory.annotation.Autowired");
        }

        List<GeneratedJavaFile> result = new ArrayList<>();
        result.add(new GeneratedJavaFile(controller,
                targetProject,
                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                context.getJavaFormatter()));

        return result;
    }
}
