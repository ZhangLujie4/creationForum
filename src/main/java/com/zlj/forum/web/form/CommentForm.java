package com.zlj.forum.web.form;

import lombok.Data;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-21 10:00
 */

@Data
public class CommentForm {

    private Long rid;

    private Long aid;

    private String content;
}
