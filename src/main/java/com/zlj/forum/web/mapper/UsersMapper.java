package com.zlj.forum.web.mapper;

import com.zlj.forum.web.dataobject.UsersDO;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-27 00:40
 */
public interface UsersMapper {

    /**
     * 获取所有的用户uid
     * @return
     */
    List<Long> getAllUserIds();

    List<UsersDO> selectUsers(@Param("uidList") Collection<Long> uidList);

    void updatePrefList(@Param("prefList") String prefList, @Param("uid") Long uid);

    void updateUserList(@Param("userList") String userList, @Param("uid") Long uid);
}
