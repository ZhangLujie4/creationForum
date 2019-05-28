package com.zlj.forum.recom;

import java.util.List;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-27 20:35
 */
public interface RecommendAlgorithm {

    //协同过滤
    public static final int CF=0;
    //基于内容的推荐
    public static final int CB=1;
    //基于热门文章的推荐
    public static final int HR=2;

    /**
     * 根据用户返回推荐结果
     */
    public void recommend(List<Long> uidList);
}
