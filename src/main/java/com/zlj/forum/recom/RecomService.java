package com.zlj.forum.recom;

import com.zlj.forum.recom.CollaborativeFilterRecommend.UserBasedCollaborativeRecommender;
import com.zlj.forum.recom.ContentBasedRecommend.ContentBasedRecommender;
import com.zlj.forum.recom.HotRecommend.HotRecommender;
import com.zlj.forum.web.mapper.UsersMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-27 00:09
 */

@Slf4j
@Service
public class RecomService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private UserBasedCollaborativeRecommender collaborativeRecommender;

    @Autowired
    private ContentBasedRecommender contentBasedRecommender;

    @Autowired
    private HotRecommender hotRecommender;

    public void executeJobForAllUsers(boolean isCF, boolean isCB, boolean isHR) {
        List<Long> uids = usersMapper.getAllUserIds();
        executeJobForUsers(uids, isCF, isCB, isHR);
    }

    /**
     *
     * @param uidList 做推荐的用户id
     * @param isCF 是否启用协同过滤的推荐(Collaborative Filtering)
     * @param isCB 是否启用基于内容的推荐(Content-Based Recommendation)
     * @param isHR 是否启用热门文章推荐(Hot Article Recommendation)
     */
    public void executeJobForUsers(List<Long> uidList, boolean isCF, boolean isCB, boolean isHR) {
        try {
            if (isCF) {
                collaborativeRecommender.recommend(uidList);
            }
            if (isCB) {
                contentBasedRecommender.recommend(uidList);
            }
            if (isHR) {
                hotRecommender.recommend(uidList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("本次推荐结束于"+new Date());
    }
}
