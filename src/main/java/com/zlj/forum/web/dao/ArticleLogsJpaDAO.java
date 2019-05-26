package com.zlj.forum.web.dao;

import com.zlj.forum.web.dataobject.ArticleLogsDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-26 22:39
 */
public interface ArticleLogsJpaDAO extends JpaRepository<ArticleLogsDO, Long> {
}
