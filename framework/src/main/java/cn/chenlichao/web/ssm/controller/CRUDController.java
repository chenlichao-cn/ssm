package cn.chenlichao.web.ssm.controller;

import cn.chenlichao.web.ssm.dao.entity.BaseEntity;
import cn.chenlichao.web.ssm.service.BaseService;
import cn.chenlichao.web.ssm.service.PageParams;
import cn.chenlichao.web.ssm.service.PageResults;
import cn.chenlichao.web.ssm.utils.DateUtils;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;

/**
 * 单表增删改查基础Controller
 *
 * <br>author: 陈立朝
 * <br>date: 16/6/15 下午4:33
 * <br>version: V1.0.0
 * <br>Copyright：Copyright © 2016 Jlzx. All rights reserved.
 */

@SuppressWarnings({"WeakerAccess", "UnusedParameters", "unused"})
public abstract class CRUDController<E extends BaseEntity<PK>, PK extends Serializable> extends BaseController
        implements InitializingBean {

    protected static final String MODEL_ATTR = "model";

    private Class<E> entityClass;
    private Class<PK> pkClass;

    /** 通用服务, 根据泛型自动选择注入 */
    @Autowired
    private BaseService<E, PK> baseService;

    @SuppressWarnings("unchecked")
    public CRUDController() {
        // 返回此 Class 所表示的实体 (类, 接口, 基本类型或void) 的直接超类的 Type
        Type genType = getClass().getGenericSuperclass();

        // 逐级向上查找, 直到找到对应的泛型类型, 用于处理多级继承
        while(!genType.equals(Object.class)) {
            if (genType instanceof ParameterizedType) {
                ParameterizedType superType = (ParameterizedType) genType;
                Type[] params = superType.getActualTypeArguments();
                if (params.length == 2 && params[0] instanceof Class) {
                    entityClass = (Class<E>) params[0];
                    pkClass = (Class<PK>) params[1];
                    break;
                }
            }
            if (genType instanceof Class) {
                genType = ((Class) genType).getGenericSuperclass();
            } else {
                break;
            }
        }
        LOGGER.debug("Entity Class: {}, PK class: {}", entityClass.getSimpleName(), pkClass.getSimpleName());
    }

    /**
     * 处理方法调用前的准备工作
     *
     * @param request HTTP请求
     * @return 数据对象
     */
    @ModelAttribute
    public final WrapModel<E> prepareAction(HttpServletRequest request) {
        //解析路径
        String contextPath = request.getContextPath();
        String uri = request.getRequestURI();
        String requestPrefix = uri.substring(contextPath.length() + 1);
        String actionName = requestPrefix;
        if (requestPrefix.contains("/")) {
            actionName = requestPrefix.substring(requestPrefix.lastIndexOf("/") + 1);
            requestPrefix = requestPrefix.substring(0, requestPrefix.lastIndexOf("/"));
        }
        if (actionName.contains(".")) { // 去掉.do等后缀
            actionName = actionName.substring(0, actionName.lastIndexOf('.'));
        }
        LOGGER.debug("收到请求: [{}], requestPath=[{}], action=[{}]", uri, requestPrefix, actionName);

        // 构建展示模型
        WrapModel<E> model = new WrapModel<>();
        model.setViewName(requestPrefix + "/" + actionName);
        model.getPp().setParamEntity(composeEntity(request, Action.LIST));

        request.setAttribute(MODEL_ATTR, model);
        return model;
    }

    /**
     * 响应列表请求
     *
     * @param request HTTP请求对象
     * @param model 数据对象
     *
     * @return view name
     */
    @RequestMapping("/list.do")
    public String list(HttpServletRequest request, @ModelAttribute WrapModel<E> model) throws Exception {

        // 获取参数
        PageParams<E> pp = model.getPp();
        E params = pp.getParamEntity();
        if (params != null) {
            populateParams(params, "model.pp.pe.", request);
        }
        // 处理排序字段
        String orderBy = request.getParameter(PageParams.ORDER_BY_KEY);
        if (StringUtils.hasText(orderBy)) {
            pp.setOrderBy(orderBy);
        }
        String isAsc = request.getParameter(PageParams.IS_ASC_KEY);
        if (StringUtils.hasText(isAsc)) {
            pp.setAsc(Boolean.parseBoolean(isAsc));
        }
        // 处理分页参数
        PageUtils.appendPageParams(request, pp);

        // 执行查询
        try {
            beforeAction(Action.LIST, model);
            PageResults<E> pageResults = baseService.queryPage(pp);
            model.setPageResults(pageResults);
            postAction(Action.LIST, model);
        } catch (Exception e) {
            addExceptionError(e);
            throw e;
        }

        return model.getViewName();
    }

    /**
     * 处理请求前的预处理
     *
     * @param action 请求类型
     * @param model 数据对象
     */
    protected void beforeAction(Action action, WrapModel<E> model) {
        //override as need
    }

    /**
     * 处理请求后的再加工
     *
     * @param action 请求类型
     * @param model 数据对象
     */
    protected void postAction(Action action, WrapModel<E> model) {
        //override as need
    }

    @SuppressWarnings({"unchecked", "Duplicates"})
    @RequestMapping("/view.do")
    protected String view(HttpServletRequest request, @ModelAttribute WrapModel<E> model) {
        Action action = Action.VIEW;

        try {
            PK id = getIdFromRequest(request);

            beforeAction(action, model);
            E entity = baseService.get(id);
            if (entity == null) {
                throw new ResourceNotFoundException("找不到指定的记录。");
            }
            model.setEntity(entity);
            postAction(action, model);
        } catch (Exception e) {
            addExceptionError(e);
            throw e;
        }
        return model.getViewName();
    }

    @SuppressWarnings("Duplicates")
    @RequestMapping("/edit.do")
    protected String edit(HttpServletRequest request, @ModelAttribute WrapModel<E> model) {
        Action action = Action.EDIT;

        try {
            PK id = getIdFromRequest(request);

            beforeAction(action, model);
            E entity = baseService.get(id);
            if (entity == null) {
                throw new ResourceNotFoundException("找不到指定的记录。");
            }
            model.setEntity(entity);
            postAction(action, model);
        } catch (Exception e) {
            addExceptionError(e);
            throw e;
        }
        return model.getViewName();
    }

    @RequestMapping("/create.do")
    protected String create(HttpServletRequest request, @ModelAttribute WrapModel<E> model) {
        Action action = Action.CREATE;
        try {
            E entity = composeEntity(request, action);
            model.setEntity(entity);
        } catch (Exception e) {
            addExceptionError(e);
            throw e;
        }
        return model.getViewName();
    }

    @RequestMapping("/save.do")
    protected String save(HttpServletRequest request, @ModelAttribute WrapModel<E> model) {
        Action action = Action.SAVE;
        PK id;
        try {
            E entity = composeEntity(request, action);
            if (entity == null) {
                throw new IllegalArgumentException("无法构建Entity实体对象");
            }
            populateParams(entity, "model.entity.", request);
            model.setEntity(entity);
            beforeAction(action, model);
            id = baseService.save(entity);
            if (id == null) {
                addActionError("保存失败");
            }
            postAction(action, model);
            request.setAttribute("id", id);
        } catch (Exception e) {
            addExceptionError(e);
            throw e;
        }

        addActionSuccess("保存成功!");
        String viewName = model.getViewName();
        String viewUrl = request.getContextPath() + "/"
                + viewName.substring(0, viewName.lastIndexOf("/") + 1)
                + Action.VIEW.getName();
        return "redirect:view.do?id=" + id;
    }

    @RequestMapping("/update.do")
    protected String update(HttpServletRequest request, @ModelAttribute WrapModel<E> model) {
        Action action = Action.UPDATE;
        PK id;
        try {
            E entity = composeEntity(request, action);
            id = getIdFromRequest(request);
            if (entity == null) {
                throw new IllegalArgumentException("无法构建Entity实体对象");
            }
            entity.setId(id);
            populateParams(entity, "model.entity.", request);
            model.setEntity(entity);

            beforeAction(action, model);
            int result = baseService.update(entity);
            if (result == 0) {
                addActionWarning("修改操作未命中, 记录未发生变化, 记录可能已删除。");
            }
            postAction(action, model);
            request.setAttribute("id", id);
        } catch (Exception e) {
            addExceptionError(e);
            throw e;
        }
        addActionSuccess("更新成功!");
        String viewName = model.getViewName();
        String viewUrl = request.getContextPath() + "/"
                + viewName.substring(0, viewName.lastIndexOf("/") + 1)
                + Action.VIEW.getName();
        return "redirect:view.do?id=" + id;
    }

    @RequestMapping("/delete.do")
    protected String delete(HttpServletRequest request, WrapModel<E> model) {
        Action action = Action.DELETE;
        try {
            PK id = getIdFromRequest(request);

            beforeAction(action, model);
            int result = baseService.delete(id);
            if (result == 0) {
                addActionWarning("删除操作没有命中, 可能已经被删除");
            }
            postAction(action, model);
        } catch (Exception e) {
            addExceptionError(e);
            throw e;
        }
        addActionSuccess("删除成功!");
        String viewName = model.getViewName();
        String viewUrl = request.getContextPath() + "/"
                + viewName.substring(0, viewName.lastIndexOf("/") + 1)
                + Action.LIST.getName();
        return "redirect:" + Action.LIST.getName() + ".do";
    }

    @SuppressWarnings("unchecked")
    private PK getIdFromRequest(HttpServletRequest request) {
        String idString = request.getParameter("id");
        if (StringUtils.isEmpty(idString)) {
            throw new IllegalArgumentException("id参数为空!");
        }
        PK id;
        try {
            if (Integer.class == pkClass) {
                id = (PK) NumberUtils.parseNumber(idString, Integer.class);
            } else if (Long.class == pkClass) {
                id = (PK) NumberUtils.parseNumber(idString, Long.class);
            } else {
                throw new IllegalArgumentException("id参数不合法!");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("id参数不合法!");
        }
        return id;
    }

    /**
     * 构建单一实体对象, 主要用于查询条件、新建页面展示等默认值展示
     *
     * @param request 请求对象
     * @param action 请求类型
     * @return 单一实体对象
     */
    protected E composeEntity(HttpServletRequest request, Action action) {
        if (entityClass != null) {
            try {
                return entityClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                LOGGER.warn("创建实体[{}]失败！", entityClass.getName(), e);
            }
        }
        return null;
    }

    /**
     * 获取通用服务, 当泛型注入失败时, 会调用此方法, 子类可选择性重写
     *
     * @return Controller对应的服务类
     */
    protected BaseService<E, PK> getBaseService() {
        // override as need
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.baseService == null) {
            this.baseService = getBaseService();
        }
    }

    protected void populateParams(E entity, String prefix, HttpServletRequest request) {
        Assert.notNull(prefix, "前缀不能为空");

        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(entity);
        for (PropertyDescriptor pd : beanWrapper.getPropertyDescriptors()) {
            String pName = pd.getName();
            String pValue = request.getParameter(prefix + pName);
            if (StringUtils.hasText(pValue)) {
                Class<?> pType = pd.getPropertyType();
                if (Date.class == pType) {
                    try {
                        Date date = DateUtils.parse(pValue);
                        beanWrapper.setPropertyValue(pName, date);
                    } catch (ParseException e) {
                        LOGGER.warn("日期时间字符串不合法: [{}]", pValue);
                    }
                } else {
                    beanWrapper.setPropertyValue(pName, pValue);
                }
            }
        }
    }

    private void addExceptionError(Exception e) {
        String message = e.getMessage();
        if (StringUtils.isEmpty(message)) {
            message = e.getClass().getName();
        }
        addActionError("查询列表失败, 原因为: " + message);
    }

    protected enum Action {
        LIST("list"),
        VIEW("view"),
        EDIT("edit"),
        CREATE("create"),
        SAVE("save"),
        UPDATE("update"),
        DELETE("delete");

        private String name;

        Action(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static Action getAction(String name) {
            if (name == null) {
                return null;
            }
            switch (name) {
                case "list": return LIST;
                case "view": return VIEW;
                case "edit": return EDIT;
                case "create": return CREATE;
                case "save": return SAVE;
                case "update": return UPDATE;
                case "delete": return DELETE;
                default: return null;
            }
        }
    }
}
