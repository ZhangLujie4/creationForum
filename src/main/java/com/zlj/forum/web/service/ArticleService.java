package com.zlj.forum.web.service;

import com.alibaba.fastjson.JSONObject;
import com.zlj.forum.common.VO.ResultVO;
import com.zlj.forum.common.utils.ResultVOUtil;
import com.zlj.forum.common.utils.SecurityUtil;
import com.zlj.forum.enums.ResultEnum;
import com.zlj.forum.web.dao.*;
import com.zlj.forum.web.dataobject.*;
import com.zlj.forum.web.form.ArticleForm;
import com.zlj.forum.web.form.CommentForm;
import com.zlj.forum.web.mapper.ArticleMapper;
import com.zlj.forum.web.mapper.LikeRelationCommentMapper;
import com.zlj.forum.web.mapper.UserCommentMapper;
import com.zlj.forum.web.to.ArticleTO;
import com.zlj.forum.web.to.UserCommentTO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-20 21:04
 */

@Slf4j
@Service
public class ArticleService {

    public static String index = "article";

    public static String type = "story";

    @Autowired
    private TransportClient client;

    @Autowired
    private ArticleJpaDAO articleJpaDAO;

    @Autowired
    private UserJpaDAO userJpaDAO;

    @Autowired
    private UserExtJpaDAO userExtJpaDAO;

    @Autowired
    private MyFavoriteJpaDAO myFavoriteJpaDAO;

    @Autowired
    private LikeRelationJpaDAO likeRelationJpaDAO;

    @Autowired
    private FollowRelationJpaDAO followRelationJpaDAO;

    @Autowired
    private UserCommentJpaDAO userCommentJpaDAO;

    @Autowired
    private UserCommentMapper userCommentMapper;

