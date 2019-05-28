package com.zlj.forum.web.mapper;

import com.zlj.forum.web.dataobject.ArticleDetailDO;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-27 17:18
 */
public interface ArticleDetailMapper {

    List<ArticleDetailDO> selectArticles(@Param("aidList") Collection<Long> aidList);

    List<ArticleDetailDO> selectArticleByUid(@Param("uid") Long uid, @Param("num") int num);

    List<ArticleDetailDO> getBasicArticle(@Param("num") int num);

    List<ArticleDetailDO> getArticleByFollowed(@Param("uid") Long uid);
}
