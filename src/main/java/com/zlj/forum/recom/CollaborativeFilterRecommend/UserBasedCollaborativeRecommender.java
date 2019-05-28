package com.zlj.forum.recom.CollaborativeFilterRecommend;

import com.zlj.forum.common.utils.TimeFormatUtil;
import com.zlj.forum.recom.RecomCommonUtil;
import com.zlj.forum.recom.RecommendAlgorithm;
import com.zlj.forum.web.dataobject.ArticleLogsDO;
import com.zlj.forum.web.mapper.ArticleLogsMapper;
import com.zlj.forum.web.mapper.UsersMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.util.*;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-27 13:20
 */

@Slf4j
@Service
public class UserBasedCollaborativeRecommender implements RecommendAlgorithm {

    // 每个近邻用户推荐文章数量
    private static final int N = 4;

    /**
     * 推荐时效 (为负数，当前时间+该时间)
     * 超过三十天的浏览记录就删掉
     */
    public static int beforeDays = -30;

    /**
     * 默认近邻5个用户
     */
    public static int recomUserNum = 5;

    /**
     * 开始计数的log类型
     */
    public static int minDegree = -1;

    /**
     * 最不匹配值
     */
    public static double max = 10000d;

    @Autowired
    private ArticleLogsMapper articleLogsMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private RecomCommonUtil recomCommonUtil;

    public static final HashMap<Integer, Double> scoreMap = new HashMap<>(3);

    static {
        scoreMap.put(0, 0.5d);
        scoreMap.put(1, 2d);
        scoreMap.put(2, 3d);
        scoreMap.put(3, 1d);
    }

