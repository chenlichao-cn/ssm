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
package cn.chenlichao.web.ssm.support.tiles;

import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;
import org.apache.tiles.definition.DefinitionsReader;
import org.apache.tiles.definition.dao.BaseLocaleUrlDefinitionDAO;
import org.apache.tiles.definition.dao.CachingLocaleUrlDefinitionDAO;
import org.apache.tiles.definition.dao.ResolvingLocaleUrlDefinitionDAO;
import org.apache.tiles.definition.digester.DigesterDefinitionsReader;
import org.apache.tiles.el.ELAttributeEvaluator;
import org.apache.tiles.el.ScopeELResolver;
import org.apache.tiles.el.TilesContextBeanELResolver;
import org.apache.tiles.el.TilesContextELResolver;
import org.apache.tiles.evaluator.AttributeEvaluator;
import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
import org.apache.tiles.evaluator.BasicAttributeEvaluatorFactory;
import org.apache.tiles.evaluator.impl.DirectAttributeEvaluator;
import org.apache.tiles.factory.AbstractTilesContainerFactory;
import org.apache.tiles.factory.BasicTilesContainerFactory;
import org.apache.tiles.impl.mgmt.CachingTilesContainer;
import org.apache.tiles.locale.LocaleResolver;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.ApplicationResource;
import org.apache.tiles.startup.DefaultTilesInitializer;
import org.apache.tiles.startup.TilesInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.tiles3.SpringLocaleResolver;
import org.springframework.web.servlet.view.tiles3.SpringWildcardServletTilesApplicationContext;

import javax.el.*;
import javax.servlet.ServletContext;
import javax.servlet.jsp.JspFactory;
import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于Spring框架的 Tiles 3.x 配置类。查看<a href="http://tiles.apache.org">http://tiles.apache.org</a>获取Tiles的更多信息。
 * Tiles是一个模板布局框架, 可以与JSP和其他模板引擎配合使用。
 *
 * <p>Tiles3Configurer加载tiles definition配置文件列表, 配置一个TilesContainer,
 * 供{@link org.springframework.web.servlet.view.tiles3.TilesView}调用。
 * 同时, 对于未在tiles definition配置文件中配置的definition name, 将根据约定, 自动构建{@link Definition}.</p>
 *
 * <p>一个常用的tiles definition配置示例: </p>
 *
 * <pre class="code">
 * &lt;bean id="tilesConfigurer" class="cn.chenlichao.web.Tiles3Configurer">
 *     &lt;property name="definitions">
 *         &lt;list>
 *             &lt;value>/WEB-INF/defs/templates.xml&lt;/value>
 *             &lt;value>/WEB-INF/defs/templates_en_US.xml&lt;/value>
 *         &lt;/list>
 *     &lt;/property>
 *     &lt;property name="pagePrefix" value="/WEB-INF/jsp/" />
 * &lt;/bean>
 * </pre>
 *
 * <p>definitions属性的值, 就是tiles definition配置文件, 如果不指定definitions属性, 默认加载{@code "/WEB-INF/tiles.xml"}</p>
 *
 * <p>pagePrefix属性, 用于自动构建Definition查找页面。 自动构建Definition时, 根据URI路径, 到对应文件夹下查找相同名字的页面,
 * 若找到则构建Definition。
 * 例如: pagePrefix="/WEB-INF/jsp/", 请求的URI为/system/user/view, 则会去/WEB-INF/jsp/system/user文件夹下查找名为view.jsp的页面。
 * 找到后构建Defination, 同时在tiles definition配置文件中依次查找名为 system/user/template, system/template, template 的
 * 模板定义, 找到后作为心构建Definition的父定义,
 * 并覆盖父定义中的部分定义, 如{@code <put-attribute name="body" value="/WEB-INF/jsp/system/user/view.jsp" />}
 * </p>
 *
 *
 * <br>author: 陈立朝
 * <br>date: 16/6/13 下午5:17
 * <br>version: V1.0.0
 * <br>Copyright：Copyright © 2016 Chen Lichao. All rights reserved.
 * @see org.springframework.web.servlet.view.tiles3.TilesView
 * @see org.springframework.web.servlet.view.tiles3.TilesViewResolver
 */