    @Autowired
    private LikeRelationCommentMapper likeRelationCommentMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private AsyncCommonService asyncCommonService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 更新文档
     * @param articleForm
     * @return
     */
    public ResultVO updateArticle(ArticleForm articleForm, Long uid) {
        // 当id存在时，说明是更新操作
        ArticleDO findRes = articleJpaDAO.getOne(articleForm.getId());
        if (findRes == null) {
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(), "文章不存在");
        }
        if (!findRes.getUid().equals(uid)) {
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(), "当前操作者不是文章创建者！！！");
        }
        // es更新操作
        UpdateRequest update = new UpdateRequest(index, type, articleForm.getId() + "");
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder()
                    .startObject();
            if (articleForm.getTitle() != null) {
                findRes.setTitle(articleForm.getTitle());
                builder.field("title", articleForm.getTitle());
            }
            if (articleForm.getContent() != null) {
                builder.field("content", articleForm.getContent());
            }
            builder.field("gmt_publish", System.currentTimeMillis());
            builder.endObject();
            update.doc(builder);
        } catch (IOException e) {
            return ResultVOUtil.error(ResultEnum.ES_ERROR);
        }

        try {
            UpdateResponse result = client.update(update).get();
            findRes.setGmtUpdate(new Date());
            articleJpaDAO.save(findRes);
            return ResultVOUtil.success(result.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(ResultEnum.ES_ERROR);
        }
    }

    /**
     * 新增文档
     * @param articleForm
     * @return
     */
    public ResultVO addArticle(ArticleForm articleForm) {
        long uid = SecurityUtil.getCurrentUserId();
        ArticleDO articleDO = new ArticleDO();
        articleDO.setUid(uid);
        articleDO.setLikeNum(0L);
        articleDO.setFavoriteNum(0L);
        articleDO.setCommentNum(0L);
        if (articleForm.getTitle() != null) {
            articleDO.setTitle(articleForm.getTitle());
        }
        articleJpaDAO.save(articleDO);
        try {
            XContentBuilder story = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("title", articleForm.getTitle())
                    .field("content", articleForm.getContent())
                    .field("author", uid)
                    .field("gmt_publish", System.currentTimeMillis())
                    .endObject();
            IndexResponse result = client.prepareIndex(index, type, articleDO.getId() + "")
                    .setSource(story)
                    .get();
            return ResultVOUtil.success(result.getId());
        } catch (IOException e) {
            e.printStackTrace();
            return ResultVOUtil.error(ResultEnum.ES_ERROR);
        }
    }

    /**
     * 删除文档
     * @param id
     * @return
     */
    public ResultVO deleteArticle(long uid, String id) {
        if (StringUtils.isEmpty(id)) {
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }
        DeleteResponse result = this.client.prepareDelete(index, type, id)
                .get();

        articleJpaDAO.removeByIdAndUid(Long.valueOf(id), uid);
        return ResultVOUtil.success(result.getResult().toString());
    }

    /**
     * 获取文章信息
     * @param id
     * @return
     */
    public ResultVO getDetail(String id) {
        if (StringUtils.isEmpty(id)) {
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }
        Long aid = Long.valueOf(id);

        GetResponse result = this.client.prepareGet(index, type, id)
                .get();
        if (!result.isExists()) {
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(), "未查找到该文章");
        }
        Map<String, Object> map = result.getSource();
        long author = Long.valueOf(map.get("author").toString());
        ArticleDO articleDO = articleJpaDAO.getOne(Long.parseLong(id));
        UserDO userDO = userJpaDAO.findByUid(author);
        UserExtDO userExtDO = userExtJpaDAO.findByUid(author);
        map.put("gmt_create", articleDO.getGmtCreate().getTime());
        map.put("username", userDO.getUsername());
        map.put("likeNum", articleDO.getLikeNum());
        map.put("favoriteNum", articleDO.getFavoriteNum());
        map.put("commentNum", articleDO.getCommentNum());
        if (userExtDO != null) {
            map.put("avatar", userExtDO.getAvatar());
            map.put("nickname", userExtDO.getNickName());
            map.put("motto", userExtDO.getMotto());
        }
        Long uid = SecurityUtil.getCurrentUserId();
        if (uid != null) {
            map.put("is_self", uid.equals(author));
            map.put("like", likeRelationJpaDAO.findByAidAndUid(aid, uid) != null);
            map.put("favorite", myFavoriteJpaDAO.findByAidAndUid(aid, uid) != null);
            map.put("follow", followRelationJpaDAO.findByFansUidAndFollowedUid(uid, author) != null);
        }
        return ResultVOUtil.success(map);
    }

    /**
     * 获取评论列表
     * @param uid
     * @param id
     * @param page
     * @param size
     * @return
     */
    public ResultVO getCommentList(Long uid, String id, int page, int size) {
        if (StringUtils.isEmpty(id)) {
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }
        Long aid = Long.valueOf(id);
        long total = articleJpaDAO.getOne(aid).getCommentNum();
        List<UserCommentDO> userCommentDOList = userCommentMapper.getCommentList(aid, (page - 1) * size, size);
        List<UserCommentTO> userCommentTOS = convertComment(userCommentDOList);
        List<String> cids = new ArrayList<>();
        for (UserCommentDO userCommentDO : userCommentDOList) {
            cids.add(userCommentDO.getId().toString());
        }

        if (uid != null && !CollectionUtils.isEmpty(cids)) {
            String cidString = org.apache.tomcat.util.buf.StringUtils.join(cids, ',');
            List<Long> likeCids = likeRelationCommentMapper.getRelationList(uid, "(" + cidString + ")");
            for (UserCommentTO userCommentTO : userCommentTOS) {
                if (likeCids.contains(userCommentTO.getId())) {
                    userCommentTO.setLike(true);
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("comments", userCommentTOS);
        map.put("page", page);
        map.put("size", size);
        map.put("more", page * size < total);
        return ResultVOUtil.success(map);
    }

    /**
     * 新增评论
     * @param uid
     * @param commentForm
     * @return
     */
    public ResultVO addComment(Long uid, CommentForm commentForm) {

        UserCommentDO userCommentDO = new UserCommentDO();
        userCommentDO.setAid(commentForm.getAid());
        userCommentDO.setContent(commentForm.getContent());
        UserExtDO extDO = userExtJpaDAO.findByUid(uid);
        if (null == extDO || StringUtils.isEmpty(extDO.getNickName())) {
            userCommentDO.setNickName(SecurityUtil.getCurrentUser().getUsername());
        } else {
            userCommentDO.setNickName(extDO.getNickName());
            userCommentDO.setAvatar(extDO.getAvatar());
        }
        userCommentDO.setUid(uid);
        if (commentForm.getRid() != null && commentForm.getRid() > 0) {
            UserCommentDO commentDO = userCommentJpaDAO.getOne(commentForm.getRid());
            if (commentDO != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("uid", commentDO.getUid());
                map.put("content", commentDO.getContent());
                map.put("id", commentDO.getId());
                map.put("nickName", commentDO.getNickName());
                map.put("gmtCreate", commentDO.getGmtCreate());
                map.put("avatar", commentDO.getAvatar());
                userCommentDO.setReply(JSONObject.toJSONString(map));
            }
        }
        userCommentDO.setLikeNum(0L);
        userCommentDO.setGmtCreate(new Date());
        userCommentJpaDAO.save(userCommentDO);

        // 异步更新评论数
        asyncCommonService.addCommentNum(commentForm.getAid());
        return ResultVOUtil.success(userCommentDO);
    }

    /**
     * 获取首页文章类目
     * @param page
     * @param size
     * @return
     */
    public ResultVO getHomeList(int page, int size) {
        String cacheKey = "home_list:" + page;
        String homeString = redisTemplate.opsForValue().get(cacheKey);
        if (true){//StringUtils.isEmpty(homeString)) {
            List<ArticleDO> articleDOS = articleMapper.getHomeList((page - 1) * size, size);
            String[] aids = new String[articleDOS.size()];
            int i = 0;
            for (ArticleDO articleDO : articleDOS) {
               aids[i++] = articleDO.getId().toString();
            }
            QueryBuilder query = QueryBuilders.idsQuery(type).addIds(aids);
            SearchRequestBuilder builder = client.prepareSearch(index)
                    .setTypes(type)
                    .setQuery(query)
                    .setFrom(0)
                    .setSize(articleDOS.size());
            SearchResponse response = builder.get();

            Map<String, Object> result = new HashMap<>();
            for (SearchHit hit : response.getHits()) {
                result.put(hit.getId(), hit.getSource());
            }

            List<ArticleTO> articleTOS = new ArrayList<>();
            for (ArticleDO articleDO : articleDOS) {
                ArticleTO articleTO = new ArticleTO();
                BeanUtils.copyProperties(articleDO, articleTO);
                articleTO.setContent(result.get(articleDO.getId()+""));
                articleTOS.add(articleTO);
            }
            redisTemplate.opsForValue().set(cacheKey, JSONObject.toJSONString(articleTOS), 60*5, TimeUnit.SECONDS);
            return ResultVOUtil.success(articleTOS);
        }
        return ResultVOUtil.success(JSONObject.parse(homeString));
    }

    /**
     * 关键词搜索文章列表
     * @param keyword
     * @param page
     * @param size
     * @return
     */
    public ResultVO getListByKeyword(String keyword, int page, int size) {

        // 异步更新热搜词
        asyncCommonService.updateHotSearch(keyword);

        // es搜索文章列表
        QueryBuilder query = QueryBuilders.multiMatchQuery(keyword, "title", "content");
        FieldSortBuilder publishSort = SortBuilders.fieldSort("gmt_publish").order(SortOrder.DESC);
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field title = new HighlightBuilder.Field("title");
        HighlightBuilder.Field content = new HighlightBuilder.Field("content");
        highlightBuilder.field(title);
        highlightBuilder.field(content);
        highlightBuilder.preTags("「");
        highlightBuilder.postTags("」");
        SearchRequestBuilder builder = client.prepareSearch(index)
                .setTypes(type)
                .setQuery(query)
                .highlighter(highlightBuilder)
                .addSort(new ScoreSortBuilder())
                .addSort(publishSort)
                .setFrom((page - 1) * size)
                .setSize(size);
        SearchResponse response = builder.get();
        Map<String, Object> map = new HashMap<>();
        map.put("total", response.getHits().getTotalHits());
        List<Map<String, Object>> result = new ArrayList<>();
        for (SearchHit hit : response.getHits()) {
            Map<String, Object> item = hit.getSource();
            item.put("score", hit.getScore());
            item.put("id", hit.getId());
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField highlight = highlightFields.get("title");
            if (highlight != null) {
                Text[] fragments = highlight.fragments();
                item.put("title", fragments[0].string());
            }
            HighlightField highlight2 = highlightFields.get("content");
            if (highlight2 != null) {
                Text[] fragments = highlight2.fragments();
                item.put("content", fragments[0].string());
            }
            result.add(hit.getSource());
        }
        map.put("data", result);
        map.put("page", page);
        map.put("size", size);
        map.put("more", page * size < response.getHits().getTotalHits());
        return ResultVOUtil.success(map);
    }


    /*************************************************  私有方法  *******************************************************/

    private List<UserCommentTO> convertComment(List<UserCommentDO> userCommentDOS) {
        List<UserCommentTO> userCommentTOS = new ArrayList<>();
        for (UserCommentDO commentDO : userCommentDOS) {
            UserCommentTO commentTO = new UserCommentTO();
            commentTO.setId(commentDO.getId());
            commentTO.setLikeNum(commentDO.getLikeNum());
            commentTO.setNickName(commentDO.getNickName());
            commentTO.setReply(StringUtils.isEmpty(commentDO.getReply()) ? false : JSONObject.parse(commentDO.getReply()));
            commentTO.setUid(commentDO.getUid());
            commentTO.setGmtCreate(commentDO.getGmtCreate());
            commentTO.setContent(commentDO.getContent());
            commentTO.setAvatar(commentDO.getAvatar());
            userCommentTOS.add(commentTO);
        }
        return userCommentTOS;
    }
}
