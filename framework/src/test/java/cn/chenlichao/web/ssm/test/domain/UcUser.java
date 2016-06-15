package cn.chenlichao.web.ssm.test.domain;

import cn.chenlichao.web.ssm.dao.entity.IntegerEntity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "uc_user")
public class UcUser extends IntegerEntity {
    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户类型
     */
    @Column(name = "user_type")
    private Short userType;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * 性别:0=未知，1=男，2=女
     */
    private Short sex;

    /**
     * 生日日期
     */
    private Date birthday;

    /**
     * 注册时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 是否是管理员
     */
    @Column(name = "is_admin")
    private Boolean isAdmin;

    /**
     * 获取邮箱
     *
     * @return email - 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取用户名
     *
     * @return username - 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public Short getUserType() {
        return userType;
    }

    public void setUserType(Short userType) {
        this.userType = userType;
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取真实姓名
     *
     * @return real_name - 真实姓名
     */
    public String getRealName() {
        return realName;
    }

    /**
     * 设置真实姓名
     *
     * @param realName 真实姓名
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * 获取性别:0=未知，1=男，2=女
     *
     * @return sex - 性别:0=未知，1=男，2=女
     */
    public Short getSex() {
        return sex;
    }

    /**
     * 设置性别:0=未知，1=男，2=女
     *
     * @param sex 性别:0=未知，1=男，2=女
     */
    public void setSex(Short sex) {
        this.sex = sex;
    }

    /**
     * 获取生日日期
     *
     * @return birthday - 生日日期
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * 设置生日日期
     *
     * @param birthday 生日日期
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * 获取注册时间
     *
     * @return create_time - 注册时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置注册时间
     *
     * @param createTime 注册时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取是否是管理员
     *
     * @return is_admin - 是否是管理员
     */
    public Boolean getIsAdmin() {
        return isAdmin;
    }

    /**
     * 设置是否是管理员
     *
     * @param isAdmin 是否是管理员
     */
    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}