package com.zlj.forum.web.controller;

import com.zlj.forum.web.form.ItemForm;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * @author tori
 * @description
 * @date 2018/8/25 下午9:40
 */

@RestController
@RequestMapping("/api/private")
public class User2Controller {

    @ApiOperation("就是一个test")
    @PostMapping("/post")
    public String sendPost(@RequestBody ItemForm itemForm) {
        return itemForm.getName() + itemForm.getPass();
    }

    @GetMapping("/get")
    public String sendGet() {
        return "send2Get";
    }
}
