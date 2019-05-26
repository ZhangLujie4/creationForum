package com.zlj.forum.web.service;

import com.zlj.forum.common.VO.ResultVO;
import com.zlj.forum.common.utils.ResultVOUtil;
import com.zlj.forum.enums.ResultEnum;
import com.zlj.forum.web.dao.*;
import com.zlj.forum.web.dataobject.*;
import com.zlj.forum.web.form.UserExtForm;
import com.zlj.forum.web.mapper.ArticleMapper;
import com.zlj.forum.web.mapper.FollowRelationMapper;
import com.zlj.forum.web.to.FollowTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-21 20:43
 */

@Service
@Slf4j
public class UserBasicService {

    @Autowired
    private LikeRelationJpaDAO likeRelationJpaDAO;

    @Autowired
    private MyFavoriteJpaDAO myFavoriteJpaDAO;

    @Autowired
    private AsyncCommonService asyncCommonService;

    @Autowired
    private UserExtJpaDAO userExtJpaDAO;

    @Autowired
    private UserJpaDAO userJpaDAO;

    @Autowired
    private LikeRelationCommentJpaDAO likeRelationCommentJpaDAO;

    @Autowired
    private FollowRelationJpaDAO followRelationJpaDAO;

    @Autowired
    private ArticleJpaDAO articleJpaDAO;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private FollowRelationMapper followRelationMapper;

    /**
     * 点赞/取消赞
     * @param uid
     * @param aid
     * @return
     */
    public ResultVO likeArticle(Long uid, Long aid) {
        LikeRelationDO likeRelationDO = likeRelationJpaDAO.findByAidAndUid(aid, uid);
        if (null == likeRelationDO) {
            likeRelationDO = new LikeRelationDO();
            likeRelationDO.setAid(aid);
            likeRelationDO.setUid(uid);
            likeRelationJpaDAO.save(likeRelationDO);
            asyncCommonService.updateArticleLikeNum(aid, 1);
            asyncCommonService.saveLogs(aid, uid, 1);
        } else {
            likeRelationJpaDAO.deleteById(likeRelationDO.getId());
            asyncCommonService.updateArticleLikeNum(aid, -1);
        }

        return ResultVOUtil.success("操作成功");
    }

    /**
     * 收藏，取消收藏
     * @param uid
     * @param aid
     * @return
     */
    public ResultVO favoriteArticle(Long uid, Long aid) {
        MyFavoriteDO myFavoriteDO = myFavoriteJpaDAO.findByAidAndUid(aid, uid);
        if (null == myFavoriteDO) {
            myFavoriteDO = new MyFavoriteDO();
            myFavoriteDO.setAid(aid);
            myFavoriteDO.setUid(uid);
            myFavoriteJpaDAO.save(myFavoriteDO);
            asyncCommonService.updateArticleFavoriteNum(aid, 1);
            asyncCommonService.saveLogs(aid, uid, 2);
        } else {
            myFavoriteJpaDAO.deleteById(myFavoriteDO.getId());
            asyncCommonService.updateArticleFavoriteNum(aid, -1);
        }

        return ResultVOUtil.success("操作成功");
    }

    /**
     * 评论点赞
     * @param uid
     * @param cid
     * @return
     */
    public ResultVO likeComment(Long uid, Long cid) {
        LikeRelationCommentDO commentDO = likeRelationCommentJpaDAO.findByCidAndUid(cid, uid);
        if (null == commentDO) {
            commentDO = new LikeRelationCommentDO();
            commentDO.setCid(cid);
            commentDO.setUid(uid);
            likeRelationCommentJpaDAO.save(commentDO);
            asyncCommonService.updateCommentLikeNum(cid, 1);
        } else {
            likeRelationCommentJpaDAO.deleteById(commentDO.getId());
            asyncCommonService.updateCommentLikeNum(cid, -1);
        }

        return ResultVOUtil.success("操作成功");
    }

    /**
     * 关注&取消关注
     * @param currentUid
     * @param uid
     * @return
     */
    public ResultVO followUser(Long currentUid, Long uid) {
        if (uid.equals(currentUid)) {
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(), "用户不能关注自己");
        }
        FollowRelationDO relationDO = followRelationJpaDAO.findByFansUidAndFollowedUid(currentUid, uid);
        if (null == relationDO) {
            relationDO = new FollowRelationDO();
            relationDO.setFansUid(currentUid);
            relationDO.setFollowedUid(uid);
            followRelationJpaDAO.save(relationDO);
        } else {
            followRelationJpaDAO.deleteById(relationDO.getId());
        }

