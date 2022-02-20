package com.example.rootdeng.esSearch.service.impl;

import com.example.rootdeng.esSearch.dao.HotSearchDao;
import com.example.rootdeng.esSearch.service.HotSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName: HotSearchServiceImpl
 * @version: 1.0.0
 * @date: 21-8-7 下午1:13
 * @author: hadoop
 */
@Service
public class HotSearchServiceImpl implements HotSearchService {
    @Autowired
    HotSearchDao hotSearchDao;

    @Override
    public List<String> insertHotSearch(HttpServletRequest request, String content) {
        if(StringUtils.isEmpty(content)) {
            return null;
        }
        String host = request.getHeader("Host");
        String userAgent = request.getHeader("User-Agent");
        hotSearchDao.insertSearch(host,userAgent,content);
        return null;
    }

    @Override
    public List<String> hotWordCount() {
        return hotSearchDao.hotWordCount();
    }
}
