package com.zlj.forum.web.service;

import com.zlj.forum.common.VO.ResultVO;
import com.zlj.forum.common.utils.ResultVOUtil;
import com.zlj.forum.web.mapper.HotSearchMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-22 01:00
 */

@Service
public class RecommendService {

    @Autowired
    private HotSearchMapper hotSearchMapper;

    /**
     * 获得热搜关键词列表
     * @return
     */
    public ResultVO getHotWords() {

        // todo 热搜关键词 人工置顶 + 统计词频

        // 词频热搜
        return ResultVOUtil.success(hotSearchMapper.getHotWords());
    }
}
