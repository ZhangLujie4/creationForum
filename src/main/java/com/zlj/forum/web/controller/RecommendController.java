package com.zlj.forum.web.controller;

import com.zlj.forum.common.VO.ResultVO;
import com.zlj.forum.common.utils.SecurityUtil;
import com.zlj.forum.web.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-21 20:32
 */

@RestController
@RequestMapping("/api")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    /**
     * 用户文章推荐
     */
    @GetMapping("/user/recom/article")
    public ResultVO getRecomArticle() {
        // 通过三种推荐算法获得的文章列表
        Long uid = SecurityUtil.getCurrentUserId();
        return recommendService.getRecomArticle(uid);
    }

    @GetMapping("/user/follower/article")
    public ResultVO getFollowerArticle() {
        // 关注者文章
        Long uid = SecurityUtil.getCurrentUserId();
        return recommendService.getFollowArticle(uid);
    }

    /**
     * 用户作者推荐
     */
    @GetMapping("/common/recom/user")
    public ResultVO getRecomUser() {
        // 最相似的用户 + 兴趣相同的用户
        Long uid = SecurityUtil.getCurrentUserId();
        return recommendService.getSimilarUser(uid);
    }

    /**
     * 热搜关键词
     */
    @GetMapping("/common/hot/word")
    public ResultVO getHotWords() {
        return recommendService.getHotWords();
    }
}
