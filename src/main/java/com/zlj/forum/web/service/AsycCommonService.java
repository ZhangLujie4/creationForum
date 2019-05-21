package com.zlj.forum.web.service;

import com.zlj.forum.web.dao.HotSearchJpaDAO;
import com.zlj.forum.web.dataobject.HotSearchDO;
import com.zlj.forum.web.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-21 11:22
 */

@Component
public class AsycCommonService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private HotSearchJpaDAO hotSearchJpaDAO;

    @Async
    public void addCommentNum(Long aid) {
        articleMapper.addCommentNum(aid);
    }

    @Async
    public void updateHotSearch(String keyword) {
        /**
         * 记录搜索信息
         * 判断搜索条件
         * String content = form.getContent().replaceAll("[^\u4e00-\u9fa5a-zA-Z0-9]", "")
         */

        String hotWord = keyword.replaceAll("[^\u4e00-\u9fa5a-zA-Z0-9]", "");
        if (!StringUtils.isEmpty(hotWord)) {
            HotSearchDO hotSearchDO = hotSearchJpaDAO.findByKeyword(hotWord);
            if (null == hotSearchDO) {
                hotSearchDO = new HotSearchDO();
                hotSearchDO.setKeyword(hotWord);
                hotSearchDO.setScore(1L);
            } else {
                hotSearchDO.setScore(hotSearchDO.getScore() + 1);
            }
            hotSearchJpaDAO.save(hotSearchDO);
        }
    }
}
