package com.zlj.forum.web.dao;

import com.zlj.forum.web.dataobject.FollowRelationDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-20 23:29
 */
public interface FollowRelationJpaDAO extends JpaRepository<FollowRelationDO, Long> {

    FollowRelationDO findByFansUidAndFollowedUid(Long fansUid, Long followedUid);

    /**
     * 关注数
     * @return
     */
    long countByFansUid(Long uid);

    /**
     * 粉丝数
     * @param uid
     * @return
     */
    long countByFollowedUid(Long uid);
}
