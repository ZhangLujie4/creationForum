package com.zlj.forum.recom.HotRecommend;

import com.zlj.forum.common.utils.TimeFormatUtil;
import com.zlj.forum.recom.RecomCommonUtil;
import com.zlj.forum.recom.RecommendAlgorithm;
import com.zlj.forum.web.mapper.ArticleLogsMapper;
import com.zlj.forum.web.mapper.RecommendActionsMapper;
import com.zlj.forum.web.to.ItemTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

/**
 * @author zhanglujie
 * @description 基于热门点击的文章生成的推荐，一般用于在cf和cb算法推荐结果数较少的情况下补充
 * @date 2019-05-27 13:22
 */

@Service
public class HotRecommender implements RecommendAlgorithm {

    // 十天内热门文章
    public static int beforeDays = -10;

    // 推荐系统的每日推荐文章总数
    public static int TOTAL_REC_NUM = 20;

    @Autowired
    private ArticleLogsMapper articleLogsMapper;

    @Autowired
    private RecommendActionsMapper recommendActionsMapper;

    @Autowired
    private RecomCommonUtil recomCommonUtil;

    public List<Long> formTodayTopHotNewsList() {
        ArrayList<Long> hotArticleRecommend = new ArrayList<>();
        try {
            List<ItemTO> itemTOS = articleLogsMapper.getHotArticles(TimeFormatUtil.getSpecificDayFormat(beforeDays));
            for (ItemTO itemTO : itemTOS) {
                hotArticleRecommend.add(itemTO.getIid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hotArticleRecommend;
    }

    @Override
    public void recommend(List<Long> uidList) {

        List<Long> hotArticleRecommend = formTodayTopHotNewsList();

        Timestamp timestamp = TimeFormatUtil.getCertainTimestamp(0, 0, 0);
        System.out.println("HR start at "+new Date());
        int count=0;
        for (Long uid : uidList) {
            // 获取已经预备给用户推荐的文章，如果数目少于推荐最低限制则补充热门文章推荐
            int recNum = recommendActionsMapper.getReadyRecomNum(uid, timestamp);
            int delta = TOTAL_REC_NUM - recNum;
            Set<Long> toBeRecommended = new HashSet<>();
            if (delta > 0) {
                int i = hotArticleRecommend.size() > delta ? delta : hotArticleRecommend.size();
                while (i-- > 0) {
                    toBeRecommended.add(hotArticleRecommend.get(i));
                }
            }
            recomCommonUtil.filterRecomArticles(toBeRecommended, uid);
            recomCommonUtil.filterBrowsedArticles(toBeRecommended, uid);
            recomCommonUtil.insertRecommend(uid, toBeRecommended.iterator(),  RecommendAlgorithm.HR);
            count+=toBeRecommended.size();
        }
        System.out.println("HR has contributed " + (uidList.size()==0 ? 0 : (double)count/uidList.size()) + " recommending articles on average");
        System.out.println("HR end at "+new Date());
    }
}
