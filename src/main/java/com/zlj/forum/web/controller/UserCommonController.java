package com.zlj.forum.web.controller;

import com.zlj.forum.common.VO.ResultVO;
import com.zlj.forum.common.exception.ResultException;
import com.zlj.forum.enums.ResultEnum;
import com.zlj.forum.web.form.RegisterForm;
import com.zlj.forum.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-20 14:19
 */

@Slf4j
@RestController
@RequestMapping("/api/common")
public class UserCommonController {

    @Autowired
    private UserService userService;



    @PostMapping("/user/register")
    public ResultVO register(@RequestBody @Valid RegisterForm registerForm,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("参数错误,用户注册失败");
            throw new ResultException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }

        return userService.register(registerForm);
    }

    @PostMapping("/user/login")
    public ResultVO login(@RequestBody RegisterForm registerForm) throws Exception {

        return userService.login(registerForm);
    }
}
