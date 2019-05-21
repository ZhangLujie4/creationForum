package com.zlj.forum.web.dao;

import com.zlj.forum.web.dataobject.UserDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-20 14:34
 */
public interface UserJpaDAO extends JpaRepository<UserDO, Long> {

    /**
     * 根据用户名查询
     * @param username
     * @return
     */
    UserDO findByUsername(String username);

    UserDO findByUid(long uid);

}
