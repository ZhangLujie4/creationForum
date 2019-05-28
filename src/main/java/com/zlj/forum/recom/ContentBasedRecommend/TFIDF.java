package com.zlj.forum.recom.ContentBasedRecommend;

import java.util.List;
import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Result;
import org.ansj.splitWord.analysis.ToAnalysis;

/**
 * @author zhanglujie
 * @description ansj的tf-idf方法应用
 * @date 2019-05-27 17:30
 */
public class TFIDF
{
    public static Result split(String text)
    {
        return ToAnalysis.parse(text);
    }

    /**
     *
     * @param title 文本标题
     * @param content 文本内容
     * @param keyNums 返回的关键词数目
     * @return
     */
    public static List<Keyword> getTFIDE(String title, String content, int keyNums)
    {
        KeyWordComputer kwc = new KeyWordComputer(keyNums);
        return kwc.computeArticleTfidf(title, content);
    }

    /**
     *
     * @param content 文本内容
     * @param keyNums 返回的关键词数目
     * @return
     */
    public static List<Keyword> getTFIDE(String content,int keyNums)
    {
        KeyWordComputer kwc = new KeyWordComputer(keyNums);
        return kwc.computeArticleTfidf(content);
    }
}
