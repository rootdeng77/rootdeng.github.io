package com.example.rootdeng.esSearch.util;

import com.alibaba.fastjson.JSON;
import com.example.rootdeng.esSearch.mapping.News;
import com.example.rootdeng.esSearch.service.NewsESService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: SearchUtil
 * @version: 1.0.0
 * @date: 21-8-8 下午11:38
 * @author: hadoop
 */
@Component
public class SearchUtil {
    @Autowired
    private NewsESService newsESService;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    /**
     * 匹配查询
     * @param title
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo termQueryTitle(String title, Integer pageNum, Integer pageSize) {
        Page page = PageUtil.pageParams(pageNum, pageSize);
        PageRequest pageRequest = PageRequest.of(page.getPageNum(), page.getPageSize());
        sublement(title);

        if(StringUtils.isEmpty(title)) {
            return getMatchAllTitle(pageRequest);
        } else {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", title);
            return new PageInfo<>(newsESService.search(termQueryBuilder,pageRequest).getContent(),5);
        }
    }

    /**
     * 模糊查询
     * @param title
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo fuzzyQueryTitle(String title, Integer pageNum, Integer pageSize) {
        Page page = PageUtil.pageParams(pageNum, pageSize);
        PageRequest pageRequest = PageRequest.of(page.getPageNum(), page.getPageSize());
        sublement(title);
        if(StringUtils.isEmpty(title)) {
            return getMatchAllTitle(pageRequest);
        } else {
            FuzzyQueryBuilder fuzzyQueryBuilder = QueryBuilders.fuzzyQuery("title", title).fuzziness(Fuzziness.ONE);
            return createResultPage(newsESService.search(fuzzyQueryBuilder,pageRequest), 3);
        }
    }

    /**
     * 将查询结果转化为pageInfo
     * @param newsPage
     * @param navigatePages
     * @return
     */
    private PageInfo createResultPage(org.springframework.data.domain.Page newsPage, int navigatePages) {
        PageInfo pageInfo = new PageInfo();
        int pageNum = newsPage.getPageable().getPageNumber();
        pageInfo.setList(newsPage.getContent());
        pageInfo.setNavigatepageNums(calcNavigatepageNums(newsPage, navigatePages));
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(newsPage.getPageable().getPageSize());
        pageInfo.setTotal(newsPage.getTotalElements());
        pageInfo.setPrePage(pageNum == 0 ? 1 : pageNum-1);
        pageInfo.setEndRow(newsPage.getTotalPages());
        pageInfo.setStartRow(1);
        pageInfo.setNextPage(pageNum == newsPage.getTotalPages()  ? pageNum - 1: pageNum +1);
        System.out.println(JSON.toJSONString(pageInfo, true));
        return pageInfo;
    }

    /**
     * 获取全部数据
     * @param pageRequest
     * @return
     */
    private PageInfo getMatchAllTitle(PageRequest pageRequest) {
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        return createResultPage(newsESService.search(matchAllQueryBuilder, pageRequest), 3);
    }

    /**
     * 计算翻页叔祖
     * @param page
     * @param navigatePages
     * @return
     */
    private int[] calcNavigatepageNums(org.springframework.data.domain.Page page, int navigatePages) {
        int[] navigatepageNums;
        int i;
        if (page.getTotalPages() <= navigatePages) {
            navigatepageNums = new int[page.getTotalPages()];
            for(i = 0; i < page.getTotalPages(); i++) {
                navigatepageNums[i] = i+1;
            }
        } else {
            int pageNum = page.getPageable().getPageNumber();
            int pages = page.getTotalPages();
            navigatepageNums = new int[navigatePages];
            i = pageNum - navigatePages / 2;
            int j;
            int endNum = pageNum + navigatePages / 2;
            if (i < 1) {
                i = 1;
                for(j = 0; j < navigatePages; ++j) {
                    navigatepageNums[j] = i++;
                }
            } else if (endNum > pages) {
                endNum = pages;

                for(j = navigatePages - 1; j >= 0; --j) {
                    navigatepageNums[j] = endNum--;
                }
            } else {
                for(j = 0; j < navigatePages; ++j) {
                    navigatepageNums[j] = i++;
                }
            }
        }
        return navigatepageNums;
    }

    /**
     * 建议搜索
     * @param title
     * @return
     */
    public List<String> sublement(String title) {
        if(StringUtils.isEmpty(title)) {
            return null;
        }

        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", title);
        List<String> sublementList = new ArrayList<>();
        newsESService.search(matchQueryBuilder).forEach(s->{
            System.out.println("s --> " + s);
        });
        newsESService.search(matchQueryBuilder).iterator().forEachRemaining(s -> {
            System.out.println(s.getTitle());
        //    sublementList.add(s.getTitle());
        });
        return sublementList;
    }

    public List<String> suggestQueryTitle(String title, Integer pageNum, Integer pageSize) {
        Page page = PageUtil.pageParams(
                pageNum, pageSize);
    //    PageRequest pageRequest = PageRequest.of(page.getPageNum(), page.getPageSize());


        if(StringUtils.isEmpty(title)) {
            return null;
        } else {
            CompletionSuggestionBuilder suggestionBuilder = SuggestBuilders.completionSuggestion("suggestTitle")
                    .prefix(title)
                    .skipDuplicates(true)
                    .size(5);
            SuggestBuilder suggestBuilder = new SuggestBuilder();
            suggestBuilder.addSuggestion("title-suggest", suggestionBuilder);
            IndexCoordinates indexCoordinates = elasticsearchOperations.getIndexCoordinatesFor(News.class);
            //查询
            SearchResponse response = elasticsearchRestTemplate.suggest(suggestBuilder, indexCoordinates);
            Suggest.Suggestion<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>> goodsNameSuggest = response
                    .getSuggest().getSuggestion("title-suggest");

            // 处理返回
            List<String> suggests = goodsNameSuggest.getEntries().stream().map(x -> x.getOptions().stream().map(y->y.getText().toString()).collect(Collectors.toList())).findFirst().get();
            // 输出内容
            for (String suggest : suggests) {
                System.out.println("suggest = " + suggest);
            }
            return suggests;
        }
    }
}