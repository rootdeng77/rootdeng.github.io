package com.example.rootdeng.esSearch.util;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: PageUtil
 * @version: 1.0.0
 * @date: 21-8-7 上午9:49
 * @author: hadoop
 */
public class PageUtil {
    //页码信息设置
    public static Page pageParams(Integer currentPage, Integer pageSize) {
        if(currentPage == null) {
            currentPage =0;
        } else {
            currentPage = currentPage - 1;
        }
        if(pageSize == null) {
            pageSize = 10;
        }
        return new Page(currentPage,pageSize);
    }

    //返回分页
    public static List<String> getPageList(PageInfo pageInfo, Integer currentPage, Integer pageSize) {
        List<String> pageList = new ArrayList<>();
        if(currentPage == null){
            currentPage =1;
        }
        if(pageSize == null) {
            pageSize = 10;
        }
        pageList.add("1");
        if(pageInfo.getSize() == 0) {
            return pageList;
        }
        if(currentPage >3) {
            pageList.add("...");
            for(int i = currentPage-2;i<=currentPage; i++) {
                pageList.add(String.valueOf(i));
            }
        } else {
            for(int i=2;i<=currentPage;i++) {
                pageList.add(String.valueOf(i));
            }
        }
        int totalPage = pageInfo.getSize()/pageSize;
        if(pageInfo.getTotal()%pageSize !=0) totalPage++;
        if(currentPage<totalPage - 2) {
            pageList.add(String.valueOf(currentPage+1));
            pageList.add(String.valueOf(currentPage+2));
            pageList.add("...");
        } else {
            if(currentPage+2==totalPage) {
                pageList.add(String.valueOf(currentPage+1));
            }
        }
        if(currentPage != totalPage){
            pageList.add(String.valueOf(totalPage));
        }

        return pageList;
    }
}
