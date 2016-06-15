DROP TABLE IF EXISTS uc_user;
CREATE TABLE `uc_user` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '序列号',
`email` varchar(45) NOT NULL COMMENT '邮箱',
`username` varchar(45) NOT NULL COMMENT '用户名',
`user_type` TINYINT not null COMMENT '用户类型',
`password` varchar(45) DEFAULT '123456' COMMENT '密码',
`real_name` varchar(45) COMMENT '真实姓名',
`sex` smallint(6) NOT NULL DEFAULT '0' COMMENT '性别:0=未知，1=男，2=女',
`birthday` date DEFAULT NULL COMMENT '生日日期',
`create_time` datetime NOT NULL COMMENT '注册时间',
  is_admin BIT(1) COMMENT '是否是管理员',
PRIMARY KEY (`id`)
);
INSERT INTO uc_user (`email`,`username`,`user_type`,`password`,`real_name`,sex,birthday,create_time,is_admin) VALUES ('email@email.com','chenlichao',1,'pass','陈立朝',1,'1985-01-08',CURRENT_TIME,0);