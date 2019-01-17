package com.zlj.forum.common.exception;

import com.zlj.forum.enums.ResultEnum;
import lombok.Getter;

/**
 * @author tori
 * 2018/8/6 下午10:34
 */

@Getter
public class ResultException extends RuntimeException {

    private Integer code;

    public ResultException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public ResultException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }
}
