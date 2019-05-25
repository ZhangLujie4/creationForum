package com.zlj.forum.web.to;

import lombok.Data;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-22 00:29
 */

@Data
public class FollowTO {

    private String nickName;

    private String username;

    private String avatar;

    private String motto;

    private Long uid;
}
