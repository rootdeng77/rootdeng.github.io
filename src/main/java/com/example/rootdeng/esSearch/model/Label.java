package com.example.rootdeng.esSearch.model;

import lombok.*;

/**
 * @ClassName: label
 * @version: 1.0.0
 * @date: 21-8-3 下午11:18
 * @author: hadoop
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Label {
    private Integer id;
    private String newsId;
    private String label;
}
