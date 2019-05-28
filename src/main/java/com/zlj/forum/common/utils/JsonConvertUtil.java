package com.zlj.forum.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zlj.forum.recom.ContentBasedRecommend.CustomizedHashMap;

/**
 * @author zhanglujie
 * @description json转换工具类
 * @date 2019-05-27 15:52
 */
public class JsonConvertUtil {

    /**
     * 将用户的喜好关键词列表字符串转换为map
     * @param srcJson
     * @return
     */
    public static CustomizedHashMap<String,Double> json2Object(String srcJson){
        CustomizedHashMap<String,Double> map=null;
        try {
            map = JSON.parseObject(srcJson, new TypeReference<CustomizedHashMap<String,Double>>(){});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
