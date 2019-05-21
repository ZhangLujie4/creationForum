package com.zlj.forum.web.dao;

import com.zlj.forum.web.dataobject.HotSearchDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-21 17:02
 */
public interface HotSearchJpaDAO extends JpaRepository<HotSearchDO, Long> {

    HotSearchDO findByKeyword(String keyword);
}
