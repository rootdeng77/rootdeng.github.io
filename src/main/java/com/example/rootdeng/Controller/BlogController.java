package com.example.rootdeng.Controller;

import com.example.rootdeng.Model.Blog;
import com.example.rootdeng.Model.Emotion;
import com.example.rootdeng.Service.BlogMapper;
import com.example.rootdeng.util.MRDPUtil;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BlogController {
    @Autowired
    BlogMapper bm;

    @RequestMapping("/")
    public String index(HttpServletRequest request){
        List<Blog> blogs=bm.getBlogs();
        request.setAttribute("blogs",blogs);
        return "index";
    }

    @RequestMapping("/AIController")
    public String AIController(){
        return "AIController";
    }

    @RequestMapping("/blog/edit")
    public String edit(){
        return "edits";
    }

    @RequestMapping(value="blog/editdo")
    public String editdo(HttpServletRequest request){
        if(request.getParameter("id")==null||request.getParameter("id")=="") {
            Blog blog=new Blog();
            blog.setBrief(request.getParameter("brief"));
            blog.setSynopsis(request.getParameter("synopsis"));
            blog.setLink(request.getParameter("link"));
            //    System.out.println("Blog is "+blog);
            if(blog.getBrief()!=null) bm.insert(blog);   //不知为何每次跳转过程中插入空值
        }else if(request.getParameter("brief")!=null){
            Blog blog=new Blog();
            blog.setId(Integer.parseInt(request.getParameter("id")));
            blog.setBrief(request.getParameter("brief"));
            blog.setSynopsis(request.getParameter("synopsis"));
            blog.setLink(request.getParameter("link"));
            //   System.out.println("Blog is "+blog);
            bm.update(blog);
        }else{
            Blog blog=new Blog();
            blog=bm.getBlogById(Integer.parseInt(request.getParameter("id")));
            //    System.out.println("blog is "+blog);
            request.setAttribute("Blog",blog);
        }
        return "edits";
    }

    @RequestMapping("/ChinaMapShow")
    public String ChinaMapShow(){
        return "chinaMap";
    }

    @RequestMapping("/emotionAnalys")
    public String emotionAnalys(HttpServletRequest request) throws IOException {
        Emotion emotion=new Emotion();
        if(request.getParameter("synopsis")!=null){
            String synopsis=request.getParameter("synopsis");
            emotion.setStatement(synopsis);
            Analyzer analyzer=new Analyzer();
            emotion.setLabel(analyzer.calculate(synopsis));
            //    System.out.println(emotion.getLabel());
        }
        request.setAttribute("Emotion",emotion);
        return "emotion";
    }

    @RequestMapping("/segments")
    public String segments(HttpServletRequest request){
        String str=request.getParameter("segment");
        List<String> phrases=new ArrayList<>();
        List<String> keyWords=new ArrayList<>();
        List<String> segments=new ArrayList<>();
        if(str!=null){
            System.out.println(str);
            phrases=HanLP.extractPhrase(str,3);
            keyWords=HanLP.extractKeyword(str,3);
            List<Term> segmentss= HanLP.segment(str);
            for(Term term:segmentss){
                segments.add(MRDPUtil.getStrings(term.toString()));
            }
        }
        request.setAttribute("segments",segments);
        request.setAttribute("segment",str);
        request.setAttribute("phrases",phrases);
        request.setAttribute("keyWords",keyWords);
        return "segment";
    }

    @RequestMapping("/ESsearch")
    public String ESsearch(HttpServletRequest request){
        return "ESsearch";
    }
}
