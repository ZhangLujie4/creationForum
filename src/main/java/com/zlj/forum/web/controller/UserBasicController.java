package com.zlj.forum.web.controller;

import com.zlj.forum.common.VO.ResultVO;
import com.zlj.forum.common.utils.ResultVOUtil;
import com.zlj.forum.common.utils.SecurityUtil;
import com.zlj.forum.enums.ResultEnum;
import com.zlj.forum.web.form.UserExtForm;
import com.zlj.forum.web.service.UserBasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-21 20:27
 */

@RestController
@RequestMapping("/api")
public class UserBasicController {

    @Autowired
    private UserBasicService userBasicService;

    /**
     * 文章点赞
     */
    @GetMapping("/user/article/like")
    public ResultVO likeArticle(@RequestParam(name = "id") Long id) {
        if (id == null) {
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }
        Long uid = SecurityUtil.getCurrentUserId();
        return userBasicService.likeArticle(uid, id);
    }


    /**
     * 文章收藏
     */
    @GetMapping("/user/article/favorite")
    public ResultVO favoriteArticle(@RequestParam(name = "id") Long id) {
        if (id == null) {
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }
        Long uid = SecurityUtil.getCurrentUserId();
        return userBasicService.favoriteArticle(uid, id);
    }

    /**
     * 评论点赞
     */
    @GetMapping("/user/comment/like")
    public ResultVO likeComment(@RequestParam(name = "id") Long id) {
        if (id == null) {
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }
        Long uid = SecurityUtil.getCurrentUserId();
        return userBasicService.likeComment(uid, id);
    }

    /**
     * 关注&取消关注
     */
    @GetMapping("/user/follow")
    public ResultVO followUser(@RequestParam(name = "id") Long id) {
        if (id == null) {
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }
        Long uid = SecurityUtil.getCurrentUserId();
        return userBasicService.followUser(uid, id);
    }

    /**
     * 用户信息获取
     */
    @GetMapping("/user/home/info")
    public ResultVO getUserInfo(@RequestParam(name = "id") Long id) {
        if (id == null) {
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }
        Long uid = SecurityUtil.getCurrentUserId();
        return userBasicService.getUserInfo(uid, id);
    }

    /**
     * 用户信息编辑
     */
    @PostMapping("/user/info/edit")
    public ResultVO editUserInfo(@RequestBody UserExtForm extForm) {

        Long uid = SecurityUtil.getCurrentUserId();
        return userBasicService.editUserInfo(uid, extForm);
    }

    /**
     * 获取用户文章列表
     */
    @GetMapping("/common/user/article")
    public ResultVO getUserArticle(@RequestParam(name = "id") Long id,
                                   @RequestParam(name = "page", defaultValue = "1") int page,
                                   @RequestParam(name = "size", defaultValue = "15") int size) {
        return userBasicService.getUserArticle(id, page, size);
    }


    /**
     * 获取作者收藏夹
     */
    @GetMapping("/common/user/favorite")
    public ResultVO getUserFavorite(@RequestParam(name = "id") Long id,
                                    @RequestParam(name = "page", defaultValue = "1") int page,
                                    @RequestParam(name = "size", defaultValue = "15") int size) {
        return userBasicService.getUserFavorite(id, page, size);
    }

    /**
     * 获取关注用户
     */
    @GetMapping("/common/user/follower")
    public ResultVO getUserFollower(@RequestParam(name = "id") Long id,
                                    @RequestParam(name = "page", defaultValue = "1") int page,
                                    @RequestParam(name = "size", defaultValue = "15") int size) {
        return userBasicService.getUserFollower(id, page, size);
    }

    /**
     * 获取粉丝用户
     */
    @GetMapping("/common/user/fans")
    public ResultVO getUserFans(@RequestParam(name = "id") Long id,
                                @RequestParam(name = "page", defaultValue = "1") int page,
                                @RequestParam(name = "size", defaultValue = "15") int size) {
        return userBasicService.getUserFans(id, page, size);
    }


}
