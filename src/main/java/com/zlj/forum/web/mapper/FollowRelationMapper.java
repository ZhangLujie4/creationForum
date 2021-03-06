package com.zlj.forum.web.mapper;

import com.zlj.forum.web.to.FollowTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-22 00:22
 */
public interface FollowRelationMapper {

    List<FollowTO> getUserFollower(@Param("uid") long uid, @Param("from") int from, @Param("size") int size);

    List<FollowTO> getUserFans(@Param("uid") long uid, @Param("from") int from, @Param("size") int size);

    List<Long> getFollowIds(@Param("uid") long uid);

    List<FollowTO> getSimilar(@Param("uids") List<Long> uids);

    List<FollowTO> getTagsUser(@Param("regexStr") String regexStr);

    List<FollowTO> getHotUsers();
}