        return ResultVOUtil.success("操作成功");
    }

    /**
     * 获取用户可编辑信息
     * @param uid
     * @return
     */
    public ResultVO getUserInfo(Long currentUid, Long uid) {
        Map<String, Object> map = new HashMap();
        boolean isSelf = uid.equals(currentUid);
        map.put("is_self", isSelf);
        map.put("is_follow", false);
        if (!isSelf) {
            FollowRelationDO relationDO = followRelationJpaDAO.findByFansUidAndFollowedUid(currentUid, uid);
            map.put("is_follow", relationDO != null);
        }
        UserDO userDO = userJpaDAO.findByUid(uid);
        map.put("username", userDO.getUsername());
        UserExtDO userExtDO = userExtJpaDAO.findByUid(uid);
        if (null != userExtDO) {
            map.put("avatar", userExtDO.getAvatar());
            map.put("motto", userExtDO.getMotto());
            map.put("nickname", userExtDO.getNickName());
            map.put("tags", userExtDO.getTags());
        }
        map.put("articleNum", articleJpaDAO.countByUid(uid));
        map.put("followNum", followRelationJpaDAO.countByFansUid(uid));
        map.put("fansNum", followRelationJpaDAO.countByFollowedUid(uid));
        map.put("favoriteNum", myFavoriteJpaDAO.countByUid(uid));
        return ResultVOUtil.success(map);
    }

    /**
     * 编辑用户信息
     * @param uid
     * @param extForm
     * @return
     */
    public ResultVO editUserInfo(Long uid, UserExtForm extForm) {
        UserExtDO userExtDO = userExtJpaDAO.findByUid(uid);
        if (userExtDO == null) {
            userExtDO = new UserExtDO();
        }

        if (null != extForm.getAvatar()) {
            userExtDO.setAvatar(extForm.getAvatar());
        }

        if (null != extForm.getMotto()) {
            userExtDO.setMotto(extForm.getMotto());
        }

        if (null != extForm.getNickName()) {
            userExtDO.setNickName(extForm.getNickName());
        }

        if (null != extForm.getTags()) {
            userExtDO.setTags(extForm.getTags());
        }
        userExtDO.setUid(uid);
        userExtJpaDAO.save(userExtDO);
        return ResultVOUtil.success(userExtDO);
    }

    /**
     * 获取用户文章列表
     * @param uid
     * @param page
     * @param size
     * @return
     */
    public ResultVO getUserArticle(Long uid, int page, int size) {
        Map<String, Object> map = new HashMap<>();
        long total = articleJpaDAO.countByUid(uid);
        map.put("total", total);
        map.put("more", page * size < total);
        map.put("page", page);
        map.put("size", size);
        List<ArticleDO> articleDOList = articleMapper.getArticleListByUid(uid, (page - 1) * size, size);
        if (!CollectionUtils.isEmpty(articleDOList)) {
            map.put("data", articleDOList);
        }
        return ResultVOUtil.success(map);
    }

    /**
     * 获取用户收藏文章列表
     * @param uid
     * @param page
     * @param size
     * @return
     */
    public ResultVO getUserFavorite(Long uid, int page, int size) {
        Map<String, Object> map = new HashMap<>();
        long total = myFavoriteJpaDAO.countByUid(uid);
        map.put("total", total);
        map.put("more", page * size < total);
        map.put("page", page);
        map.put("size", size);
        List<ArticleDO> articleDOList = articleMapper.getArticleListByAids(uid, (page -1) * size, size);
        if (!CollectionUtils.isEmpty(articleDOList)) {
            map.put("data", articleDOList);
        }
        return ResultVOUtil.success(map);
    }

    /**
     * 获取用户关注者列表
     * @param uid
     * @param page
     * @param size
     * @return
     */
    public ResultVO getUserFollower(Long currentId, Long uid, int page, int size) {
        Map<String, Object> map = new HashMap<>();
        long total = followRelationJpaDAO.countByFansUid(uid);
        map.put("total", total);
        map.put("more", page * size < total);
        map.put("page", page);
        map.put("size", size);
        List<FollowTO> followForms = followRelationMapper.getUserFollower(uid, (page -1) * size, size);
        if (null != currentId) {
            List<Long> ids = followRelationMapper.getFollowIds(currentId);
            for (FollowTO followTO : followForms) {
                if (ids.contains(followTO.getUid())) {
                    followTO.setIsFollow(true);
                }
            }
        }
        if (!CollectionUtils.isEmpty(followForms)) {
            map.put("data", followForms);
        }
        return ResultVOUtil.success(map);
    }

    /**
     * 获取用户粉丝列表
     * @param uid
     * @param page
     * @param size
     * @return
     */
    public ResultVO getUserFans(Long currentId, Long uid, int page, int size) {
        Map<String, Object> map = new HashMap<>();
        long total = followRelationJpaDAO.countByFollowedUid(uid);
        map.put("total", total);
        map.put("more", page * size < total);
        map.put("page", page);
        map.put("size", size);
        List<FollowTO> followForms = followRelationMapper.getUserFans(uid, (page -1) * size, size);
        if (null != currentId) {
            List<Long> ids = followRelationMapper.getFollowIds(currentId);
            for (FollowTO followTO : followForms) {
                if (ids.contains(followTO.getUid())) {
                    followTO.setIsFollow(true);
                }
            }
        }
        if (!CollectionUtils.isEmpty(followForms)) {
            map.put("data", followForms);
        }
        return ResultVOUtil.success(map);
    }
}
