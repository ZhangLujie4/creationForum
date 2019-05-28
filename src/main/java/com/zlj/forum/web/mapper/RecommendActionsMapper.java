package com.zlj.forum.web.mapper;

import com.zlj.forum.web.dataobject.RecommendActionsDO;
import com.zlj.forum.web.to.ItemTO;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-27 20:58
 */
public interface RecommendActionsMapper {

    List<RecommendActionsDO> getHistoryRecom(@Param("uid") Long uid, @Param("deriveTime") String deriveTime);

    int getReadyRecomNum(@Param("uid") Long uid, @Param("deriveTime") Timestamp deriveTime);
}
