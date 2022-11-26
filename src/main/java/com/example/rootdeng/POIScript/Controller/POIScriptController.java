package com.example.rootdeng.POIScript.Controller;

import com.example.rootdeng.Model.Blog;
import com.example.rootdeng.POIScript.Util.POIScriptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName: POIScriptController
 * @version: 1.0.0
 * @date: 22-11-20 下午1:37
 * @author: hadoop
 * @describe:
 */
@Controller
public class POIScriptController {

    @Autowired
    private POIScriptUtil util;

    @RequestMapping("/poiString")
    public String index(String bodyJSON){
        bodyJSON = "{\n" +
                "    \"fileName\":\"20221124高风险地区分类\",\n" +
                "    \"titleTemplate\" : [\n" +
                "        {\n" +
                "            \"sheetName\" : \"poiScript测试\",\n" +
                "            \"title\":\n" +
                "            [\n" +
                "                {\n" +
                "                    \"titleName\": \"街道\",\n" +
                "                    \"headName\": \"street\",\n" +
                "                    \"heightSize\":3000\n" +
                "                },\n" +
                "                {\n" +
                "                    \"titleName\": \"社区\",\n" +
                "                    \"headName\": \"village\",\n" +
                "                    \"heightSize\":4000\n" +
                "                },\n" +
                "                {\n" +
                "                    \"titleName\": \"具体地区\",\n" +
                "                    \"headName\": \"location\",\n" +
                "                    \"heightSize\":4500\n" +
                "                },\n" +
                "                {\n" +
                "                    \"titleName\": \"时间\",\n" +
                "                    \"headName\": \"time\",\n" +
                "                    \"heightSize\":3000\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ],\n" +
                "    \"contentTemplate\": [\n" +
                "        [\n" +
                "            {\n" +
                "                \"execute\":\"select distinct street as street from epidemic_house\",\n" +
                "                \"args\":\"street\",\n" +
                "                \"split\": true\n" +
                "            },\n" +
                "            {\n" +
                "                \"execute\":\"select * from epidemic_house where street = ${street}\"\n" +
                "            }\n" +
                "        ]\n" +
                "    ]\n" +
                "}";
        util.analyBodyJSON(bodyJSON);
        return "index";
    }
}
