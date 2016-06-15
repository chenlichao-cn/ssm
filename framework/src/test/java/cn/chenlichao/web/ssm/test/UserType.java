package cn.chenlichao.web.ssm.test;

/**
 * Created by chenlichao on 15/7/13.
 */
public enum UserType {
    NORMAL(1,"普通用户"),
    ADMIN(2, "管理员");

    private final short value;

    private final String descs;

    UserType(int value, String descs) {
        this.value = (short)value;
        this.descs = descs;
    }
    public short getValue() {
        return this.value;
    }

    public String getDescs() {
        return this.descs;
    }
}
