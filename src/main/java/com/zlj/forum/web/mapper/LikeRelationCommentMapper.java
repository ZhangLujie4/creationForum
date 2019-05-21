package com.zlj.forum.web.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-21 09:09
 */
public interface LikeRelationCommentMapper {

    List<Long> getRelationList(@Param("uid") Long uid,
                                        @Param("cids") List<Long> cids);
}
