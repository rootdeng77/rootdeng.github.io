package com.example.rootdeng.config;

import lombok.Data;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.stereotype.Component;

/**
 * @ClassName: ElasticSearchConfig
 * @version: 1.0.0
 * @date: 21-8-8 下午4:32
 * @author: hadoop
 */
@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {
    private String host;
    private Integer port;

    @Override
    public RestHighLevelClient elasticsearchClient() {
        RestClientBuilder builder = RestClient.builder(new HttpHost(host, port));
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(builder);
        return restHighLevelClient;
    }
}