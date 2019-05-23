package com.zlj.forum.web.to;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-21 01:51
 */

@Data
public class UserCommentTO {

    private Long id;

    private Long uid;

    private String content;

    private Object reply;

    private Long likeNum;

    private String nickName;

    private String avatar;

    private boolean isLike = false;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
}
