package cn.chenlichao.web.ssm.test;

import cn.chenlichao.web.ssm.test.domain.UcUser;
import cn.chenlichao.web.ssm.test.mapper.UcUserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import tk.mybatis.mapper.entity.Example;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

/**
 * 通用Mapper单元测试
 *
 * <br>author: 陈立朝
 * <br>date: 16/6/12 下午2:29
 * <br>version: V1.0.0
 * <br>Copyright：Copyright © 2016 Chen Lichao. All rights reserved.
 */
@ContextConfiguration("classpath:spring/spring-config.xml")
@Sql({"/sql/init.sql", "/sql/add-records.sql"})
@RunWith(SpringJUnit4ClassRunner.class)
public class BaseMapperTest {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private UcUserMapper mapper;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    public void insert() throws Exception {
        UcUser user = generatorUser();
        int r = mapper.insert(user);
        assertEquals("数据插入返回值", 1, r);
        assertNotNull("数据插入返回ID非空", user.getId());
        assertEquals("数据插入返回ID", 42, user.getId().intValue());
        //jdbcTemplate.execute("ALTER TABLE UC_USER AUTO_INCREMENT=200");
        user.setId(null);
        r = mapper.insert(user);
        assertEquals("第二次数据插入返回值", 1, r);
        assertNotNull("第二次数据插入返回ID非空", user.getId());
        assertEquals("第二次数据插入返回ID", 43, user.getId().intValue());
    }

