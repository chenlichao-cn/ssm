package cn.chenlichao.web.ssm.test;

import cn.chenlichao.web.ssm.service.PageParams;
import cn.chenlichao.web.ssm.service.PageResults;
import cn.chenlichao.web.ssm.test.domain.UcUser;
import cn.chenlichao.web.ssm.test.service.UcUserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

/**
 * 基础服务接口单元测试
 *
 * <br>author: 陈立朝
 * <br>date: 16/6/12 下午5:53
 * <br>version: V1.0.0
 * <br>Copyright：Copyright © 2016 Chen Lichao. All rights reserved.
 */
@ContextConfiguration("classpath:spring/spring-config.xml")
@Sql({"/sql/init.sql", "/sql/add-records.sql"})
@RunWith(SpringJUnit4ClassRunner.class)
public class BaseServiceTest {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    private UcUserService service;

    @Test
    public void save() throws Exception {
        UcUser user = null;
        try {
            service.save(user);
            fail("未抛出非法参数异常");
        } catch (IllegalArgumentException e) {
            // ok
        }
        user = generatorUser();
        int id = service.save(user);
        assertEquals(42, id);
        assertEquals(42, countOfTable());
    }

    @Test
    public void saveCollection() throws Exception {
        List<UcUser> userList = new ArrayList<>(10);
        for (int i=0; i<10; i++) {
            UcUser user = generatorUser();
            user.setUsername("123456");
            userList.add(user);
        }
        int result = service.save(userList);
        assertEquals(10, result);
        assertEquals(51, countOfTable());
        assertEquals(10, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "uc_user", "username='123456'"));
    }

    @Test
    public void saveWithNull() throws Exception {

    }

    @Test
    public void saveCollectionWithNull() throws Exception {

    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void updateWithNull() throws Exception {

    }

    @Test
    public void updateByExample() throws Exception {

    }

    @Test
    public void updateByExampleWithNull() throws Exception {

    }

    @Test
    public void delete() throws Exception {

    }

    @Test
    public void delete1() throws Exception {

    }

    @Test
    public void delete2() throws Exception {

    }

    @Test
    public void deleteByExample() throws Exception {

    }

    @Test
    public void get() throws Exception {

    }

    @Test
    public void select() throws Exception {

    }

    @Test
    public void selectByExample() throws Exception {

    }

    @Test
    public void selectAll() throws Exception {

    }

    @Test
    public void selectCount() throws Exception {

    }

    @Test
    public void selectCountByExample() throws Exception {

    }

    @Test
    public void queryPage() throws Exception {
        PageParams<UcUser> pp = new PageParams<>();
        pp.setPageSize(10);
        PageResults<UcUser> results = service.queryPage(pp);
        assertEquals("全部记录", 41, results.getTotalCount());
        assertEquals("全部记录, 页数", 5, results.getPageCount());
        Assert.assertEquals("全部记录, 默认降序", 41, results.getResults().get(0).getId().intValue());

        pp.setParamEntity(new UcUser());
        pp.getParamEntity().setSex((short)1);
        results = service.queryPage(pp);
        assertEquals("部分记录", 32, results.getTotalCount());
        assertEquals("部分记录, 页数", 4, results.getPageCount());
        Assert.assertEquals("部分记录, 默认降序", 41, results.getResults().get(0).getId().intValue());

        pp.setPageIndex(2);
        results = service.queryPage(pp);
        assertEquals("部分记录", 32, results.getTotalCount());
        assertEquals("部分记录, 页数", 4, results.getPageCount());
        Assert.assertEquals("部分记录, 默认降序, 第二页", 31, results.getResults().get(0).getId().intValue());

        pp.setPageIndex(1);
        pp.getParamEntity().setSex((short)0);
        pp.setOrderBy("id");
        pp.setAsc(true);
        results = service.queryPage(pp);
        assertEquals("部分记录", 9, results.getTotalCount());
        assertEquals("部分记录, 页数", 1, results.getPageCount());
        Assert.assertEquals("部分记录, 升序", 11, results.getResults().get(0).getId().intValue());

    }

    @Test
    public void queryPageByExample() throws Exception {

    }

    @SuppressWarnings("Duplicates")
    private UcUser generatorUser() {
        UcUser user = new UcUser();
        user.setBirthday(new Date());
        user.setCreateTime(new Date());
        user.setEmail("test@email.com");
        user.setPassword("pass");
        user.setRealName("陈立朝");
        user.setSex((short) 1);
        user.setUsername("chenlichao");
        user.setUserType(UserType.NORMAL.getValue());
        user.setIsAdmin(false);
        return user;
    }

    private int countOfTable(String tableName) {
        return countRowsInTable(jdbcTemplate, tableName);
    }

    private int countOfTable() {
        return countOfTable("uc_user");
    }
}