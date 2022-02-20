package com.example.rootdeng.esSearch.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @ClassName: News
 * @version: 1.0.0
 * @date: 21-8-7 下午5:36
 * @author: hadoop
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(indexName = "news", type="_doc", shards = 3, replicas = 1)
public class News {
    @Id
    private Long id;

    @Field(type = FieldType.Auto, index = false)
    private String newsId;

    @Field
    private Integer categoryCode;

    @Field(type = FieldType.Auto)
    private String categoryName;

    @Field(type = FieldType.Auto, analyzer = "ik_max_word")
    private String title;

    @Field(type = FieldType.Auto)
    private String suggestTitle;

    public News(Long id, String newsId, Integer categoryCode, String categoryName, String title) {
        this.id = id;
        this.newsId = newsId;
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.title = title;
    }
}
