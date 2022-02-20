package com.example.rootdeng.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.rootdeng.Model.VAreaChannel;
import com.example.rootdeng.Model.VAreaRequesttype;
import com.example.rootdeng.Model.VChannelno;
import com.example.rootdeng.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping("/chartss")
    public ModelAndView charts() {
        //获取用户渠道趋势分析维度
        JSONObject jsonObjectDim_AreaChannel = new JSONObject();
        List<String> channelDim = userService.getVAreaChannelDim();
        for (int i=0; i < channelDim.size();i++){
            JSONArray jsonArray = new JSONArray();
            List<VAreaChannel> vAreaChannelList = userService.getVAreaChannel(channelDim.get(i));
            for (int j =0;j<vAreaChannelList.size();j++){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("areaname",vAreaChannelList.get(j).getAreaname());
                jsonObject.put("num",vAreaChannelList.get(j).getNum());
                jsonArray.add(jsonObject);
            }
            jsonObjectDim_AreaChannel.put(channelDim.get(i),jsonArray);
        }
        //测试输出

        //用户请求方式对比情况维度
        JSONObject jsonObjectDim_AreaRequestType = new JSONObject();
        List<String> requestTypeDim = userService.getVAreaRequestTypeDim();
        for (int i=0; i < requestTypeDim.size();i++){
            JSONArray jsonArray = new JSONArray();
            List<VAreaRequesttype> vAreaRequesttypeList = userService.getVAreaRequestType(requestTypeDim.get(i));
            for (int j =0;j<vAreaRequesttypeList.size();j++){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("areaname",vAreaRequesttypeList.get(j).getAreaname());
                jsonObject.put("num",vAreaRequesttypeList.get(j).getNum());
                jsonArray.add(jsonObject);
            }
            jsonObjectDim_AreaRequestType.put(requestTypeDim.get(i),jsonArray);
        }
        //用户渠道饼图情况
        List<VChannelno> channelnoList = userService.getVChannelno();

        //用户渠道趋势分析
        ModelAndView mad = new ModelAndView("charts");
        mad.addObject("areaChannelData",jsonObjectDim_AreaChannel.toJSONString());

        mad.addObject("areaChannelList",channelDim.toString());
        //用户请求方式对比情况
        mad.addObject("areaRequestTypeData",jsonObjectDim_AreaRequestType.toJSONString());
        mad.addObject("areaRequestTypeList",requestTypeDim);
        //用户渠道饼图情况
        mad.addObject("channelnoList",channelnoList);
        // 用户渠道趋势分析
        mad.addObject("areaChannelListAll",userService.getVAreaChannelAll());

        mad.addObject("areaRequestTypeListAll",userService.getVAreaRequestTypeAll());
        mad.addObject("userLoginAll",userService.getVUserLogin());
        return mad;
    }

    @RequestMapping("/details")
    public ModelAndView detail(@RequestParam(value = "areaname") String areaname, @RequestParam(value = "channelname") String channelname) {
        ModelAndView mad = new ModelAndView("detail");
        mad.addObject("userDetailList",userService.getDetail(areaname,channelname));
        // http://localhost:8080/details?areaname=%E7%A6%8F%E5%BB%BA%E7%9C%81%E7%A6%8F%E5%B7%9E%E5%B8%82&channelname=%E6%89%8B%E6%9C%BA
        return mad;
    }

    @RequestMapping("/chartssss")
    public String chartssss() {
        //获取用户渠道趋势分析维度
        return "relation";
    }
}
