package com.example.rootdeng.esSearch.service.impl;

import com.example.rootdeng.esSearch.model.News;
import com.example.rootdeng.esSearch.service.NewsESService;
import com.example.rootdeng.esSearch.service.NewsService;
import com.example.rootdeng.esSearch.util.SearchUtil;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.*;

/**
 * @ClassName: newsServiceImplTest
 * @version: 1.0.0
 * @date: 21-8-2 下午11:21
 * @author: hadoop
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class newsServiceImplTest {
    @Autowired
    NewsService newsService;
    @Autowired
    NewsESService newsESService;
    @Autowired
    SearchUtil searchUtil;

    @Test
    @Ignore
    public void createSql() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File("/media/hadoop/文档/文本测试文件夹/newText.txt")));
        BufferedWriter newsBw = new BufferedWriter(new FileWriter(new File("/media/hadoop/文档/文本测试文件夹/insertNews.txt")));
        BufferedWriter labelBw = new BufferedWriter(new FileWriter(new File("/media/hadoop/文档/文本测试文件夹/insertlabels.txt")));
        int cnt = 0;
        String line;
        while((line = br.readLine()) != null) {
            System.out.println(line);
            String[] fields = line.split("_!_");
            System.out.printf(" insert into news(id,category_code,category_name,title) value('%s',%s,'%s','%s')\n", fields[0],fields[1],fields[2],fields[3]);
            String newsStr = "insert into news(id,category_code,category_name,title) value('" + fields[0] + "'," + fields[1] + ",'" + fields[2]+ "','" + fields[3] +"');\n";
            newsBw.write(newsStr);
            System.out.println(" " +newsStr);
            if(fields.length >4) {
                String[] labels = fields[4].split(",");
                for(int i=0;i<labels.length;i++) {
                    System.out.printf("insert into news_label(id,news_id,label) value(%s,'%s','%s')\n", ++cnt, fields[0], labels[i]);
                    String labelStr = "insert into news_label(id,news_id,label) value(" + cnt + ",'" + fields[0] + "','" + labels[i] + "');\n";
                    System.out.println(labelStr);
                    labelBw.write(labelStr);
                }
            }
        }
        br.close();
        newsBw.close();
        labelBw.close();
    }

//    @Test
//    public void insertNewsTOES() throws IOException {
//        BufferedReader br = new BufferedReader(new FileReader(new File("/media/hadoop/文档/文本测试文件夹/newText.txt")));
//        int cnt = 0;
//        String line;
//        while((line = br.readLine()) != null) {
//            ++cnt;
//            if(cnt>150) break;
//            System.out.println(line);
//            String[] fields = line.split("_!_");
////            System.out.printf(" insert into news(id,category_code,category_name,title) value('%s',%s,'%s','%s')\n", fields[0],fields[1],fields[2],fields[3]);
//            News news = new News(fields[0],Integer.parseInt(fields[1]),fields[2],fields[3]);
//            newsService.insertNews(news);
//            String newsStr = "insert into news(id,category_code,category_name,title) value('" + fields[0] + "'," + fields[1] + ",'" + fields[2]+ "','" + fields[3] +"');\n";
////            System.out.println(" " +newsStr);
//            if(fields.length >4) {
//                String[] labels = fields[4].split(",");
//                for(int i=0;i<labels.length;i++) {
////                    System.out.printf("insert into news_label(id,news_id,label) value(%s,'%s','%s')\n", ++cnt, fields[0], labels[i]);
//                    String labelStr = "insert into news_label(id,news_id,label) value(" + cnt + ",'" + fields[0] + "','" + labels[i] + "');\n";
////                    System.out.println(labelStr);
//                }
//            }
//        }
//        br.close();
//    }

    @Test
    public void tttt() {
    //    searchUtil.fuzzyQueryTitle(null,null,null);
        int currentPage = 0;
        int pageSize = 5;
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize);
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", "树");
        Iterable<com.example.rootdeng.esSearch.mapping.News> products = newsESService.search(termQueryBuilder,pageRequest);
        for (com.example.rootdeng.esSearch.mapping.News product : products) {
            System.out.println(product);
        }
    }
}