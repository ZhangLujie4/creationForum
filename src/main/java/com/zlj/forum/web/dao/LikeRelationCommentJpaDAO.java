package com.zlj.forum.web.dao;

import com.zlj.forum.web.dataobject.LikeRelationCommentDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-21 21:29
 */
public interface LikeRelationCommentJpaDAO extends JpaRepository<LikeRelationCommentDO, Long> {

    LikeRelationCommentDO findByCidAndUid(Long cid, Long uid);
}
