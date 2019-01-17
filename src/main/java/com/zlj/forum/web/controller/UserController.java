package com.zlj.forum.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tori
 * @description
 * @date 2018/8/25 下午9:40
 */

@RestController
@RequestMapping("/api/public")
public class UserController {

    @PostMapping("/post")
    public String sendPost() {
        return "sendPost";
    }

    @GetMapping("/get")
    public String sendGet() {
        return "sendGet";
    }
}
