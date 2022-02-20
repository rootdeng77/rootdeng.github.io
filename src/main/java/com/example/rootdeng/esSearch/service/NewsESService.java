package com.example.rootdeng.esSearch.service;

import com.example.rootdeng.esSearch.mapping.News;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @ClassName: NewsESService
 * @version: 1.0.0
 * @date: 21-8-7 下午5:39
 * @author: hadoop
 */
@Repository
public interface NewsESService extends ElasticsearchRepository<News, Long> {
}
