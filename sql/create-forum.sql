create table `user` (
  `id` int(11) auto_increment comment '自增id',
  `uid` int(11) default '0' comment '用户id',
  `username` varchar(16) not null comment '账户名',
  `password` varchar(32) not null comment '密码',
  `type` varchar(16) not null comment '账户类型 role_user/role_admin',
  `gmt_create` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_update` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  primary key (`id`),
  unique key (`uid`),
  unique key (`username`)
) comment '用户表';

create table `article` (
  `id` int(11) auto_increment comment '自增ID',
  `title` varchar(128) default '' comment '文章名称',
  `uid` int(11) default '0' comment '作者id',
  `gmt_create` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_update` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  primary key (`id`)
) comment '文章表';

create table `follow_relation` (
  `id` int(11) auto_increment comment '自增ID',
  `fans_uid` int(11) not null comment '粉丝id',
  `followed_uid` int(11) not null comment '被关注者id',
  `gmt_create` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_update` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  primary key (`id`)
) comment '用户关注表';

create table `my_favorite` (
  `id` int(11) auto_increment comment '自增ID',
  `uid` int(11) not null comment '用户id',
  `aid` int(11) not null comment '文章id',
  `gmt_create` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_update` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  primary key (`id`)
) comment '收藏夹';

create table `like_relation` (
  `id` int(11) auto_increment comment '自增ID',
  `uid` int(11) not null comment '用户id',
  `aid` int(11) not null comment '文章id',
  `gmt_create` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_update` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  primary key (`id`)
) comment '点赞关系表';

create table `hot_search` (
  `id` int(11) auto_increment comment '自增id',
  `keyword` varchar(64) not null comment '热搜关键词',
  `score` int(11) default '0' comment '搜索次数',
  `gmt_create` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_update` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  primary key (`id`)
) comment '热搜表';

create table `user_ext` (
  `id` int(11) auto_increment comment '自增id',
  `uid` int(11) not null comment '用户id',
  `avatar` varchar(256) default '' comment '头像链接',
  `motto` varchar(128) default '' comment '座右铭',
  primary key (`id`)
) comment '用户信息扩展标';