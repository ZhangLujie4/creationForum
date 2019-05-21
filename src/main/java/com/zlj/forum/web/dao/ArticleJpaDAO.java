package com.zlj.forum.web.dao;

import com.zlj.forum.web.dataobject.ArticleDO;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-20 20:48
 */
public interface ArticleJpaDAO extends JpaRepository<ArticleDO, Long> {

    /**
     * 根据id和uid删除文档
     * @param id
     * @param uid
     * @return
     */
    @Transactional
    void removeByIdAndUid(Long id, Long uid);
}
