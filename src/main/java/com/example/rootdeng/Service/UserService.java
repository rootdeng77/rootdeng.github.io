package com.example.rootdeng.Service;

import com.example.rootdeng.Model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserService {
    int insertUser(User user);
    User getUser(int id);
    List<User> getUsers();
    List<User> getProUser(int id);
    //获取用户渠道趋势分析维
    List<String> getVAreaChannelDim();

    //用户渠道趋势分析
    List<VAreaChannel> getVAreaChannel(String dim);
    List<VAreaChannel> getVAreaChannelAll();

    //获取用户请求方式对比情况维度
    List<String> getVAreaRequestTypeDim();
    //用户请求方式对比情况
    List<VAreaRequesttype> getVAreaRequestType(String dim);

    List<VAreaRequesttype> getVAreaRequestTypeAll();

    //用户渠道饼图情况
    List<VChannelno> getVChannelno();
    List<VUserLogin> getVUserLogin();
    List<VUserDetail> getDetail(String areaname, String channelname);
}
