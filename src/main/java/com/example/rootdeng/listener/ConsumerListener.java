package com.example.rootdeng.listener;

import com.example.rootdeng.esSearch.model.News;
import com.example.rootdeng.esSearch.service.NewsService;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Base64;

/**
 * @ClassName: ConsumerListener
 * @version: 1.0.0
 * @date: 21-8-14 下午11:57
 * @author: hadoop
 */
@RocketMQMessageListener(topic = "springboot-rocketmq", consumeMode = ConsumeMode.CONCURRENTLY,
        consumerGroup = "${rocketmq.consumer.group}",selectorExpression = "news")
@Component
public class ConsumerListener  implements RocketMQListener<String> {
    @Autowired
    private NewsService newsService;

    @SneakyThrows
    @Override
    public void onMessage(String s) {
        String decodeString = "";
        try {
            decodeString = new String(Base64.getDecoder().decode(s.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("decode error");
        }
        System.out.println("接受到消息 " + decodeString);
        String[] fields = decodeString.split("_!_");
        News news = null;
        if(fields.length == 4) {
            news = new News(fields[0],Integer.parseInt(fields[1]),fields[2],fields[3], null);
        } else {
            news = new News(fields[0],Integer.parseInt(fields[1]),fields[2],fields[3], Lists.newArrayList(fields[4].split(",")));
        }
        newsService.insertNews(news);
    }
}
