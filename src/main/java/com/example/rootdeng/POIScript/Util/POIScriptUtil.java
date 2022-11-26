package com.example.rootdeng.POIScript.Util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.rootdeng.POIScript.Service.POIScriptService;
import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: POIScriptUtil
 * @version: 1.0.0
 * @date: 22-11-20 下午1:37
 * @author: rootdeng
 * @describe: 通过json文件来实现特定excel树立
 */
@Component
public class POIScriptUtil {
    @Autowired
    POIScriptService poiScriptService;


    public static final String TITLE_NAME = "titleName";

    public static final String HEAD_NAME = "headName";

    public static final String HEIGHT_SIZE = "heightSize";

    public static final String TITLE_TEMPLATE = "titleTemplate";

    public static final String CONTENT_TEMPLATE = "contentTemplate";

    public static final String SHEET_NAME = "sheetName";

    public static final String EXECUTE = "execute";

    public static final String ARGS = "args";

    public static final String SPLIT_FILE = "splitBy";

    public static final String SPLIT = "Split";

    public static final String FILE_NAME = "fileName";

    public static final int EXCEL_ROE_LIMIT = 5;


    /**
     * 创建shett页信息
     * @param workbook workbook
     * @param sheetName sheet名称
     * @param titleData 标题信息
     * @param contentData 内容信息
     */
    public void createSheet(Workbook workbook, String sheetName, JSONArray titleData, JSONArray contentData) {
        Sheet sheet = workbook.createSheet(sheetName);
        createSheetTitle(sheet, titleData, createTitleStyle(workbook));
        createSheetContent(sheet, titleData, contentData, createContentStyle(workbook));
    }

    /**
     * 在当前的sheet页面敬爱嗯标题信息写入
     * @param sheet sheet
     * @param titleData 标题信息
     * @param cellStyle 单元格格式
     */
    public void createSheetTitle(Sheet sheet, JSONArray titleData, CellStyle cellStyle) {
        Row row = sheet.createRow(0);
        for(int i=0;i<titleData.size();i++) {
            int weight = titleData.getJSONObject(i).getIntValue(HEIGHT_SIZE);
            sheet.setColumnWidth(i, weight==0?10*256:weight);
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(titleData.getJSONObject(i).getString(TITLE_NAME));
        }
    }

