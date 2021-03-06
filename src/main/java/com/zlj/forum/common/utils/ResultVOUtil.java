package com.zlj.forum.common.utils;


import com.zlj.forum.common.VO.ResultVO;
import com.zlj.forum.enums.ResultEnum;

/**
 * @author tori
 * 2018/7/30 上午10:51
 */
public class ResultVOUtil {

    public static ResultVO success(Object content) {
        ResultVO resultVO = new ResultVO();
        resultVO.setSuccess(true);
        resultVO.setCode(0);
        resultVO.setContent(content);
        return resultVO;
    }

    public static ResultVO success() {
        return ResultVOUtil.success(null);
    }

    public static ResultVO error(Integer code, String status) {
        ResultVO resultVO = new ResultVO();
        resultVO.setSuccess(false);
        resultVO.setCode(code);
        resultVO.setContent(status);
        return resultVO;
    }

    public static ResultVO error(ResultEnum resultEnum) {
        ResultVO resultVO = new ResultVO();
        resultVO.setSuccess(false);
        resultVO.setCode(resultEnum.getCode());
        resultVO.setContent(resultEnum.getMsg());
        return resultVO;
    }

}
