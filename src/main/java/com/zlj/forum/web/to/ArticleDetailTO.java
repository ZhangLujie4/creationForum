package com.zlj.forum.web.to;

import lombok.Data;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-28 14:13
 */

@Data
public class ArticleDetailTO {

    private Long id;

    private Long aid;

    private String content;

    private String title;
}
