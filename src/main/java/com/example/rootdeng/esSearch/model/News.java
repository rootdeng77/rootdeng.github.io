package com.example.rootdeng.esSearch.model;

import lombok.*;

import java.util.List;

/**
 * @ClassName: news
 * @version: 1.0.0
 * @date: 21-8-3 下午11:15
 * @author: hadoop
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class News {
    public static final Integer FAILURE = 0;
    public static final Integer SUCCESS = 1;

    private String id;
    private Integer categoryCode;
    private String categoryName;
    private String title;
    private List<String> suggestTitle;
}