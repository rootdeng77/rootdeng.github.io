package com.example.rootdeng.esSearch.service;

import com.example.rootdeng.esSearch.model.News;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Component;

/**
 * @ClassName: newsService
 * @version: 1.0.0
 * @date: 21-8-2 下午11:20
 * @author: hadoop
 */
@Component
public interface NewsService {
    PageInfo findAllNews(News news,Integer pageNum, Integer pageSize);
    int insertNews(News news) throws Exception;
    void createMapping();
}