    @Test
    public void insertWithNull() throws Exception {
        UcUser user = generatorUser();
        user.setPassword(null);
        mapper.insertWithNull(user);
        jdbcTemplate.query("select password from uc_user where id=" + user.getId(), new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                String passwd = resultSet.getString(1);
                assertNull("NULL值覆盖数据库失败", passwd);
            }
        });
        user.setId(null);
        user.setUsername(null);
        try {
            mapper.insertWithNull(user);
            fail("未抛出空值异常");
        } catch (Exception e) {
            // ok
        }
    }

    @Test
    public void deleteById() throws Exception {
        mapper.deleteById(10);
        assertEquals(40, countOfTable("uc_user"));
        mapper.deleteById(20);
        mapper.deleteById(21);
        mapper.deleteById(22);
        mapper.deleteById(23);
        mapper.deleteById(24);
        assertEquals(35, countOfTable("uc_user"));
    }

    @Test
    public void delete() throws Exception {
        UcUser user = new UcUser();
        user.setId(3);
        mapper.delete(user);
        assertEquals(40, countOfTable("uc_user"));
        user.setId(null);
        user.setSex((short)0);
        mapper.delete(user);
        assertEquals(31, countOfTable("uc_user"));
    }

    @Test
    public void deleteByExample() throws Exception{
        Example example = new Example(UcUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", 3);
        int result = mapper.deleteByExample(example);
        assertEquals(1, result);
        assertEquals(40, countOfTable("uc_user"));
        criteria.andEqualTo("sex", 0);
        result = mapper.deleteByExample(example);
        assertEquals(0, result);
        assertEquals(40, countOfTable("uc_user"));

        //删除多记录
        example = new Example(UcUser.class);
        example.createCriteria().andEqualTo("sex", 0);
        result = mapper.deleteByExample(example);
        assertEquals("多记录删除", 9, result);
        assertEquals("多记录删除", 31, countOfTable("uc_user"));
    }

    @Test
    public void update() throws Exception {
        UcUser user = new UcUser();
        user.setId(10000);
        user.setUsername("123456");
        int result = mapper.update(user);
        assertEquals(0, result);
        user.setId(10);
        result = mapper.update(user);
        assertEquals(1, result);
        jdbcTemplate.query("select * from uc_user where id=10", new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                assertEquals("123456", resultSet.getString("username"));
            }
        });
    }

    @Test
    public void updateWithNull() throws Exception {
        UcUser user = generatorUser();
        user.setId(10000);
        user.setPassword(null);
        user.setUsername("123456");
        int result = mapper.updateWithNull(user);
        assertEquals(0, result);
        user.setId(10);
        result = mapper.updateWithNull(user);
        assertEquals(1, result);
        jdbcTemplate.query("select * from uc_user where id=10", new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                assertEquals("123456", resultSet.getString("username"));
                assertNull(resultSet.getString("password"));
            }
        });
    }

    @Test
    public void updateByExample() throws Exception {
        UcUser user = new UcUser();
        user.setUsername("123456");

        // 无记录
        Example example = new Example(UcUser.class);
        example.createCriteria().andEqualTo("id", 10000);
        int result = mapper.updateByExample(user, example);
        assertEquals("无对应记录", 0, result);

        // 单记录
        example = new Example(UcUser.class);
        example.createCriteria().andEqualTo("id", 10);
        result = mapper.updateByExample(user, example);
        assertEquals(1, result);
        assertEquals("但记录更新", 1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "uc_user", "username='123456'"));

        //多记录
        example = new Example(UcUser.class);
        example.createCriteria().andEqualTo("sex", 0);
        result = mapper.updateByExample(user, example);
        assertEquals(9, result);
        assertEquals("多记录更新", 10, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "uc_user", "username='123456'"));
    }

    @Test
    public void updateByExampleWithNull() throws Exception {
        UcUser user = generatorUser();
        user.setUsername("123456");
        user.setPassword(null);

        // 无记录
        Example example = new Example(UcUser.class);
        example.createCriteria().andEqualTo("id", 10000);
        int result = mapper.updateByExampleWithNull(user, example);
        assertEquals("无对应记录", 0, result);

        // 单记录
        example = new Example(UcUser.class);
        example.createCriteria().andEqualTo("id", 10);
        result = mapper.updateByExampleWithNull(user, example);
        assertEquals(1, result);
        assertEquals("单记录更新", 1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "uc_user", "username='123456'"));
        assertEquals("但记录null值", 1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "uc_user", "password is null"));

        //多记录
        example = new Example(UcUser.class);
        example.createCriteria().andEqualTo("sex", 0);
        result = mapper.updateByExampleWithNull(user, example);
        assertEquals(9, result);
        assertEquals("多记录更新", 10, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "uc_user", "username='123456'"));
        assertEquals("但记录null值", 10, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "uc_user", "password is null"));
    }

    @Test
    public void get() throws Exception {
        UcUser user = mapper.get(10000);
        assertNull(user);

        user = mapper.get(10);
        assertNotNull(user);
        assertEquals(10, user.getId().intValue());
    }

    @Test
    public void selectAll() throws Exception {
        List<UcUser> results = mapper.selectAll();
        assertNotNull(results);
        assertEquals(41, results.size());
        assertEquals("默认降序", 41, results.get(0).getId().intValue());
    }

    @Test
    public void selectCount() throws Exception {
        UcUser user = new UcUser();
        assertEquals("全部记录", 41, mapper.selectCount(user));

        user.setId(10);
        assertEquals("单一记录", 1, mapper.selectCount(user));

        user.setId(null);
        user.setSex((short)0);
        assertEquals("多记录", 9, mapper.selectCount(user));

        assertEquals("null参数", 41, mapper.selectCount(null));

        user.setId(10000);
        assertEquals("0记录", 0, mapper.selectCount(user));
    }

    @Test
    public void selectCountByExample() throws Exception {
        assertEquals("null参数", 41, mapper.selectCountByExample(null));

        Example example = new Example(UcUser.class);
        assertEquals("全部记录", 41, mapper.selectCountByExample(example));

        example.createCriteria().andEqualTo("id", 10);
        assertEquals("单一记录", 1, mapper.selectCountByExample(example));

        example.clear();
        example.createCriteria().andEqualTo("sex", 0);
        assertEquals("多条记录", 9, mapper.selectCountByExample(example));

        example.clear();
        example.createCriteria().andEqualTo("id", 10000);
        assertEquals("0记录", 0, mapper.selectCountByExample(example));
    }

    @Test
    public void select() throws Exception {
        List<UcUser> results = mapper.select(null);
        assertNotNull("null参数", results);
        assertEquals("null参数", 41, results.size());

        UcUser user = new UcUser();
        results = mapper.select(user);
        assertNotNull("全部记录", results);
        assertEquals("全部记录", 41, results.size());

        user.setId(10);
        results = mapper.select(user);
        assertNotNull("单一记录", results);
        assertEquals("单一记录", 1, results.size());

        user.setId(null);
        user.setSex((short)0);
        results = mapper.select(user);
        assertNotNull("多记录", results);
        assertEquals("多记录", 9, results.size());

        user.setId(10000);
        results = mapper.select(user);
        assertNotNull("0记录", results);
        assertEquals("0记录", 0, results.size());
    }

    @Test
    public void selectByExample() throws Exception {
        List<UcUser> results = mapper.selectByExample(null);
        assertNotNull("null参数", results);
        assertEquals("null参数", 41, results.size());

        Example example = new Example(UcUser.class);
        results = mapper.selectByExample(example);
        assertNotNull("全部记录", results);
        assertEquals("全部记录", 41, results.size());

        example.createCriteria().andEqualTo("id", 10);
        results = mapper.selectByExample(example);
        assertNotNull("单一记录", results);
        assertEquals("单一记录", 1, results.size());

        example.clear();
        example.createCriteria().andEqualTo("sex", 0);
        results = mapper.selectByExample(example);
        assertNotNull("多记录", results);
        assertEquals("多记录", 9, results.size());

        example.clear();
        example.createCriteria().andEqualTo("id", 10000);
        results = mapper.selectByExample(example);
        assertNotNull("0记录", results);
        assertEquals("0记录", 0, results.size());
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

}