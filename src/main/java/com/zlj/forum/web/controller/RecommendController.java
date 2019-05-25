package com.zlj.forum.web.controller;

import com.zlj.forum.common.VO.ResultVO;
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

    /**
     * 用户作者推荐
     */

    /**
     * 热搜关键词
     */
    @GetMapping("/common/hot/word")
    public ResultVO getHotWords() {
        return recommendService.getHotWords();
    }
}
