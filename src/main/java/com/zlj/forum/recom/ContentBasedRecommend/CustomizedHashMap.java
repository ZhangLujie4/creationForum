package com.zlj.forum.recom.ContentBasedRecommend;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-27 15:45
 */
public class CustomizedHashMap<K, V> extends HashMap<K, V> {

    @Override
    public String toString(){
        String toString="{";
        Iterator<K> keyIte=this.keySet().iterator();
        while(keyIte.hasNext()){
            K key=keyIte.next();
            toString+="\""+key+"\":"+this.get(key)+",";
        }
        if(toString.equals("{")){
            toString="{}";
        }
        else{
            toString=toString.substring(0, toString.length()-1)+"}";
        }
        return toString;

    }
}
