create table `user` (
  `id` int(11) auto_increment comment '自增id',
  `uid` int(11) default '0' comment '用户id',
  `username` varchar(32) not null comment '账户名',
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
  `favorite_num` int(11) default '0' comment '收藏数',
  `like_num` int(11) default '0' comment '喜欢数',
  `comment_num` int(11) default '0' comment '评论数',
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

create table `like_relation_comment` (
  `id` int(11) auto_increment comment '自增id',
  `uid` int(11) not null comment '用户id',
  `cid` int(11) not null comment '评论id',
  `gmt_create` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_update` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  primary key (`id`)
) comment '评论点赞关系表';

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
  `nick_name` varchar(64) default '' comment '昵称',
  `tags` varchar(256) default '' comment '用户标签',
  primary key (`id`)
) comment '用户信息扩展标';

create table `user_comment` (
  `id` int(11) auto_increment comment '自增id',
  `aid` int(11) default '0' comment '文章id',
  `uid` int(11) default '0' comment '用户id',
  `nick_name` varchar(64) default '' comment '用户昵称',
  `content` varchar(256) default '' comment '评论内容',
  `reply` varchar(512) default '' comment '回复内容',
  `like_num` int(11) default '0' comment '点赞数',
  `avatar` varchar(256) default '' comment '用户头像',
  `gmt_create` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_update` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  primary key (`id`)
) comment '用户评论';

# 推荐相关表
create table `article_detail` (
  `id` int(11) not null auto_increment comment '自增id',
  `content` text comment '内容',
  `title` text not null comment '标题',
  `aid` int(16) not null comment '文章id',
  primary key (`id`),
  unique key (`aid`)
) comment '文章详情表';

create table `users` (
  `id` int(11) not null auto_increment comment '用户关系表',
  `uid` int(11) not null comment '用户ID',
  `pref_list` text comment '用户关键词',
  `user_list` text comment '用户相似度最高的几个人',
  `username` varchar(32) not null comment '用户名',
  primary key (`id`),
  unique key (`uid`)
) comment '用户关键词表';

create table `article_logs` (
  `id` int(11) not null auto_increment comment '自增id',
  `uid` int(11) not null comment '用户uid',
  `aid` int(11) not null comment '文章id',
  `prefer_degree` int(11) default '0' comment '喜欢等级',
  `view_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '浏览时间',
  primary key (`id`)
#   constraint `artclelogs_article_id` foreign key (`uid`) references `article_detail` (`uid`),
#   constraint `artclelogs_users_id` foreign key (`aid`) references `users` (`aid`)
) comment '用户操作记录表';

create table `recommend_actions` (
  `id` int(11) not null auto_increment comment '自增id',
  `uid` int(11) not null comment '用户id',
  `aid` int(11) not null comment '文章id',
  `derive_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `feedback` int(1) default '0' comment '是否浏览',
  `derive_algorithm` int(11) not null comment '推荐算法',
#  `reason` varchar(256) default '' comment '推荐原因',
  primary key (`id`)
#   constraint `recommend_article_id` foreign key (`uid`) references `article_detail` (`uid`),
#   constraint `recommend_users_id` foreign key (`aid`) references `users` (`aid`)
) comment '用户推荐表';
