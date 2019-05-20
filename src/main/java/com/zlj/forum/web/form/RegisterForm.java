package com.zlj.forum.web.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author tori
 * @description
 * @date 2018/8/25 下午9:44
 */

@Data
public class RegisterForm {

    @NotBlank(message = "账户不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
