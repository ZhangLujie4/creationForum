package com.zlj.forum.web.dao;

import com.zlj.forum.web.dataobject.ArticleDetailDO;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-26 22:38
 */
public interface ArticleDetailJpaDAO extends JpaRepository<ArticleDetailDO, Long> {

    ArticleDetailDO findByAid(Long aid);

    @Transactional
    void removeByAid(Long aid);
}
