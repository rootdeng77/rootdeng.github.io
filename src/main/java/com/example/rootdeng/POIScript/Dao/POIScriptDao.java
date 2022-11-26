package com.example.rootdeng.POIScript.Dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: POIScriptDao
 * @version: 1.0.0
 * @date: 22-11-20 下午3:41
 * @author: hadoop
 * @describe:
 */
@Mapper
public interface POIScriptDao {
    /**
     * 执行对应的命令
     * @param execute 执行参数
     * @return 执行结果
     */
    List<Map<String, Object>> executeOrder(String execute);
}