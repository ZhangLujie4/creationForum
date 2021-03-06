package com.zlj.forum.web.dao;

import com.zlj.forum.web.dataobject.RecommendActionsDO;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-26 22:40
 */
public interface RecommendActionsJpaDAO extends JpaRepository<RecommendActionsDO, Long> {

    @Transactional
    void removeByAid(Long aid);
}
