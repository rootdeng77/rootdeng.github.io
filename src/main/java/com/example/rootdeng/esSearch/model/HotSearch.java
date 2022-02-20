package com.example.rootdeng.esSearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName: HotSearch
 * @version: 1.0.0
 * @date: 21-8-7 下午1:08
 * @author: hadoop
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HotSearch {
    private Integer id;
    private String content;
    private String host;
    private String userAgent;
}