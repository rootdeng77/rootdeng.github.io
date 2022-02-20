package com.example.rootdeng.esSearch.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.rootdeng.esSearch.dao.NewsDao;
import com.example.rootdeng.esSearch.model.News;
import com.example.rootdeng.esSearch.service.NewsESService;
import com.example.rootdeng.esSearch.service.NewsService;
import com.example.rootdeng.esSearch.util.PageUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @ClassName: newsServiceImpl
 * @version: 1.0.0
 * @date: 21-8-2 下午11:20
 * @author: hadoop
 */

@Service
public class NewsServiceImpl implements NewsService {
    @Autowired
    private NewsDao newsDao;
    @Autowired
    private NewsESService newsESService;

    @Override
    public PageInfo findAllNews(News news,Integer pageNum, Integer pageSize) {
        Page page = PageUtil.pageParams(pageNum, pageSize);
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return new PageInfo<>(newsDao.findAllNews(news),3);
    }

//    @Override
//    public int insertNews(News news) {
//        long mark = System.currentTimeMillis();
//        long _id= 0;
//        System.out.println("time:" + mark +":" + JSON.toJSONString(news));
//        try{
//            newsDao.insertNews(news);
//            System.out.println("time:" + mark +":" + JSON.toJSONString(news));
//            if(!CollectionUtils.isEmpty(news.getSuggestTitle())) {
//                newsDao.insertNewsLabel(news.getId(), news.getSuggestTitle());
//            }
//            _id = Long.parseLong(news.getId());
//        } catch (Exception e){
//            System.out.println("error");
//        }
//        if(_id == 0) {
//            return News.FAILURE;
//        } else {
//            com.example.rootdeng.esSearch.mapping.News msg =
//                    new com.example.rootdeng.esSearch.mapping.News(_id,news.getId(),news.getCategoryCode(),news.getCategoryName(),news.getTitle());
////            if(news.getSuggestTitle() == null) {
////                msg.setSuggestTitle(null);
////            } else {
////                msg.setSuggestTitle(news.getSuggestTitle());
////            }
////            System.out.println(" end time:" + mark +":" + JSON.toJSONString(msg));
//            newsESService.save(msg);
//        }
//        return News.SUCCESS;
//    }

    @Override
    public void createMapping() {
        List<News> newsList = newsDao.findAllNews(new News());
        for(int i=0;i<newsList.size();i++) {
            com.example.rootdeng.esSearch.mapping.News msg =
                    new com.example.rootdeng.esSearch.mapping.News((long) i,newsList.get(i).getId(),newsList.get(i).getCategoryCode(),newsList.get(i).getCategoryName(),newsList.get(i).getTitle());
            msg.setSuggestTitle(newsList.get(i).getTitle());
            newsESService.save(msg);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertNews(News news) throws Exception {
        long mark = System.currentTimeMillis();
        System.out.println("time:" + mark +":" + JSON.toJSONString(news));
        newsDao.insertNews(news);
        insertLabel(news);
        return News.SUCCESS;
    }

    private void insertLabel(News news) throws Exception {
        if(!CollectionUtils.isEmpty(news.getSuggestTitle())) {
            newsDao.insertNewsLabel(news.getId(), news.getSuggestTitle());
        } else {
            throw new Exception("输入参数错误");
        }
    }
}