    /**
     * 创建标题的单元格格式（灰色北京，内容黑色加粗，居中）
     * @param workbook workbook
     * @return 标题的单元格格式
     */
    public CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        //设置背景颜色
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        //全部填充
        style.setFillPattern(FillPatternType.NO_FILL);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        return style;
    }

    /**
     * 创建内容的单元格格式（白色背景，居中）
     * @param workbook workbook
     * @return 内容的单元格格式
     */
    public CellStyle createContentStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        //设置背景颜色
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        //全部填充
        style.setFillPattern(FillPatternType.NO_FILL);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        return style;
    }

    /**
     * 在当前的sheet页面将内容信息写入
     * @param sheet 当前的sheet页面
     * @param titleData 标题参数
     * @param contentData 待写入的内容
     * @param cellStyle 内容的单元格格式
     */
    public void createSheetContent(Sheet sheet, JSONArray titleData, JSONArray contentData, CellStyle cellStyle) {
        int rowIndex = 1;
        for(int i=0;i<contentData.size();i++, rowIndex++) {
            JSONObject rowJSON = contentData.getJSONObject(i);
            Row row = sheet.createRow(rowIndex);
            for(int j=0; j< titleData.size();j++) {
                Cell cell = row.createCell(j);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(rowJSON.getString(titleData.getJSONObject(j).getString(HEAD_NAME)));
            }
        }
    }

    /**
     * 生成的excel写入本地
     * @param fileName
     */
    public void exportSheetToLocal(String fileName) {

    }


    /**
     * 解析并生成excel文件
     * @param bodyString 需要解析的json
     */
    public void analyBodyJSON(String bodyString) {
        JSONObject bodyJSON = JSONObject.parseObject(bodyString);
        String fileName = bodyJSON.getString(FILE_NAME);
        JSONArray titleData = bodyJSON.getJSONArray(TITLE_TEMPLATE);
        JSONArray contentData = bodyJSON.getJSONArray(CONTENT_TEMPLATE);
        if(!checkBothTemplate(titleData, contentData)) {
            System.out.println("标题模板与内容模板数量不一致");
        }
        for(int i=0;i<titleData.size();i++) {
            //目前先处理只有一个sheet的问题
            createSplitExcelByFile(contentData.getJSONArray(i), titleData.getJSONObject(i), fileName);
        }
    }


    /**
     * 开始进行分文件处理
     * @param contentData 内容信息
     * @param titleData 标题信息
     */
    private void createSplitExcelByFile(JSONArray contentData, JSONObject titleData, String fileName) {
        String args = "";
        JSONArray titleName = titleData.getJSONArray("title");
        List<Map<String, Object>> tempExecMap = Lists.newArrayList();
        for(int i=0;i<contentData.size();i++) {
            JSONObject contentJSON = contentData.getJSONObject(i);
            String execute = contentJSON.getString(EXECUTE);
            if(!StringUtils.isEmpty(args)) {
                //当前待执行的sql是最终生成文件的sql，执行处理
                if(CollectionUtils.isEmpty(tempExecMap)) {
                    //如果不需要根据参数进行拆分的话，直接导出
                    createExcelFile(titleData.getString(SHEET_NAME), titleName,  executeOrder(execute, titleName), fileName);
                } else {
                    //需要分组，根据之前查询的分组结果进行拆分
                    for(int j=0;j<tempExecMap.size();j++) {
                        execute = execute.replaceAll("\\$\\{" + args + "\\}", "\"" + tempExecMap.get(j).get(args).toString() + "\"");
                        preCreateExcelFile(titleData.getString(SHEET_NAME), titleName,  executeOrder(execute, titleName), fileName+tempExecMap.get(j).get(args));
                        execute = contentJSON.getString(EXECUTE);
                    }
                }
            } else {
                //有参数，表示当前待处理的sql并不是最终生成文件的sql，作为前置查询条件继续执行
                tempExecMap = poiScriptService.executeOrder(execute);
                args = contentJSON.getString(ARGS);

            }
        }
    }

    /**
     * 提前判断内容的数据连是否过大excel是否装得下，是否需要分成多个excel文件
     * @param sheetName
     * @param titleData
     * @param contentData
     * @param fileName
     */
    private void preCreateExcelFile(String sheetName, JSONArray titleData, JSONArray contentData, String fileName) {

        if(contentData.size() < EXCEL_ROE_LIMIT + 1)  {
            createExcelFile(sheetName, titleData, contentData, fileName);
        } else {
            int totalCount = contentData.size()/EXCEL_ROE_LIMIT + 1;
            for(int i=1;i<= totalCount; i++) {
                int fromIndex = (i-1)*EXCEL_ROE_LIMIT;
                JSONArray subContentData = new JSONArray();
                for(int j = fromIndex; j<((fromIndex+ + EXCEL_ROE_LIMIT)>contentData.size()?contentData.size():(fromIndex+ + EXCEL_ROE_LIMIT)); j++) {
                    subContentData.add(contentData.getJSONObject(j));
                }
                createExcelFile(sheetName, titleData, subContentData, fileName + "part" + i);
                subContentData.clear();
            }
        }
    }

    /**
     * 常见excel文件
     * @param sheetName sheett页名称
     * @param titleData 标题信息
     * @param contentData 内容信息
     * @param fileName excel文件名策划嗯
     */
    private void createExcelFile(String sheetName, JSONArray titleData, JSONArray contentData, String fileName) {
        fileName = fileName + ".xlsx";
        Workbook workbook = new SXSSFWorkbook();
        createSheet(workbook, sheetName, titleData, contentData);
        try {
            String filePath = "/media/hadoop/文档/data/" + fileName;
            File file = new File("/media/hadoop/文档/data/", fileName);
            if(!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePath);
            workbook.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据需要执行的sql生成对应的JSON文件
     * @param order 待执行的查询sql
     * @param titleData 当前sheet页面的标题内容
     * @return 执行结果转换后的json文件
     */
    public JSONArray executeOrder(String order, JSONArray titleData) {
        List<Map<String, Object>> result = poiScriptService.executeOrder(order);
        JSONArray jsonArray = new JSONArray();
        for(int i=0; i< result.size(); i++) {
            JSONObject json = new JSONObject();
            for(int j=0;j< titleData.size();j++) {
                String key = titleData.getJSONObject(j).getString(HEAD_NAME);
                json.put(key, result.get(i).get(key));
            }
            jsonArray.add(json);
        }
        return jsonArray;
    }

    /**
     * 检查标题和内容的叔祖是否相同
     * @param title 标题json叔祖
     * @param content 内容json叔祖
     * @return 是否相同
     */
    private boolean checkBothTemplate(JSONArray title, JSONArray content) {
        if(title.size() != content.size()) {
            return false;
        }
        return true;
    }
}
