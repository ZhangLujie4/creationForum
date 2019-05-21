package com.zlj.forum.web.mapper;

import com.zlj.forum.web.dataobject.ArticleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-21 11:24
 */

@Mapper
public interface ArticleMapper {

    Long addCommentNum(@Param("aid") Long aid);

    List<ArticleDO> getHomeList(@Param("from") int from, @Param("size") int size);

    List<ArticleDO> getArticleListByUid(@Param("uid") long uid, @Param("from") int from, @Param("size") int size);

    List<ArticleDO> getArticleListByAids(@Param("uid") long uid, @Param("from") int from, @Param("size") int size);

}
