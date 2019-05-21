package com.zlj.forum.web.mapper;

import com.zlj.forum.web.dataobject.UserCommentDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-21 01:46
 */
public interface UserCommentMapper {

    int countByAid(@Param("aid") Long aid);

    /**
     * 获取评论列表
     * @param aid
     * @param from
     * @param size
     * @return
     */
    List<UserCommentDO> getCommentList(@Param("aid") Long aid, @Param("from") int from, @Param("size") int size);
}
