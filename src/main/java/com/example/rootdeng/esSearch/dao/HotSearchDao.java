package com.example.rootdeng.esSearch.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName: HotSearchDao
 * @version: 1.0.0
 * @date: 21-8-7 下午1:59
 * @author: hadoop
 */
@Mapper
public interface HotSearchDao {
    //插入数据
    int insertSearch(String host, String userAgent, String content);

    //热搜
    List<String> hotWordCount();
}
