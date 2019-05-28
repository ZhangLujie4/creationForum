package com.zlj.forum.recom.ContentBasedRecommend;

import com.zlj.forum.common.utils.JsonConvertUtil;
import com.zlj.forum.common.utils.TimeFormatUtil;
import com.zlj.forum.recom.RecomCommonUtil;
import com.zlj.forum.web.dataobject.ArticleDetailDO;
import com.zlj.forum.web.dataobject.ArticleLogsDO;
import com.zlj.forum.web.dataobject.UsersDO;
import com.zlj.forum.web.mapper.ArticleDetailMapper;
import com.zlj.forum.web.mapper.ArticleLogsMapper;
import com.zlj.forum.web.mapper.UsersMapper;
import lombok.extern.slf4j.Slf4j;
import org.ansj.app.keyword.Keyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhanglujie
 * @description 更新用户喜好关键词列表
 * @date 2019-05-27 14:59
 */

@Slf4j
@Service
public class UserPrefRefresher {

    // 设置TF/IDF提取的关键词数量
    private static final int KEY_WORDS_NUM = 10;

    //每日衰减系数
    private static final double DEC_COEE = 0.7;

    @Autowired
    private ArticleLogsMapper articleLogsMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private ArticleDetailMapper articleDetailMapper;

    @Autowired
    private RecomCommonUtil recomCommonUtil;

    public void refresh(List<Long> uidList){
        //首先对用户的喜好关键词列表进行衰减更新
        autoDecRefresh(uidList);

        //用户浏览文章纪录
        HashMap<Long, ArrayList<Long>> userBrowsedMap = getTodayHistoryMap();
        //如果前一天没有浏览记录，则不需要执行后续更新步骤
        if(userBrowsedMap.size()==0) {
            return;
        }

        //用户喜好关键词列表,只针对当天有浏览记录的用户
        HashMap<Long, CustomizedHashMap<String,Double>> userPrefListMap = recomCommonUtil.getUserPrefListMap(userBrowsedMap.keySet());
        //文章对应关键词列表：articleTFIDFMap:<Long(aid),List<Keyword>>
        HashMap<Long, List<Keyword>> articleTFIDFMap = getArticleTFIDFMap(0);

        //开始遍历用户浏览记录，更新用户喜好关键词列表
        //对每个用户（外层循环），循环他所看过的每条新闻（内层循环），对每个新闻，更新它的关键词列表到用户的对应模块中
        Iterator<Long> ite=userBrowsedMap.keySet().iterator();


        while(ite.hasNext()) {
            Long userId = ite.next();
            ArrayList<Long> articleList=userBrowsedMap.get(userId);
            // 获取用户喜好关键词map
            CustomizedHashMap<String,Double> rateMap = userPrefListMap.get(userId);
            for (Long aid : articleList) {
                //获得文章的（关键词：TF/IDF值）map
                List<Keyword> keywordList = articleTFIDFMap.get(aid);
                Iterator<Keyword> keywordIte=keywordList.iterator();
                while(keywordIte.hasNext()){
                    Keyword keyword=keywordIte.next();
                    String name=keyword.getName();
                    if (rateMap.containsKey(name)) {
                        rateMap.put(name, rateMap.get(name) + keyword.getScore());
                    } else {
                        rateMap.put(name,keyword.getScore());
                    }
                }
            }
            userPrefListMap.put(userId, rateMap);
        }

        Iterator<Long> iterator = userBrowsedMap.keySet().iterator();
        while(iterator.hasNext()){
            Long userId=iterator.next();
            try {
                usersMapper.updatePrefList(userPrefListMap.get(userId).toString(), userId);
            } catch (Exception e) {
                log.error("userId = {}关键词更新失败", userId);
            }
        }

    }

    /**
     * 所有用户的喜好关键词列表TFIDF值随时间进行自动衰减更新
     */
    public void autoDecRefresh(List<Long> uidList){
        try {
            if (CollectionUtils.isEmpty(uidList)) {
                return;
            }
            List<UsersDO> userList = usersMapper.selectUsers(uidList);
            // 删除喜好值过低的关键词
            ArrayList<String> keywordToDelete = new ArrayList<String>();
            for (UsersDO user : userList) {

                CustomizedHashMap<String, Double> map = JsonConvertUtil.json2Object(StringUtils.isEmpty(user.getPrefList()) ? "{}" : user.getPrefList());
                if (!map.toString().equals("{}")) {
                    Iterator<String> iterator = map.keySet().iterator();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        double result = map.get(key) * DEC_COEE;
                        // 乘衰减系数之后小于阀值,将用户喜好关键词删除
                        if (result < 10) {
                            keywordToDelete.add(key);
                        }
                        // 记录衰减过后的值
                        map.put(key, result);
                    }
                }
                // 删除记录的小于阀值的key
                for (String deleteKey : keywordToDelete) {
                    map.remove(deleteKey);
                }
                keywordToDelete.clear();
                usersMapper.updatePrefList(map.toString(), user.getUid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当天有浏览记录的用户及其对应文章id，一对多的关系
     * @return
     */
    private HashMap<Long, ArrayList<Long>> getTodayHistoryMap() {
        HashMap<Long, ArrayList<Long>> historyMap = new HashMap<>();
        List<ArticleLogsDO> articleLogsDOS = articleLogsMapper.selectUserLogs(TimeFormatUtil.getSpecificDayFormat(0));
        try {
            for (ArticleLogsDO articleLogsDO : articleLogsDOS) {
                if (historyMap.containsKey(articleLogsDO.getUid())) {
                    historyMap.get(articleLogsDO.getUid()).add(articleLogsDO.getAid());
                } else {
                    historyMap.put(articleLogsDO.getUid(), new ArrayList<>());
                    historyMap.get(articleLogsDO.getUid()).add(articleLogsDO.getAid());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return historyMap;
    }

    /**
     * 将所有当天被浏览过的新闻提取出来，以便进行TFIDF求值操作，以及对用户喜好关键词列表的更新。
     * @return
     */
    public HashMap<Long, List<Keyword>> getArticleTFIDFMap(int beforeDays) {
        HashMap<Long, List<Keyword>> articleTFIDFMap = new HashMap<>();
        try {
            List<ArticleLogsDO> articleLogsDOS = articleLogsMapper.selectUserLogs(TimeFormatUtil.getSpecificDayFormat(beforeDays));
            List<ArticleDetailDO> detailDOS = articleDetailMapper.selectArticles(
                    articleLogsDOS.stream().map(articleLogsDO -> articleLogsDO.getAid()).collect(Collectors.toList())
            );
            for (ArticleDetailDO detailDO : detailDOS) {
                articleTFIDFMap.put(detailDO.getAid(), TFIDF.getTFIDE(detailDO.getTitle(), detailDO.getContent(), KEY_WORDS_NUM));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return articleTFIDFMap;
    }
}
