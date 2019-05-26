package com.zlj.forum.web.service;

import com.zlj.forum.web.dao.*;
import com.zlj.forum.web.dataobject.*;
import com.zlj.forum.web.form.ArticleForm;
import com.zlj.forum.web.mapper.ArticleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-21 11:22
 */

@Slf4j
@Component
public class AsyncCommonService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleJpaDAO articleJpaDAO;

    @Autowired
    private UserCommentJpaDAO userCommentJpaDAO;

    @Autowired
    private HotSearchJpaDAO hotSearchJpaDAO;

    @Autowired
    private UsersJpaDAO usersJpaDAO;

    @Autowired
    private ArticleDetailJpaDAO articleDetailJpaDAO;

    @Autowired
    private ArticleLogsJpaDAO articleLogsJpaDAO;

    @Async
    public void addCommentNum(Long aid) {
        articleMapper.addCommentNum(aid);
    }

    @Async
    public void updateHotSearch(String keyword) {
        /**
         * 记录搜索信息
         * 判断搜索条件
         * String content = form.getContent().replaceAll("[^\u4e00-\u9fa5a-zA-Z0-9]", "")
         */

        String hotWord = keyword.replaceAll("[^\u4e00-\u9fa5a-zA-Z0-9]", "");
        if (!StringUtils.isEmpty(hotWord)) {
            HotSearchDO hotSearchDO = hotSearchJpaDAO.findByKeyword(hotWord);
            if (null == hotSearchDO) {
                hotSearchDO = new HotSearchDO();
                hotSearchDO.setKeyword(hotWord);
                hotSearchDO.setScore(1L);
            } else {
                hotSearchDO.setScore(hotSearchDO.getScore() + 1);
            }
            hotSearchJpaDAO.save(hotSearchDO);
        }
    }

    /**
     * 异步修改赞数
     */
    @Async
    public void updateArticleLikeNum(Long aid, int addition) {
        ArticleDO articleDO = articleJpaDAO.getOne(aid);
        if (null == articleDO) {
            log.error("为查找到对应文档 aid={}", aid);
            return;
        }

        articleDO.setLikeNum(articleDO.getLikeNum() + addition);
        articleJpaDAO.save(articleDO);
    }

    /**
     * 异步修改收藏数
     */
    @Async
    public void updateArticleFavoriteNum(Long aid, int addition) {
        ArticleDO articleDO = articleJpaDAO.getOne(aid);
        if (null == articleDO) {
            log.error("为查找到对应文档 aid={}", aid);
            return;
        }

        articleDO.setFavoriteNum(articleDO.getFavoriteNum() + addition);
        articleJpaDAO.save(articleDO);
    }

    /**
     * 异步修改评论点赞数
     * @param cid
     * @param addition
     */
    @Async
    public void updateCommentLikeNum(Long cid, int addition) {
        UserCommentDO commentDO = userCommentJpaDAO.getOne(cid);
        if (null == commentDO) {
            log.error("为查找到对应评论 cid={}", cid);
            return;
        }
        commentDO.setLikeNum(commentDO.getLikeNum() + addition);
        userCommentJpaDAO.save(commentDO);
    }

    @Async
    public void saveUsers(UserDO userDO) {
        UsersDO usersDO = new UsersDO();
        usersDO.setPrefList("");
        usersDO.setUid(userDO.getUid());
        usersDO.setUsername(userDO.getUsername());
        usersJpaDAO.save(usersDO);
    }

    @Async
    public void updateArticleDetail(ArticleForm articleForm) {
        ArticleDetailDO articleDetailDO = articleDetailJpaDAO.findByAid(articleForm.getId());
        articleDetailDO.setContent(articleForm.getDetail());
        articleDetailDO.setTitle(articleForm.getTitle());
        articleDetailJpaDAO.save(articleDetailDO);
    }

    @Async
    public void addArticleDetail(Long aid, ArticleForm articleForm) {
        ArticleDetailDO articleDetailDO = new ArticleDetailDO();
        articleDetailDO.setTitle(articleForm.getTitle());
        articleDetailDO.setContent(articleForm.getDetail());
        articleDetailDO.setAid(aid);
        articleDetailJpaDAO.save(articleDetailDO);
    }

    @Async
    public void saveLogs(Long aid, Long uid, Integer degree) {
        ArticleLogsDO articleLogsDO = new ArticleLogsDO();
        articleLogsDO.setAid(aid);
        articleLogsDO.setPreferDegree(degree);
        articleLogsDO.setViewTime(new Date());
        articleLogsDO.setUid(uid);
        articleLogsJpaDAO.save(articleLogsDO);
    }
}