public class Tiles3Configurer implements ServletContextAware, InitializingBean, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(Tiles3Configurer.class);

    private static final boolean tilesElPresent =
            ClassUtils.isPresent("org.apache.tiles.el.ELAttributeEvaluator", Tiles3Configurer.class.getClassLoader());

    private TilesInitializer tilesInitializer;

    private String[] definitions;

    private String pagePrefix;

    private boolean checkRefresh = false;

    private boolean validateDefinitions = true;

    private boolean useMutableTilesContainer = false;

    private ServletContext servletContext;

    public void setDefinitions(String[] definitions) {
        this.definitions = definitions;
    }

    public void setPagePrefix(String pagePrefix) {
        this.pagePrefix = pagePrefix;
        if (!pagePrefix.endsWith("/")) {
            this.pagePrefix += "/";
        }
    }

    public void setCheckRefresh(boolean checkRefresh) {
        this.checkRefresh = checkRefresh;
    }

    public void setValidateDefinitions(boolean validateDefinitions) {
        this.validateDefinitions = validateDefinitions;
    }

    public void setUseMutableTilesContainer(boolean useMutableTilesContainer) {
        this.useMutableTilesContainer = useMutableTilesContainer;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void afterPropertiesSet() throws TilesException {
        ApplicationContext preliminaryContext = new SpringWildcardServletTilesApplicationContext(this.servletContext);
        this.tilesInitializer = new SSMTilesInitializr();
        this.tilesInitializer.initialize(preliminaryContext);
    }

    @Override
    public void destroy() throws Exception {
        this.tilesInitializer.destroy();
    }

    private class SSMTilesInitializr extends DefaultTilesInitializer {
        @Override
        protected AbstractTilesContainerFactory createContainerFactory(ApplicationContext context) {
            return new SSMTilesContainerFactory();
        }
    }

    private class SSMTilesContainerFactory extends BasicTilesContainerFactory {

        @Override
        public TilesContainer createContainer(ApplicationContext applicationContext) {
            TilesContainer container = super.createContainer(applicationContext);
            return (useMutableTilesContainer ? new CachingTilesContainer(container) : container);
        }

        @Override
        protected List<ApplicationResource> getSources(ApplicationContext applicationContext) {
            if (definitions != null) {
                List<ApplicationResource> result = new LinkedList<>();
                for (String definition : definitions) {
                    Collection<ApplicationResource> resources = applicationContext.getResources(definition);
                    if (resources != null) {
                        result.addAll(resources);
                    }
                }
                return result;
            }
            else {
                return super.getSources(applicationContext);
            }
        }

        @Override
        protected BaseLocaleUrlDefinitionDAO instantiateLocaleDefinitionDao(ApplicationContext applicationContext,
                                                                            LocaleResolver resolver) {
            BaseLocaleUrlDefinitionDAO dao = new SSMDefinitionDAO(applicationContext);
            if (checkRefresh) {
                ((CachingLocaleUrlDefinitionDAO) dao).setCheckRefresh(true);
            }
            return dao;
        }

        @Override
        protected DefinitionsReader createDefinitionsReader(ApplicationContext context) {
            DigesterDefinitionsReader reader = (DigesterDefinitionsReader) super.createDefinitionsReader(context);
            reader.setValidating(validateDefinitions);
            return reader;
        }

        @Override
        protected LocaleResolver createLocaleResolver(ApplicationContext context) {
            return new SpringLocaleResolver();
        }

        @Override
        protected AttributeEvaluatorFactory createAttributeEvaluatorFactory(ApplicationContext context,
                                                                            LocaleResolver resolver) {
            AttributeEvaluator evaluator;
            if (tilesElPresent && JspFactory.getDefaultFactory() != null) {
                evaluator = new Tiles3Configurer.TilesElActivator().createEvaluator();
            }
            else {
                evaluator = new DirectAttributeEvaluator();
            }
            return new BasicAttributeEvaluatorFactory(evaluator);
        }
    }

    private class TilesElActivator {

        public AttributeEvaluator createEvaluator() {
            ELAttributeEvaluator evaluator = new ELAttributeEvaluator();
            evaluator.setExpressionFactory(
                    JspFactory.getDefaultFactory().getJspApplicationContext(servletContext).getExpressionFactory());
            evaluator.setResolver(new Tiles3Configurer.CompositeELResolverImpl());
            return evaluator;
        }
    }

    private static class CompositeELResolverImpl extends CompositeELResolver {

        public CompositeELResolverImpl() {
            add(new ScopeELResolver());
            add(new TilesContextELResolver(new TilesContextBeanELResolver()));
            add(new TilesContextBeanELResolver());
            add(new ArrayELResolver(false));
            add(new ListELResolver(false));
            add(new MapELResolver(false));
            add(new ResourceBundleELResolver());
            add(new BeanELResolver(false));
        }
    }

    private class SSMDefinitionDAO extends ResolvingLocaleUrlDefinitionDAO {

        private Map<Locale, Map<String, Definition>> autoDefinitionMap = new ConcurrentHashMap<>();

        SSMDefinitionDAO(ApplicationContext applicationContext) {
            super(applicationContext);
        }

        @Override
        public Definition getDefinition(String name, Locale customizationKey) {
            Definition retVal = super.getDefinition(name, customizationKey);

            if (retVal == null) {
                if (customizationKey == null) {
                    customizationKey = Locale.ROOT;
                }
                Map<String, Definition> definitions = autoDefinitionMap.get(customizationKey);
                if (definitions == null) {
                    definitions = new ConcurrentHashMap<>();
                    autoDefinitionMap.put(customizationKey, definitions);
                }
                retVal = definitions.get(name);
                if (retVal == null) {
                    retVal = buildDefinition(name, customizationKey);
                    if (retVal != null) {
                        definitions.put(name, retVal);
                    }
                }
            }
            return retVal;
        }

        private Definition buildDefinition(String name, Locale customizationKey) {
            LOGGER.info("构建Definition [{}] ... ", name);
            String webPath = servletContext.getRealPath("/");
            if(webPath.endsWith(File.separator)) {
                webPath = webPath.substring(0, webPath.length() - 1);
            }
            String viewPage = webPath + pagePrefix + name + ".jsp";
            if (!(new File(viewPage).exists())) {
                LOGGER.warn("找不到Definition对应的文件 [{}] 不存在!!!", viewPage);
                return null;
            }

            Definition definition = new Definition();
            definition.setName(name);
            definition.putAttribute("body", Attribute.createTemplateAttribute(pagePrefix + name + ".jsp"));

            String templateName = name.substring(0, name.lastIndexOf("/") + 1) + "template";
            Definition parent = super.getDefinition(templateName, customizationKey);
            while(parent == null) {
                if (templateName.lastIndexOf("/") == -1) {
                    LOGGER.warn("找不到layout模板!");
                    return null;
                }
                templateName = templateName.substring(0, templateName.lastIndexOf("/"));
                templateName = templateName.substring(0, templateName.lastIndexOf("/") + 1) + "template";
                parent = super.getDefinition(templateName, customizationKey);
            }
            definition.setExtends(templateName);
            definition.inherit(parent);
            return definition;
        }
    }
}
