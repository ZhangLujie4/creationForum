package com.zlj.forum.web.to;

import lombok.Data;

import java.util.Date;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-21 15:45
 */

@Data
public class ArticleTO {

    private Long id;

    private String title;

    private Long uid;

    private Long likeNum;

    private Long favoriteNum;

    private Long commentNum;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 更新时间
     */
    private Date gmtUpdate;

    private Object content;
}
