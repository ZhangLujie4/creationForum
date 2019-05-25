登录
```
/api/common/user/login
```
注册
```
/api/common/user/register
```
上传&修改文档
```
/api/user/article/edit
```
删除文档
```
/api/user/article/delete
```
获取文档详情
```
/api/common/article/detail
```
获取文章评论列表
```
/api/common/comment/list
```
添加评论
```
/api/user/comment/add
```
获取首页文章列表
```
/api/common/home/list
```
获取搜索列表
```
/api/common/search/list
```

推荐算法/搜索算法/

create index hot_list_index on article (like_num desc, favorite_num desc, comment_num desc, gmt_update desc) 

