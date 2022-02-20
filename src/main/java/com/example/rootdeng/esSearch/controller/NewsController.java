package com.example.rootdeng.esSearch.controller;

import com.example.rootdeng.esSearch.model.News;
import com.example.rootdeng.esSearch.service.HotSearchService;
import com.example.rootdeng.esSearch.service.NewsService;
import com.example.rootdeng.esSearch.util.PageUtil;
import com.example.rootdeng.esSearch.util.SearchUtil;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName: newsController
 * @version: 1.0.0
 * @date: 21-8-2 下午11:19
 * @author: hadoop
 */
@Controller
@RequestMapping("/esSearch")
public class NewsController {
    @Autowired
    private NewsService newsService;
    @Autowired
    private HotSearchService hotSearchService;
    @Autowired
    private SearchUtil searchUtil;


    @RequestMapping("/findAllNews")
    public String findAllNews(HttpServletRequest request, News news, Integer pageNum, Integer pageSize) {
        PageInfo<News> newsPageInfo = newsService.findAllNews(news,pageNum,pageSize);
        hotSearchService.insertHotSearch(request,news.getTitle());
        List<String> pageList = PageUtil.getPageList(newsPageInfo, newsPageInfo.getPageNum(), newsPageInfo.getPageSize());
        request.setAttribute("newsPageInfo", newsPageInfo);
        request.setAttribute("pageList", pageList);
        request.setAttribute("showSize", 10-newsPageInfo.getList().size());
        request.setAttribute("hotWordList", hotSearchService.hotWordCount());
        return "ESsearch";
    }

    @RequestMapping("/createMapping")
    public void createMapping() {
        newsService.createMapping();
    }

    @RequestMapping("/findNewsESFuzzy")
    public String findAllNewsES(HttpServletRequest request, News news, Integer pageNum, Integer pageSize) {
         PageInfo<News> newsPageInfo = searchUtil.fuzzyQueryTitle(news.getTitle(),pageNum,pageSize);
        hotSearchService.insertHotSearch(request,news.getTitle());
        request.setAttribute("newsPageInfo", newsPageInfo);
        request.setAttribute("showSize", 10-newsPageInfo.getList().size());
        request.setAttribute("hotWordList", hotSearchService.hotWordCount());
        return "ESsearch";
    }

    @RequestMapping("/findNewsESTerm")
    public String findNewsESTerm(HttpServletRequest request, News news, Integer pageNum, Integer pageSize) {
        PageInfo<News> newsPageInfo = searchUtil.termQueryTitle(news.getTitle(),pageNum,pageSize);
        hotSearchService.insertHotSearch(request,news.getTitle());
        List<String> pageList = PageUtil.getPageList(newsPageInfo, newsPageInfo.getPageNum(), newsPageInfo.getPageSize());
        request.setAttribute("newsPageInfo", newsPageInfo);
        request.setAttribute("pageList", pageList);
        request.setAttribute("showSize", 10-newsPageInfo.getList().size());
        request.setAttribute("hotWordList", hotSearchService.hotWordCount());
        return "ESsearch";
    }

    @RequestMapping("/insertNew")
    public void insertNew(String line) throws Exception {
        String[] fields = line.split("_!_");
        News news = null;
        if(fields.length == 4) {
            news = new News(fields[0],Integer.parseInt(fields[1]),fields[2],fields[3], null);
        } else {
            news = new News(fields[0],Integer.parseInt(fields[1]),fields[2],fields[3], Lists.newArrayList(fields[4].split(",")));
        }
        newsService.insertNews(news);

    }
}