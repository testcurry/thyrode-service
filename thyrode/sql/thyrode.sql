set names utf8mb4;
create database if not exists thyrodedb character set 'utf8mb4' collate 'utf8mb4_cs_0900_as_cs';
create user 'thyrodeUser'@'%' identified by 'thyrodeUser123!';
grant all privileges on thyrodedb.* to 'thyrodeUser'@'%' with grant option ;
create table if not exists thyrodedb.t_account(
    `accountId` bigint  primary key auto_increment comment '账号系统ID',
    `accountName` varchar(20) not null comment '账号名称',
    `email` varchar(50) not null comment '账户邮箱',
    `salt` char(6) comment '密码摘要安全后缀',
    `password` char(64) comment 'SHA256摘要后的密码',
    `createTime` datetime comment '系统账户创建时间',
    `lastLoginTime` datetime comment '账户最近登录时间'
    ) comment '账户信息表';
insert into thyrodedb.t_account values
(null,'桃花先生','curry@vip.com','aH1p05',sha2(concat('curry','aH1p05'),256),now(),null);