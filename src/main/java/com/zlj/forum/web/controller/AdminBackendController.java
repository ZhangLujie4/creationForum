package com.zlj.forum.web.controller;

import com.zlj.forum.common.VO.ResultVO;
import com.zlj.forum.common.utils.ResultVOUtil;
import com.zlj.forum.recom.RecomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-21 20:33
 */

@RestController
@RequestMapping("/api/common")
public class AdminBackendController {

    @Autowired
    private RecomService recomService;

    /**
     * 开启推荐刷新
     */
    @GetMapping("/recom/refresh")
    public ResultVO refresh(@RequestParam("isCF") boolean isCF,
                            @RequestParam("isCB") boolean isCB,
                            @RequestParam("isHR") boolean isHR){
        recomService.executeJobForAllUsers(isCF, isCB, isHR);
        return ResultVOUtil.success();
    };


}
