package com.zlj.forum.recom;

import com.zlj.forum.common.utils.JsonConvertUtil;
import com.zlj.forum.common.utils.TimeFormatUtil;
import com.zlj.forum.recom.ContentBasedRecommend.CustomizedHashMap;
import com.zlj.forum.web.dao.RecommendActionsJpaDAO;
import com.zlj.forum.web.dataobject.ArticleLogsDO;
import com.zlj.forum.web.dataobject.RecommendActionsDO;
import com.zlj.forum.web.dataobject.UsersDO;
import com.zlj.forum.web.mapper.ArticleLogsMapper;
import com.zlj.forum.web.mapper.RecommendActionsMapper;
import com.zlj.forum.web.mapper.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author zhanglujie
 * @description 常用推荐方法
 * @date 2019-05-27 16:37
 */

@Service
public class RecomCommonUtil {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private RecommendActionsJpaDAO recommendActionsJpaDAO;

    @Autowired
    private RecommendActionsMapper recommendActionsMapper;

    @Autowired
    private ArticleLogsMapper articleLogsMapper;

    public HashMap<Long, CustomizedHashMap<String, Double>> getUserPrefListMap(Collection<Long> uidList) {
        HashMap<Long, CustomizedHashMap<String, Double>> userPrefListMap = null;
        try {
            if (!uidList.isEmpty()) {
                List<UsersDO> usersDOList = usersMapper.selectUsers(uidList);
                userPrefListMap = new HashMap<>();
                for (UsersDO usersDO : usersDOList) {
                    userPrefListMap.put(usersDO.getUid(), JsonConvertUtil.json2Object(StringUtils.isEmpty(usersDO.getPrefList()) ? "{}" : usersDO.getPrefList()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userPrefListMap;
    }

    /**
     * 过滤掉用户推荐过的
     * @param col
     * @param uid
     */
    public void filterRecomArticles(Collection<Long> col, Long uid) {
        try {
            //但凡近期已经给用户推荐过的新闻，都过滤掉
            List<RecommendActionsDO> recommendationList = recommendActionsMapper.getHistoryRecom(uid, TimeFormatUtil.getSpecificDayFormat(-30));
            for (RecommendActionsDO recommendation : recommendationList) {
                if (col.contains(recommendation.getAid())) {
                    col.remove(recommendation.getAid());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 过滤掉用户浏览过的
     * @param col
     * @param uid
     */
    public void filterBrowsedArticles(Collection<Long> col, Long uid) {
        try {
            List<ArticleLogsDO> articleLogsDOS = articleLogsMapper.selectSingleUserLog(uid);
            for (ArticleLogsDO articleLogsDO : articleLogsDOS) {
                if (col.contains(articleLogsDO.getAid())) {
                    col.remove(articleLogsDO.getAid());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 去除数量上超过为算法设置的推荐结果上限值的推荐结果
     * @param set
     * @param N
     * @return
     */
    public void removeOverArticles(Set<Long> set, int N) {
        int i = 0;
        Iterator<Long> ite = set.iterator();
        while (ite.hasNext()) {
            if (i >= N) {
                ite.remove();
                ite.next();
            } else {
                ite.next();
            }
            i++;
        }
    }

    public void insertRecommend(Long uid, Iterator<Long> articleIte, int recAlgo) {
        try {
            while (articleIte.hasNext()) {
                RecommendActionsDO rec = new RecommendActionsDO();
                rec.setUid(uid);
                rec.setDeriveAlgorithm(recAlgo);
                rec.setAid(articleIte.next());
                rec.setDeriveAlgorithm(recAlgo);
                rec.setDeriveTime(new Date());
                rec.setFeedback(0);
                recommendActionsJpaDAO.save(rec);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
