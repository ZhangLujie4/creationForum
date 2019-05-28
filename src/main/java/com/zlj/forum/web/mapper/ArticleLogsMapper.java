package com.zlj.forum.web.mapper;

import com.zlj.forum.web.dataobject.ArticleLogsDO;
import com.zlj.forum.web.to.ItemTO;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-27 12:42
 */
public interface ArticleLogsMapper {

    List<ArticleLogsDO> findAllUserLogs();

    void deleteLogs(@Param("inSql") String inSql);

    List<ArticleLogsDO> selectUserLogs(@Param("timeNode") String timeNode);

    List<ArticleLogsDO> selectLogsByUids(@Param("uidList") Collection<Long> uidList);

    List<ArticleLogsDO> selectSingleUserLog(@Param("uid") Long uid);

    List<ItemTO> getHotArticles(@Param("viewTime") String viewTime);
}
