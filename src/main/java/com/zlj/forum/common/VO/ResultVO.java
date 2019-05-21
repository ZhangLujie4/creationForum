package com.zlj.forum.common.VO;

import lombok.Data;

/**
 * @author tori
 * 2018/7/30 上午10:49
 */

@Data
public class ResultVO<T> {

    private Boolean success;

    private Integer code;

    private T content;
}
