package com.zlj.forum.web.service;

import com.zlj.forum.common.VO.ResultVO;
import com.zlj.forum.common.exception.ResultException;
import com.zlj.forum.common.utils.ResultVOUtil;
import com.zlj.forum.enums.ResultEnum;
import com.zlj.forum.web.dao.UserExtJpaDAO;
import com.zlj.forum.web.dao.UsersJpaDAO;
import com.zlj.forum.web.dataobject.ArticleDetailDO;
import com.zlj.forum.web.dataobject.UserExtDO;
import com.zlj.forum.web.dataobject.UsersDO;
import com.zlj.forum.web.mapper.ArticleDetailMapper;
import com.zlj.forum.web.mapper.FollowRelationMapper;
import com.zlj.forum.web.mapper.HotSearchMapper;
import com.zlj.forum.web.to.FollowTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-22 01:00
 */

@Service
public class RecommendService {

    @Autowired
    private HotSearchMapper hotSearchMapper;

    @Autowired
    private ArticleDetailMapper articleDetailMapper;

    @Autowired
    private UsersJpaDAO usersJpaDAO;

    @Autowired
    private FollowRelationMapper followRelationMapper;

    @Autowired
    private UserExtJpaDAO userExtJpaDAO;

    /**
     * 获得热搜关键词列表
     * @return
     */
    public ResultVO getHotWords() {

        // 统计词频
        // 词频热搜
        return ResultVOUtil.success(hotSearchMapper.getHotWords());
    }

    /**
     * 获取用户对应的推荐列表（必须是在用户登录后才能看到）
     * @param uid
     * @return
     */
    public ResultVO getRecomArticle(Long uid) {
        if (uid == null) {
            throw new ResultException(ResultEnum.NO_LOGIN);
        }
        List<ArticleDetailDO> articleDetailDOList  = articleDetailMapper.selectArticleByUid(uid, 30);
        // 为空或者小于5条时走兜底逻辑
        if (articleDetailDOList == null) {
            articleDetailDOList = articleDetailMapper.getBasicArticle(30);
            for (ArticleDetailDO articleDetailDO : articleDetailDOList) {
                // 4代表走的兜底逻辑
                articleDetailDO.setId(3L);
            }
        } else if (articleDetailDOList.size() < 5) {
            List<ArticleDetailDO> addtion = articleDetailMapper.getBasicArticle(30);
            List<Long> aids = articleDetailDOList.stream().map(articleDetailDO -> articleDetailDO.getAid()).collect(Collectors.toList());
            for (ArticleDetailDO articleDetailDO : addtion) {
                if (aids.contains(articleDetailDO.getAid())) {
                    continue;
                }
                articleDetailDO.setId(3L);
                articleDetailDOList.add(articleDetailDO);
            }
        }

        return ResultVOUtil.success(articleDetailDOList);
    }

    /**
     * 获取关注者的文章列表
     * @param uid
     * @return
     */
    public ResultVO getFollowArticle(Long uid) {
        if (uid == null) {
            throw new ResultException(ResultEnum.NO_LOGIN);
        }
        List<ArticleDetailDO> articleDetailDOList = articleDetailMapper.getArticleByFollowed(uid);
        articleDetailDOList.removeAll(Collections.singleton(null));
        return ResultVOUtil.success(articleDetailDOList);
    }

    /**
     * 获取相似用户的列表
     * @param uid
     * @return
     */
    public ResultVO getSimilarUser(Long uid) {
        if (uid == null) {
            return ResultVOUtil.success(followRelationMapper.getHotUsers());
        }
        List<Long> ids = followRelationMapper.getFollowIds(uid);
        if (ids == null) {
            ids = new ArrayList<>();
        }
        List<FollowTO> result = new ArrayList<>();
        ids.add(uid);
        //Map<String, List<FollowTO>> map = new HashMap<>();
        // 获取cf的获得的最相似用户
        UsersDO usersDO = usersJpaDAO.findByUid(uid);
        if (!StringUtils.isEmpty(usersDO.getUserList())) {
            String[] strs = usersDO.getUserList().split(" ");
            List<Long> uids = Arrays.asList(strs).stream().map(s -> Long.parseLong(s)).collect(Collectors.toList());
            List<Long> finalIds = ids;
            uids = uids.stream().filter(userId -> !finalIds.contains(userId)).collect(Collectors.toList());
            if (!uids.isEmpty()) {
                List<FollowTO> followTOS = followRelationMapper.getSimilar(uids);
                if (!CollectionUtils.isEmpty(followTOS)) {
                    followTOS = followTOS.stream().map(followTO -> {
                        followTO.setMotto(followTO.getMotto() != null ? "近邻用户:" + followTO.getMotto() : "近邻用户");
                        return followTO;
                    }).collect(Collectors.toList());
                    //map.put("similar", followTOS);
                    ids.addAll(followTOS.stream().map(followTO -> followTO.getUid()).collect(Collectors.toList()));
                    result.addAll(followTOS);
                }
            }
        }

        UserExtDO userExtDO = userExtJpaDAO.findByUid(uid);
        if (userExtDO != null && !StringUtils.isEmpty(userExtDO.getTags())) {
            String regexStr = Arrays.asList(userExtDO.getTags().split(",")).stream().collect(Collectors.joining("|"));
            List<FollowTO> followTOS = followRelationMapper.getTagsUser(regexStr);
            if (!CollectionUtils.isEmpty(followTOS)) {
                Iterator<FollowTO> it = followTOS.iterator();
                while (it.hasNext()) {
                    FollowTO followTO = it.next();
                    followTO.setMotto(followTO.getMotto() != null ? "兴趣用户:" + followTO.getMotto() : "兴趣用户");
                    if (ids.contains(followTO.getUid())) {
                        it.remove();
                        continue;
                    }
                }
                // map.put("tags", followTOS);
                result.addAll(followTOS);
            }
        }

        return ResultVOUtil.success(result);
    }
}