    @Override
    public void recommend(List<Long> uidList) {

        int count = 0;
        System.out.println("CF start at "+ new Date());
        try {
            List<ArticleLogsDO> articleLogsDOS = articleLogsMapper.findAllUserLogs();

            List<String> needDeleteLogs = new ArrayList<>();
            // 移除过期的用户浏览文章行为,这些行为对计算用户相似度的作用不大
            for (ArticleLogsDO articleLogsDO : articleLogsDOS) {
                if (articleLogsDO.getViewTime().before(TimeFormatUtil.getInRecTimestamp(beforeDays))) {
                    needDeleteLogs.add(articleLogsDO.getId().toString());
                }
            }
            if (!CollectionUtils.isEmpty(needDeleteLogs)) {
                String inSql = "(" + StringUtils.join(needDeleteLogs, ',') + ")";
                articleLogsMapper.deleteLogs(inSql);
            }

            // 获取删除超过30天记录的用户记录
            List<ArticleLogsDO> logsDOS = articleLogsMapper.findAllUserLogs();
            Map<String, Set<Integer>> actionMap = new HashMap<>();
            for (ArticleLogsDO logsDO : logsDOS) {
                // 只是单纯的浏览不做推荐依据(不然推荐会受到浏览的极大牵制)
                if (logsDO.getPreferDegree() > minDegree) {
                    String key = logsDO.getUid() + "-" + logsDO.getAid();
                    if (actionMap.containsKey(key)) {
                        actionMap.get(key).add(logsDO.getPreferDegree());
                    } else {
                        actionMap.put(key, new HashSet<>());
                        actionMap.get(key).add(logsDO.getPreferDegree());
                    }
                }
            }
            HashMap<Long, HashMap<Long, Integer>> map = new HashMap<>();
            Set<Map.Entry<String, Set<Integer>>> entrySet = actionMap.entrySet();
            Iterator<Map.Entry<String, Set<Integer>>> iterator = entrySet.iterator();
            // 获取到用户文章评价得分的map
            while (iterator.hasNext()) {
                Map.Entry<String, Set<Integer>> item = iterator.next();
                String[] uidAid = item.getKey().split("-");
                if (map.containsKey(Long.valueOf(uidAid[0]))) {
                    map.get(Long.valueOf(uidAid[0])).put(Long.valueOf(uidAid[1]), item.getValue().size());
                } else {
                    map.put(Long.valueOf(uidAid[0]), new HashMap<>());
                    map.get(Long.valueOf(uidAid[0])).put(Long.valueOf(uidAid[1]), item.getValue().size());
                }
            }

            if (map.isEmpty()) {
                log.info("不存在用户的喜好关系列表");
                return;
            }

            for (Long uid1 : uidList) {
                Map<Double, Long> distances = new TreeMap<>();
                for (Long uid2 : uidList) {
                    if (!uid1.equals(uid2)) {
                        double distance = pearsonDis(map.get(uid1), map.get(uid2));
                        if (distance != max) {
                            distances.put(distance, uid2);
                        }
                    }
                }

                String matchStr = "";
                List<Long> recomUids = new ArrayList<>();
                int i = recomUserNum;
                for (Map.Entry<Double, Long> entry : distances.entrySet()) {
                    if (i-- <= 0) {
                        break;
                    }
                    recomUids.add(entry.getValue());
                    matchStr += (entry.getValue() + " ");
                }

                if (recomUids.isEmpty()) {
                    continue;
                }
                usersMapper.updateUserList(matchStr.trim(), uid1);

                for (Long rid : recomUids) {
                    Set<Long> recomAids = new HashSet<>();
                    for (Map.Entry<Long, Integer> entry : map.get(rid).entrySet()) {
                        recomAids.add(entry.getKey());
                    }
                    recomCommonUtil.filterRecomArticles(recomAids, uid1);
                    recomCommonUtil.filterBrowsedArticles(recomAids, uid1);

                    // 无可推荐文章
                    if (CollectionUtils.isEmpty(recomAids)) {
                        continue;
                    }
                    if (recomAids.size() > N) {
                        recomCommonUtil.removeOverArticles(recomAids, N);
                    }
                    recomCommonUtil.insertRecommend(uid1, recomAids.iterator(), RecommendAlgorithm.CF);
                    count += recomAids.size();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("CF has contributed " + ((double)count/uidList.size()) + " recommending articles on average");
        System.out.println("CF finish at "+new Date());
    }

    private double pearsonDis(HashMap<Long, Integer> map1, HashMap<Long, Integer> map2) {
        if (map1 == null || map2 == null) {
            return max;
        }
        int sumXY = 0;
        int sumX = 0;
        int sumY = 0;
        double sumX2 = 0;
        double sumY2 = 0;
        int n = 0;
        Iterator<Long> iterator = map1.keySet().iterator();
        while (iterator.hasNext()) {
            Long key = iterator.next();
            if (map2.containsKey(key)) {
                n += 1;
                int x = map1.get(key);
                int y = map2.get(key);
                sumXY = x * y;
                sumX = x;
                sumY = y;
                sumX2 += Math.pow(x, 2);
                sumY2 += Math.pow(y, 2);
            }
        }
        if (n == 0) {
            return max;
        }
        double denominator = Math.sqrt(sumX2 - Math.pow(sumX, 2) / n) * Math.sqrt(sumY2 - Math.pow(sumY, 2) / n);
        if (denominator == 0) {
            return 0;
        } else {
            return (sumXY - (sumX * sumY) / n) / denominator;
        }
    }



    /**
     * 用来计算
     * @param uidList
     */
    @Deprecated
    public void toRecommend(List<Long> uidList) {
        List<ArticleLogsDO> articleLogsDOS = articleLogsMapper.findAllUserLogs();

        List<String> needDeleteLogs = new ArrayList<>();
        // 移除过期的用户浏览文章行为,这些行为对计算用户相似度的作用不大
        for (ArticleLogsDO articleLogsDO : articleLogsDOS) {
            if (articleLogsDO.getViewTime().before(TimeFormatUtil.getInRecTimestamp(beforeDays))) {
                needDeleteLogs.add(articleLogsDO.getId().toString());
            }
        }
        if (!CollectionUtils.isEmpty(needDeleteLogs)) {
            String inSql = "(" + StringUtils.join(needDeleteLogs, ',') + ")";
            articleLogsMapper.deleteLogs(inSql);
        }

        // 获取用户的最新记录列表
        List<ArticleLogsDO> logsDOS = articleLogsMapper.findAllUserLogs();
        // uid => aid1,aid2,aid3   A : 3 2 4
        HashMap<Long, Integer> userArticleLength = new HashMap<>();
        // aid => uid1, uid2, uid3 文章-用户倒排表
        HashMap<Long, Set<Long>> articleUserMap = new HashMap<>();
        // uid-aid => degree, 浏览0.5/喜欢1/收藏2
        HashMap<String, Double> userScoreMap = new HashMap<>();
        // 文章id列表
        Set<Long> aids = new HashSet<>();
        // 用户id映射关系 id=>index
        Map<Long, Integer> userID = new HashMap<>();
        // id用户映射关系 index=>id
        Map<Integer, Long> idUser = new HashMap<>();
        // 用户id列表
        Set<Long> uids = new HashSet<>();
        int uidNum = 0;
        for (ArticleLogsDO logsDO : logsDOS) {
            userScoreMap.put(logsDO.getUid() + "-" + logsDO.getAid(), scoreMap.get(logsDO.getPreferDegree()));
            if (userArticleLength.containsKey(logsDO.getUid())) {
                userArticleLength.put(logsDO.getUid(), userArticleLength.get(logsDO.getUid()) + 1);
            } else {
                uids.add(logsDO.getUid());
                userID.put(logsDO.getUid(), uidNum);
                idUser.put(uidNum, logsDO.getUid());
                uidNum++;
                userArticleLength.put(logsDO.getUid(), 1);
            }
            if (articleUserMap.containsKey(logsDO.getAid())) {
                articleUserMap.get(logsDO.getAid()).add(logsDO.getUid());
            } else {
                aids.add(logsDO.getAid());
                articleUserMap.put(logsDO.getAid(), new HashSet<>());
                articleUserMap.get(logsDO.getAid()).add(logsDO.getUid());
            }
        }

        // 创建稀疏矩阵
        int[][] sparseMatrix = new int[uidNum][uidNum];
        // 计算相似度矩阵
        Set<Map.Entry<Long, Set<Long>>> entrySet = articleUserMap.entrySet();
        Iterator<Map.Entry<Long, Set<Long>>> iterator = entrySet.iterator();
        while(iterator.hasNext()) {
            Set<Long> commonUsers = iterator.next().getValue();
            for (Long uid1 : commonUsers) {
                for (Long uid2 : commonUsers) {
                    if (uid1.equals(uid2)) {
                        continue;
                    }
                    sparseMatrix[userID.get(uid1)][userID.get(uid2)] += 1;
                }
            }
        }
        // 计算用户与物品间的推荐度
        for (Long uid : uids) {
            for (Long aid : aids) {
                Set<Long> users = articleUserMap.get(aid);
                if (!users.contains(uid)) {
                    double recomDegree = 0.0;
                    for (Long uuid : users) {
                        //推荐度计算
                        recomDegree += sparseMatrix[userID.get(uid)][userID.get(uuid)]/Math.sqrt(userArticleLength.get(uid)*userArticleLength.get(uuid));
                    }
                }
            }
        }
    }
}
