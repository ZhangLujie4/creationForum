package com.zlj.forum.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-19 22:49
 */

@RestController
@RequestMapping("/api/elastic")
@Slf4j
public class ElasticSearchDemoController {

    @Autowired
    private TransportClient client;

    @GetMapping("/article/get/{id}")
    public ResponseEntity get(@PathVariable(name = "id") String id) {
        if (StringUtils.isEmpty(id)) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        GetResponse result = this.client.prepareGet("article", "story", id)
                .get();
        if (!result.isExists()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(result.getSource(), HttpStatus.OK);
    }

    @PostMapping("/article/add")
    public ResponseEntity add(@RequestParam(name = "title") String title,
                              @RequestParam(name = "author") String author,
                              @RequestParam(name = "content") String content,
                              @RequestParam(name = "publish_date")
                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date publish_date) {
        try {
            XContentBuilder story = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("title", title)
                    .field("author", author)
                    .field("content", content)
                    .field("publish_date", publish_date.getTime())
                    .endObject();
            IndexResponse result = this.client.prepareIndex("article", "story")
                    .setSource(story)
                    .get();
            return new ResponseEntity(result.getId(), HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/article/delete")
    public ResponseEntity delete(@RequestParam(value = "id") String id) {
        if (StringUtils.isEmpty(id)) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        DeleteResponse result = this.client.prepareDelete("article", "story", id)
                .get();

        return new ResponseEntity(result.getResult().toString(), HttpStatus.OK);
    }

    @PutMapping("/article/update")
    public ResponseEntity update(@RequestParam(name = "id") String id,
                                 @RequestParam(name = "title", required = false) String title,
                                 @RequestParam(name = "author", required = false) String author) {
        UpdateRequest update = new UpdateRequest("article", "story", id);
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder()
                    .startObject();
            if (title != null) {
                builder.field("title", title);
            }
            if (author != null) {
                builder.field("author", author);
            }
            builder.endObject();
            update.doc(builder);
        } catch (IOException e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            UpdateResponse result = this.client.update(update).get();
            return new ResponseEntity(result.getResult().toString(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/article/query")
    public ResponseEntity query(@RequestParam(name = "author", required = false) String author,
                                @RequestParam(name = "title", required = false) String title) {
//                                @RequestParam(name = "startTime", defaultValue = "0") Long startTime,
//                                @RequestParam(name = "endTime", required = false) Long endTime) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (author != null) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("author", author));
        }

        if (title != null) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("title", title));
        }

//        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("publish_time");
//        if (startTime != null && startTime > 0) {
//            rangeQueryBuilder.from(startTime);
//        }
//        if (endTime != null && endTime > startTime) {
//            rangeQueryBuilder.to(endTime);
//        }
//        boolQueryBuilder.filter(rangeQueryBuilder);
        SearchRequestBuilder builder = this.client.prepareSearch("article")
                .setTypes("story")
                //数量足够多的时候用 QUERY_THEN_FETCH
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(boolQueryBuilder)
                .setFrom(0)
                .setSize(10);
        System.out.println(builder);

        SearchResponse response = builder.get();
        List<Map<String, Object>> result = new ArrayList<>();
        for (SearchHit hit : response.getHits()) {
            result.add(hit.getSource());
        }
        return new ResponseEntity(result, HttpStatus.OK);
    }
}
