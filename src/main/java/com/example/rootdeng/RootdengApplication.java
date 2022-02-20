package com.example.rootdeng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;

@ServletComponentScan
@SpringBootApplication
public class RootdengApplication {

    public static void main(String[] args) {
        SpringApplication.run(RootdengApplication.class, args);
    }

    @Bean
    public MultipartConfigElement getMultiConfig() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        return factory.createMultipartConfig();
    }
}


/*
<span>世界，你好</span> 这是我的第一版个人网站，本来我想做成个人博客，后来想想目前CSDN已经够用了，于是便打算改成个人项目展示，把最近能用到的大数据程序做成一个个小项目展示出来，好歹也算一个创意吧，如果可以的话也可以一起做一个网站，大家共同学习进步吗。<br>
				PS：开发中<br>
*/
