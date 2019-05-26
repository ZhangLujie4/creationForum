package com.zlj.forum.web.dao;

import com.zlj.forum.web.dataobject.UsersDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-26 22:41
 */
public interface UsersJpaDAO extends JpaRepository<UsersDO, Long> {
}
