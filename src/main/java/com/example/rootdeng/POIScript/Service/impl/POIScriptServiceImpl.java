package com.example.rootdeng.POIScript.Service.impl;

import com.example.rootdeng.POIScript.Dao.POIScriptDao;
import com.example.rootdeng.POIScript.Service.POIScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: POIScriptServiceImpl
 * @version: 1.0.0
 * @date: 22-11-20 下午3:40
 * @author: hadoop
 * @describe:
 */
@Service
public class POIScriptServiceImpl implements POIScriptService {
    @Autowired
    private POIScriptDao poiScriptDao;

    @Override
    public List<Map<String, Object>> executeOrder(String execute) {
        if(execute.contains("insert") || execute.contains("delete") || execute.contains("delete")) {
            System.out.println("为新操作");
        }
        List<Map<String, Object>> result =  poiScriptDao.executeOrder(execute);
        return result;
    }
}
