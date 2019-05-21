package com.zlj.forum.web.dao;

import com.zlj.forum.web.dataobject.LikeRelationDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-20 23:33
 */
public interface LikeRelationJpaDAO extends JpaRepository<LikeRelationDO, Long> {

    LikeRelationDO findByAidAndUid(long aid, long uid);
}
