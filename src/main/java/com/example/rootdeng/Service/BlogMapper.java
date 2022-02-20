package com.example.rootdeng.Service;

import com.example.rootdeng.Model.Blog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogMapper {
    void insert(Blog blog);
    List<Blog> getBlogs();
    Blog getBlogById(int id);
    void delete(int id);
    void update(Blog blog);
}
