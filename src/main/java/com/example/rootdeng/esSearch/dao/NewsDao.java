package com.example.rootdeng.esSearch.dao;

import com.example.rootdeng.esSearch.model.News;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: newsDao
 * @version: 1.0.0
 * @date: 21-8-3 下午11:15
 * @author: hadoop
 */
@Mapper
public interface NewsDao {
    List<News> findAllNews(@Param("news") News news);

    int insertNews(@Param("news") News news);

    int insertNewsLabel(@Param("newsId") String newsId, @Param("labels") List<String> labels);
}
