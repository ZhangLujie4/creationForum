package com.zlj.forum.web.dao;

import com.zlj.forum.web.dataobject.UserExtDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-20 23:14
 */


public interface UserExtJpaDAO extends JpaRepository<UserExtDO, Long> {
    UserExtDO findByUid(long uid);
}
