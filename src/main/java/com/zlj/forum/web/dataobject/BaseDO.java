package com.zlj.forum.web.dataobject;

import lombok.Data;

import java.util.Date;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-20 15:15
 */

@Data
public class BaseDO {

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 更新时间
     */
    private Date gmtUpdate;
}
