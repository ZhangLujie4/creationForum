package com.zlj.forum.recom.ContentBasedRecommend;

import com.zlj.forum.recom.RecomCommonUtil;
import com.zlj.forum.recom.RecommendAlgorithm;
import com.zlj.forum.web.mapper.ArticleDetailMapper;
import lombok.extern.slf4j.Slf4j;
import org.ansj.app.keyword.Keyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author zhanglujie
 * @description 提取文章的关键词列表（tf-idf），与每个用户的喜好关键词列表，做关键词相似度计算，取最相似的N篇文章推荐给用户。
 * @date 2019-05-27 13:21
 */

@Slf4j
@Service
public class ContentBasedRecommender implements RecommendAlgorithm {

    // 每日CB推送上限
    private static final int N = 8;

    @Autowired
    private RecomCommonUtil recomCommonUtil;

    @Autowired
    private UserPrefRefresher userPrefRefresher;

    @Autowired
    private ArticleDetailMapper articleDetailMapper;

    @Override
    public void recommend(List<Long> uidList) {
        int count = 0;
        System.out.println("CB start at "+ new Date());
        userPrefRefresher.refresh(uidList);
        // 文章:关键词列表 map
        HashMap<Long, List<Keyword>> articleKeyWordsMap = new HashMap<>();
        // 用户喜好map uid => (<keyword, score>)
        HashMap<Long, CustomizedHashMap<String, Double>> userPrefListMap = recomCommonUtil.getUserPrefListMap(uidList);
        // 三十天内的最新文章（推荐从这里面选取）
        articleKeyWordsMap = userPrefRefresher.getArticleTFIDFMap(-30);

        for (Long userId : uidList) {
            Map<Long, Double> tempMatchMap = new HashMap<>();
            Iterator<Long> ite = articleKeyWordsMap.keySet().iterator();
            while (ite.hasNext()) {
                Long aid = ite.next();
                tempMatchMap.put(aid, getMatchValue(userPrefListMap.get(userId), articleKeyWordsMap.get(aid)));
            }
            // 去除匹配值为空的项目
            removeZeroItem(tempMatchMap);
            if (!(tempMatchMap.toString().equals("{}"))) {
                tempMatchMap = sortMapByValue(tempMatchMap);
                Set<Long> toBeRecommended = tempMatchMap.keySet();
                // 过滤掉已经推荐过的文章
                recomCommonUtil.filterRecomArticles(toBeRecommended, userId);
                // 过滤掉用户已经看过的文章
                recomCommonUtil.filterBrowsedArticles(toBeRecommended, userId);
                // 如果推荐的文章数超过上限，则截取N个
                if (toBeRecommended.size() > N) {
                    recomCommonUtil.removeOverArticles(toBeRecommended,N);
                }
                recomCommonUtil.insertRecommend(userId, toBeRecommended.iterator(),RecommendAlgorithm.CB);
                count += toBeRecommended.size();
            }
        }
        System.out.println("CB has contributed " + ((double)count/uidList.size()) + " recommending articles on average");
        System.out.println("CB finished at "+new Date());
    }

    /**
     * 获得用户的关键词列表和文章关键词列表的匹配程度
     * @return
     */
    private double getMatchValue(CustomizedHashMap<String, Double> map, List<Keyword> list) {
        Set<String> keywordsSet = map.keySet();
        double matchValue = 0;
        String str = "";
        for (Keyword keyword : list) {
            if (keywordsSet.contains(keyword.getName())) {
                str += (keyword.getName() + " ");
                matchValue += keyword.getScore() * map.get(keyword.getName());
            }
        }

        log.info("匹配到关键词：{}", str);
        return matchValue;
    }

    private void removeZeroItem(Map<Long, Double> map)
    {
        HashSet<Long> toBeDeleteItemSet = new HashSet<>();
        Iterator<Long> ite = map.keySet().iterator();
        while (ite.hasNext()) {
            Long aid = ite.next();
            if (map.get(aid) <= 0) {
                toBeDeleteItemSet.add(aid);
            }
        }
        for (Long item : toBeDeleteItemSet)
        {
            map.remove(item);
        }
    }

    /**
     * 使用 Map按value进行排序
     * @param map
     * @return
     */
    public static Map<Long, Double> sortMapByValue(Map<Long, Double> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<Long, Double> sortedMap = new LinkedHashMap<>();
        List<Map.Entry<Long, Double>> entryList = new ArrayList<>(map.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<Long, Double>>() {
            @Override
            public int compare(Map.Entry<Long, Double> o1, Map.Entry<Long, Double> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        Iterator<Map.Entry<Long, Double>> iter = entryList.iterator();
        Map.Entry<Long, Double> tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }

}
