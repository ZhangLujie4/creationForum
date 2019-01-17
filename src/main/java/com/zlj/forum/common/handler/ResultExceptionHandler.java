package com.zlj.forum.common.handler;

import com.zlj.forum.common.VO.ResultVO;
import com.zlj.forum.common.exception.ResultException;
import com.zlj.forum.common.utils.ResultVOUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author tori
 * @description
 * @date 2018/8/25 下午4:03
 */

@ControllerAdvice
public class ResultExceptionHandler {

    @ExceptionHandler(value = ResultException.class)
    @ResponseBody
    public ResultVO handlerSchoolException(ResultException e) {
        return ResultVOUtil.error(e.getCode(), e.getMessage());
    }
}
