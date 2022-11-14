package com.goblincwl.cwlweb.common.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.goblincwl.cwlweb.common.utils.DateUtils;
import com.goblincwl.cwlweb.common.utils.ServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 * 基础 Controller
 *
 * @author ☪wl
 * @date 2021-05-05 0:43
 */
public class BaseController<T> {

    protected final Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 创建分页对象
     *
     * @return 分页对象
     * @date 2021-05-05 04:39:24
     * @author ☪wl
     */
    protected Page<T> createPage() {
        return new Page<>(
                ServletUtils.getParameterToInt("pageNumber", 1),
                ServletUtils.getParameterToInt("pageSize", 10)
        );
    }

    /**
     * 创建QueryWrapper对象
     *
     * @param t 实体类对象
     * @return QueryWrapper对象
     * @date 2021-05-05 04:39:36
     * @author ☪wl
     */
    protected QueryWrapper<T> createQueryWrapper(T t) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>(t);
        String sortName = ServletUtils.getParameter("sortName");
        String sortOrder = ServletUtils.getParameter("sortOrder");
        if (sortName != null && sortOrder != null) {
            queryWrapper.orderBy(true, "asc".equals(sortOrder), sortName);
        }
        return queryWrapper;
    }
}
