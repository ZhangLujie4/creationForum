package com.zlj.forum.web.dao;

import com.zlj.forum.web.dataobject.UserCommentDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-21 09:00
 */
public interface UserCommentJpaDAO extends JpaRepository<UserCommentDO, Long> {

}
