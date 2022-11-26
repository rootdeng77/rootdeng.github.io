package com.example.rootdeng.POIScript.Service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: POIScriptService
 * @version: 1.0.0
 * @date: 22-11-20 下午3:39
 * @author: hadoop
 * @describe:
 */
public interface POIScriptService {

    /**
     * 执行对应的命令
     * @param execute 执行参数
     * @return 执行结果
     */
    List<Map<String, Object>> executeOrder(String execute);
}
