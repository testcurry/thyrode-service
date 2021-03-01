set names utf8mb4;
create database if not exists thyrodedb character set 'utf8mb4' collate 'utf8mb4_cs_0900_as_cs';
create user 'thyrodeUser'@'%' identified by 'thyrodeUser123!';
grant all privileges on thyrodedb.* to 'thyrodeUser'@'%' with grant option ;

#account servcie需要的表
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

#manager service需要的表
create table thyrodedb.t_case(
                               `caseId` bigint auto_increment primary key comment '用例系统ID',
                               `caseName` varchar(50) not null unique comment '用例名称',
                               `description` varchar(200) comment '用例描述',
                               `url` varchar(200) not null comment 'url地址',
                               `method` varchar(6) not null comment '请求方法',
                               `body` text comment 'body内容',
                               `createTime` datetime comment '用例创建时间',
                               `updateTime` datetime comment '用例修改时间'
) comment '用例信息表';

create table thyrodedb.t_header(
                                 `headerId` bigint auto_increment primary key comment '请求头系统ID',
                                 `name` varchar(50) comment 'header名称',
                                 `value` varchar(200) comment 'header值',
                                 `caseId` bigint not null comment '关联的caseId'
) comment '请求头信息';

create table thyrodedb.t_assertion(
                                    `assertionId` bigint auto_increment primary key comment 'assertion系统ID',
                                    `actual` varchar(50) comment '实际值',
                                    `op` varchar(10) comment '断言操作',
                                    `expected` varchar(50) comment '期望值',
                                    `caseId` bigint not null comment '关联的caseId'
) comment '断言信息表';

create table thyrodedb.t_project(
                                  `projectId` bigint auto_increment primary key comment '项目系统ID',
                                  `projectName` varchar(50) not null unique comment '项目名称',
                                  `description` varchar(200) comment '项目描述',
                                  `status` int comment '项目状态，0-Ready, 1-Pass, 2-Fail, 3-Running',
                                  `createTime` datetime comment '创建时间',
                                  `updateTime` datetime comment '修改时间'
) comment '项目信息表';

create table tyloodb.t_suite(
                                `suiteId` bigint auto_increment primary key comment '案例集系统ID',
                                `projectId` bigint comment '项目ID',
                                `caseId` bigint comment '用例ID',
                                `status` int comment '执行状态，0-Initial，1-Pass，2-Fail，3-Block',
                                `duration` bigint comment '执行时间',
                                `lastRun` datetime comment '最近运行时间'
) comment '案例集信息表';

#engine servcie需要的表t_exec_result
create table tyloodb.t_exec_result(
                                      `execId` bigint auto_increment primary key comment '执行记录系统ID',
                                      `projectId` bigint comment '项目ID',
                                      `caseId` bigint comment '用例ID',
                                      `caseName` varchar(50) comment '用例名称',
                                      `requestHeaders` text comment '请求头',
                                      `url` varchar(200) comment 'url地址',
                                      `method` varchar(6) comment '请求方法',
                                      `requestBody` text comment '请求的body',
                                      `responseHeaders` text comment '响应头',
                                      `responseBody` text comment '响应的body内容',
                                      `status` varchar(10) comment '执行结果',
                                      `message` text comment '执行日志',
                                      `duration` bigint comment '执行时间（ms）',
                                      `startTime` DATETIME comment '开始执行时间'
) comment '用例执行记录表';
