package com.example.rootdeng.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.rootdeng.ApiAnalysis.ApiDetail;
import com.example.rootdeng.ApiAnalysis.readCatalog;
import com.example.rootdeng.Model.Message;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.example.rootdeng.util.FileUtil;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class FileController {
    private static String filenames=new String();
    private static JSONObject js=new JSONObject();


    @RequestMapping(value = "/uploadFolder")
    public String uploadFolder(RedirectAttributes attr,MultipartFile[] folder, HttpServletResponse response, HttpServletRequest req) {
        if(folder==null) return "file";
        long time=new Date().getTime();
        FileUtil.saveMultiFile("/media/hadoop/Ubuntu/file/",String.valueOf(time), folder);
        for(MultipartFile a:folder){
            filenames=a.getOriginalFilename().substring(0,a.getOriginalFilename().indexOf("/"));
        }
    //    List<ApiDetail> apiDetails=new readCatalog().getRelation();
        js =getJson("/media/hadoop/Ubuntu/file/"+String.valueOf(time)+filenames);
//        System.out.println(js.toJSONString());
        req.setAttribute("result",js);
        return "APIShow";
    }

    public static JSONObject getJson(String filename){
//        System.out.println(filename);
        List<ApiDetail> apiDetails=new readCatalog().getRelation(filename);
        JSONArray nodes=new JSONArray();
        JSONArray links=new JSONArray();
        for(ApiDetail api:apiDetails){
            JSONObject apiJson=new JSONObject();
            apiJson.put("name",api.getApi());
            apiJson.put("symbolSize",50);
            apiJson.put("category",0);
            apiJson.put("des",api.getUserService());
            nodes.add(apiJson);
//            System.out.println(apiJson.toString());
            String[] tables=api.getUserTable().split(",");
            for(String table:tables){
                JSONObject tableJson=new JSONObject();
                tableJson.put("name",table);
                tableJson.put("symbolSize",70);
                tableJson.put("category",1);
                tableJson.put("des",api.getUserService());
                nodes.add(tableJson);
  //              System.out.println(tableJson.toString());
                JSONObject link=new JSONObject();
                link.put("source",api.getApi());
                link.put("target",table);
                String relation=new String();
                for(String func:api.getFunctionName().split(",")){
                    if(func.contains("insert")||func.contains("create")){
                        if(!relation.contains("增")){
                            relation+="增";
                        }
                    }
                    if(func.contains("delete")){
                        if(!relation.contains("删")){
                            relation+="删";
                        }
                    }
                    if(func.contains("update")){
                        if(!relation.contains("改")){
                            relation+="改";
                        }
                    }
                    if(!(func.contains("insert")||func.contains("create"))&&
                        !func.contains("delete")&&!func.contains("update")){
                        if(!relation.contains("查")){
                            relation+="查";
                        }
                    }
                }
                link.put("name",relation);
                link.put("desc",api.getFunctionName());
//                System.out.println("link is "+link.get("source")+"   "+link.get("target")+"    "+api.getUserTable());
                links.add(link);
//                System.out.println(link.toString());
            }
        }
        JSONObject js=new JSONObject();
        js.put("nodes",nodes);
        js.put("links",links);
//        System.out.println(js.get("node"));
        return js;
    }

    @ResponseBody
    @RequestMapping("/list")
    public List<Message> list(){
        List<Message> list=new ArrayList<>();
        Message message=new Message();
        message.setId("1");
        message.setCommand("许嵩");
        message.setDescription("歌手");
        message.setContent("最佳歌手");
        Message message1=new Message();
        message1.setId("2");
        message1.setCommand("蜀云泉");
        message1.setDescription("程序员");
        message1.setContent("不断成长的程序员");
        list.add(message);
        list.add(message1);
        return list;
    }

    @ResponseBody
    @RequestMapping("/getTarget")
    public JSONObject getTarget(){
        return js;
    }

    @ResponseBody
    @RequestMapping("/testJson")
    public JSONObject testJson(){
        String jsonStr = "{\"school\":\"商职\",\"sex\":\"男\",\"name\":\"wjw\",\"age\":22}";
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        return jsonObject;
    }
}
