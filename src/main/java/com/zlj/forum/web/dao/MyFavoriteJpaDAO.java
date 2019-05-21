package com.zlj.forum.web.dao;

import com.zlj.forum.web.dataobject.MyFavoriteDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-20 23:37
 */
public interface MyFavoriteJpaDAO extends JpaRepository<MyFavoriteDO, Long> {

    MyFavoriteDO findByAidAndUid(long aid, long uid);
}
