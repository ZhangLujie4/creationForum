package com.zlj.forum.web.controller;


import com.zlj.forum.common.VO.ResultVO;
import com.zlj.forum.common.utils.ResultVOUtil;
import com.zlj.forum.common.utils.SecurityUtil;
import com.zlj.forum.enums.ResultEnum;
import com.zlj.forum.web.form.ArticleForm;
import com.zlj.forum.web.form.CommentForm;
import com.zlj.forum.web.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-20 20:21
 */

@RestController
@RequestMapping("/api")
public class ArticleController {

    @Autowired
    private ArticleService articleService;


    /**
     * api: /api/user/article/edit
     */
    @PostMapping("/user/article/edit")
    public ResultVO editArticle(@RequestBody ArticleForm articleForm) {
        Long uid = SecurityUtil.getCurrentUserId();
        if (uid == null || uid == 0) {
            return ResultVOUtil.error(ResultEnum.NO_LOGIN);
        }

        if (articleForm.getId() != null && articleForm.getId() > 0) {
            return articleService.updateArticle(articleForm);
        } else {
            return articleService.addArticle(articleForm);
        }
    }

    @PostMapping("/user/article/delete")
    public ResultVO deleteArticle(@RequestParam(name = "id") String id) {
        Long uid = SecurityUtil.getCurrentUserId();
        return articleService.deleteArticle(uid, id);
    }

    @GetMapping("/common/article/detail")
    public ResultVO getDetail(@RequestParam(name = "id") String id) {
        return articleService.getDetail(id);
    }

    @GetMapping("/common/comment/list")
    public ResultVO getCommentList(@RequestParam(name = "id") String id,
                                   @RequestParam(name = "page", defaultValue = "1") Integer page,
                                   @RequestParam(name = "size", defaultValue = "10") Integer size) {
        Long uid = SecurityUtil.getCurrentUserId();
        return articleService.getCommentList(uid, id, page, size);
    }

    @PostMapping("/user/comment/add")
    public ResultVO addComment(@RequestBody CommentForm commentForm) {
        Long uid = SecurityUtil.getCurrentUserId();
        return articleService.addComment(uid, commentForm);
    }

    @GetMapping("/common/home/list")
    public ResultVO getHomeList(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return articleService.getHomeList(page, size);
    }

    @GetMapping("/common/search/list")
    public ResultVO getArticleListByKeyword(@RequestParam(name = "keyword") String keyword,
                                            @RequestParam(name = "page", defaultValue = "1") Integer page,
                                            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return articleService.getListByKeyword(keyword, page, size);
    }
}