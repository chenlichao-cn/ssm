package cn.chenlichao.web.ssm.controller;

import cn.chenlichao.web.ssm.service.PageParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;

import static cn.chenlichao.web.ssm.service.PageParams.*;

/**
 * 分页参数处理工具类
 *
 * <br>author: 陈立朝
 * <br>date: 16/6/16 下午1:22
 * <br>version: V1.0.0
 * <br>Copyright：Copyright © 2016 Jlzx. All rights reserved.
 */
public class PageUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageUtils.class);

    private PageUtils() {
        // 禁止外部实例化
    }

    /**
     * 处理分页参数
     *
     * @param request 请求对象
     * @param pp 分页参数持有对象
     */
    public static void appendPageParams(HttpServletRequest request, PageParams<?> pp) {
        String pageIndexString = request.getParameter(PAGE_INDEX_KEY);
        if (StringUtils.hasText(pageIndexString)) {
            try {
                int pageIndex = NumberUtils.parseNumber(pageIndexString, Integer.class);
                if (pageIndex < 1) {
                    LOGGER.warn("传入的页码小于 1 , 进行修正: [{}] -> [{}]", pageIndex, DEFAULT_PAGE_INDEX);
                    pageIndex = 1;
                }
                pp.setPageIndex(pageIndex);
            } catch (IllegalArgumentException e) {
                LOGGER.warn("处理页码时出错, 传入的页码不是合法数字: [{}]", pageIndexString);
            }
        }
        String pageSizeString = request.getParameter(PAGE_SIZE_KEY);
        if (StringUtils.hasText(pageSizeString)) {
            try {
                int pageSize = NumberUtils.parseNumber(pageSizeString, Integer.class);
                if (pageSize < 1 || pageSize > 100) {
                    LOGGER.warn("传入的页大小不在合理范围, 进行修正: [{}] -> [{}]", pageSize, DEFAULT_PAGE_SIZE);
                }
                pp.setPageSize(pageSize);
            } catch (IllegalArgumentException e) {
                LOGGER.warn("处理页大小时出错, 传入的页大小不是合法数字: [{}]", pageIndexString);
            }
        }
    }

    /**
     * 处理分页参数, 应对部分自定义MyBatis查询
     *
     * @param request 请求对象
     * @param paramMap 分页参数持有对象
     */
    public static void appendPageParams(HttpServletRequest request, Map<String, Object> paramMap) {
        int pageIndex = DEFAULT_PAGE_INDEX;
        int pageSize = DEFAULT_PAGE_SIZE;
        String pageIndexString = request.getParameter(PAGE_INDEX_KEY);
        if (StringUtils.hasText(pageIndexString)) {
            try {
                pageIndex = NumberUtils.parseNumber(pageIndexString, Integer.class);
                if (pageIndex < 1) {
                    LOGGER.warn("传入的页码小于 1 , 进行修正: [{}] -> [{}]", pageIndex, DEFAULT_PAGE_INDEX);
                    pageIndex = DEFAULT_PAGE_INDEX;
                }
            } catch (IllegalArgumentException e) {
                LOGGER.warn("处理页码时出错, 传入的页码不是合法数字: [{}]", pageIndexString);
            }
        }
        String pageSizeString = request.getParameter(PAGE_SIZE_KEY);
        if (StringUtils.hasText(pageSizeString)) {
            try {
                pageSize = NumberUtils.parseNumber(pageSizeString, Integer.class);
                if (pageSize < 1 || pageSize > 100) {
                    LOGGER.warn("传入的页大小不在合理范围, 进行修正: [{}] -> [{}]", pageSize, DEFAULT_PAGE_SIZE);
                    pageSize = DEFAULT_PAGE_SIZE;
                }
            } catch (IllegalArgumentException e) {
                LOGGER.warn("处理页大小时出错, 传入的页大小不是合法数字: [{}]", pageIndexString);
            }
        }
        paramMap.put(PAGE_INDEX_KEY, pageIndex);
        paramMap.put(PAGE_SIZE_KEY, pageSize);
    }
}
