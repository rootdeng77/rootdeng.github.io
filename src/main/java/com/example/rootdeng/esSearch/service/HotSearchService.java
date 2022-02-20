package com.example.rootdeng.esSearch.service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName: HotSearchService
 * @version: 1.0.0
 * @date: 21-8-7 下午1:12
 * @author: hadoop
 */
public interface HotSearchService {
    List<String> insertHotSearch(HttpServletRequest request, String content);

    //热搜
    List<String> hotWordCount();
}
