package com.example.rootdeng.ApiAnalysis;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class readCatalog {
    private static List<String> apiList=new ArrayList<>();
    private static List<ApiDetail> apiDetails=new ArrayList<>();
    private static List<List<dbShow>> xmlList=new ArrayList<>();
    private static List<List<ServiceFunction>> serviceList=new ArrayList<>();
    private static int cnt=0;

//    public static void main(String[] args){
//        getRelation();
//
//    }

    public List<ApiDetail> getRelation(String filepath) {
        File dir = new File(filepath);
        File[] files = dir.listFiles();
        FileFilter fileFilter = new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        };
        files = dir.listFiles(fileFilter);
        for (int i=0; i< files.length; i++) {
            dfsCatalog(files[i]);
            File filename = files[i];
//            System.out.println(filename.toString());
        }
//        System.out.println("cnt is "+cnt);
//        System.out.println("api size is "+apiList.size()+"  api  like that \n");
//        for(String api:apiList){
//            System.out.println(api);
//        }
//        System.out.println("xml  is "+xmlList.size()+"  xml like that ");
//        for(List<dbShow> dbs:xmlList){
//            for(dbShow db:dbs){
//                System.out.println(db.toString());
//            }
//        }
//////        int ans=0,ans2=0;
////////        System.out.println("Service is "+serviceList.size()+"  service like that");
//        for(List<ServiceFunction> a:serviceList){
//            System.out.println("***********************");
//            for(ServiceFunction b : a){
//                System.out.println(b);
////                ans2++;
//            }
// //           ans++;
//        }
//        System.out.println("ans is : "+ans+"  cnt is : "+cnt+"  ans2 is : "+ans2);

//        System.out.println("apiList detail size is : "+apiDetails.size()+"  apiDetail such like that");
//        for(ApiDetail api:apiDetails){
//            System.out.println(api.toString());
//        }
//        System.out.println("*****************************");
        reviewAPITables();
//        for(ApiDetail api:apiDetails){
//                System.out.println(api.toString());
//        }
        return apiDetails;
    }

    public static void dfsCatalog(File fileDir){
        File dir=new File(String.valueOf(fileDir));
        File[] files = dir.listFiles();
        for (int i=0; i< files.length; i++) {
            if(files[i].isDirectory()){
                dfsCatalog(files[i]);
            }
            File filename = files[i];
//            System.out.println(filename.toString());
            if(filename.isFile()){
                if(filename.toString().contains("Controller.java")){
                    analysisApi(filename);
                }else if(filename.toString().contains(".xml")){
                    analysisXml(filename);
                }else if(filename.toString().contains("Service.java")){
                    analysisService(filename);
                }
            }
        }
        // 将apiDetail解析后返回调用的数据表
    }

    /*
     * 从java文件中获取全部的api接口名称，后续功能慢慢添加吧
     */
    public static void analysisApi(File filename){
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            Map<String,String> targetChange=new HashMap<>();
            String str, prefix=new String()
                    ,functionName=new String()
                    ,serviceName=new String()
                    ,apiName=new String();
            int time=0;
            Boolean isTargetChange=false;
            while ((str = in.readLine()) != null) {
                if(str.contains("RequestMapping")){
                    if(!functionName.isEmpty()){                    //以该处作为终结条件统计上一个接口的数据表调用情况
                        ApiDetail apiDetail=new ApiDetail();
                        apiDetail.setApi(apiName);
                        apiDetail.setUserService(serviceName);
                        apiDetail.setFunctionName(functionName);
                        apiDetails.add(apiDetail);
                        functionName=new String();
                        serviceName=new String();
                        apiName=new String();
//                        System.out.println(apiDetail);
                    }
                    Pattern pattern=Pattern.compile("\"(.*?)\"");
                    Matcher m=pattern.matcher(str);
                    String ans=new String();
                    if(m.find()){
                        ans=m.group(1);
                        if(time!=-1&&time==0){
                            time++;
                            prefix=m.group(1);
                        }else{
                            apiList.add(prefix+ans);
                        }
                    }
                    apiName=prefix+ans;
                }
                //判断是否存在前缀
                if(str.contains(" class ")&&time==0){
                    time=-1;
                }

                /*
                 *获取每个service的实例化对象名称
                 */
                if(isTargetChange){
                    String[] line=str.trim().split(" ");
                    int length=line.length;
//                    System.out.println("length is : "+line.length);
//                    System.out.println("str is : "+str+"\nstr.trim is : "+str.trim()+"\n"+line[1]);
                    line[length-1]=line[length-1].substring(0,line[length-1].length()-1);
//                    System.out.println(line[length-1]+"                "+line[length-2]);
                    if(targetChange.get(line[length-1])==null){
                        targetChange.put(line[length-1],line[length-2]);
                    }
                    isTargetChange=false;
                }

                if(str.contains("@Autowired")){
                    isTargetChange=true;
                }

                /*
                 * if(str.contains("=")&&str.contains("(")){
                 * 本想方便一下用简单点的写法，后来发现匹配了一堆神奇的代码，还是改用正则表达式
                 */
                Pattern pattern=Pattern.compile("[a-zA-Z<>,0-9]+\\s*\\=\\s*\\w+\\.\\w+\\(");
                Matcher matcher=pattern.matcher(str);

                if(matcher.find()){
                    for(String tareget : targetChange.keySet()){
                        if(str.contains(tareget)){
                            String[] line=str.split(tareget);
//                            System.out.println("function str is "+str);
                            if(serviceName.isEmpty()){
                                serviceName= targetChange.get(tareget);
                            }else{
                                if(!serviceName.contains(targetChange.get(tareget))){
                                    serviceName+=","+targetChange.get(tareget);
                                }
                            }
                            if(functionName.isEmpty()){
                                functionName= line[1].substring(1,line[1].indexOf('('));
                            }else{
                                if(!functionName.contains(line[1].substring(1,line[1].indexOf('(')))){
                                    functionName+=","+line[1].substring(1,line[1].indexOf('('));
                                }
                            }
                        }
                    }
                }
                Pattern inAndUpdate=Pattern.compile("[a-zA-Z0-9]+\\.[a-zA-Z0-9]+\\(");
                Matcher getInAndUpdate=inAndUpdate.matcher(str);

                if(getInAndUpdate.find()){
                    for(String tareget : targetChange.keySet()){
                        if(str.contains(tareget)){
                            String[] line=str.split(tareget);

                            if(serviceName.isEmpty()){
                                serviceName= targetChange.get(tareget);
                            }else{
                                if(!serviceName.contains(targetChange.get(tareget))){
                                    serviceName+=","+targetChange.get(tareget);
                                }
                            }
                            if(functionName.isEmpty()){
                                functionName= line[1].substring(1,line[1].indexOf('('));
                            }else{
                                if(!functionName.contains(line[1].substring(1,line[1].indexOf('(')))){
                                    functionName+=","+line[1].substring(1,line[1].indexOf('('));
                                }
                            }
                        }
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * 从xml文件中获取每次被调用的数据表
     */
    public static void analysisXml(File filename){
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String str,nameSpace="",sqlId,sqlStr="";
            List<dbShow> message=new ArrayList<>();                  //存到链表中
            dbShow  dbMessage=new dbShow();

            while ((str = in.readLine()) != null) {
                if(str.contains("namespace")){                       //获取命名空间的包名
                    Pattern pattern=Pattern.compile("\"(.*?)\"");
                    Matcher m=pattern.matcher(str);
                    if(m.find()){
                        nameSpace=m.group(1);
//                        System.out.println("ans  is "+nameSpace);
                    }
                }

                /*
                 *  由于是通过id来进行判断该行是否为新的数据库操作语句，因此容易导致最后一个操作语句丢失
                 */
                if(str.contains("id=\"")&&!str.contains("include")||str.contains("mapper")){       //切割字符串，获取id所在位置并进行切割
//                    System.out.println(dbMessage);
//                    System.out.println("sqlStr is "+sqlStr);
                    dbMessage.setDatabase(analysisTables(sqlStr));
                    dbMessage.setSqlStr(sqlStr);
//                    System.out.println("test dbMessage is "+dbMessage);
                    if(dbMessage.getDatabase()!=""){         //将上一个获取到的sql语句信息写入链表
                        message.add(dbMessage);
                    }
                    sqlStr="";

                    dbMessage=new dbShow();
                    dbMessage.setNameSpace(nameSpace);
                    String[] lines=str.split("\t");
                    for(String line:lines){
                        if(line.contains("id=")){
                            Pattern pattern=Pattern.compile("\"(.*?)\"");
                            Matcher m=pattern.matcher(str);
                            if(m.find()){
                                sqlId=m.group(1);
                                dbMessage.setId(sqlId);
//                                System.out.println("ans  is "+sqlId);
                            }
                        }
                    }
                }
                if(str.contains("include")) continue;
                if(str.contains(";")){
                    str= str.substring(0,str.length()-1);
                }
                sqlStr+=" "+str;

            }
            xmlList.add(message);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String analysisTables(String str){
        Boolean isTable=false;      // 防止存在  from  table1, table 2 这种情况，主要是把链接写在一行
//        cnt++;
        if(str.contains("?xml ")||str.contains("include ")
                ||str.contains("sql ")
                ||str.contains("context"))
            return "";
        if(str.contains("resultMap")&&!str.contains("select ")) return "";
        String table="";

        /*
         *   sql语句中链接数据库的只有from 和join ,insert ,update，检索关键词
         *    由于from和join可能会链接多个数据库，所以通过判断from 后和join后的字符串，如果碰见关键字，则停止查找
         */
        if(str.contains("from ")||str.contains("join ")){
//            System.out.println(str);
            String[] lines=str.split(" ");
            for(int i=0;i<lines.length;i++){
//                System.out.println(lines[i]+"  is table  "+isTable);
                if(lines[i].equals("from")||lines[i].equals("join")){
                    if(Character.isLetter(lines[i+1].charAt(0))){
                        isTable=true;
                        continue;
                    }
                }
                if("leftrightonorderlimitwhere,<ifgroup<select;</select".contains(lines[i].toLowerCase())){
                    isTable=false;
                }
                if("join".contains(lines[i])) continue;
                if(isTable){
                    if(table==""){
                        table=lines[i];
                    }else{
                        table=table+","+lines[i];
                    }
                }
            }
        }

        /*
         *   sql语句中链接数据库的只有from 和join ,insert ,update，检索关键词
         *    由于from和join可能会链接多个数据库，所以通过判断from 后和join后的字符串，如果碰见关键字，则停止查找
         */
        if(str.contains("insert into ")||str.contains("update ")){
            String[] lines=str.split(" ");
            for(int i=0;i<lines.length;i++){
                if(lines[i].equals("into")){
                    table=lines[i+1];
                    break;
                }
                if(lines[i].equals("update")){
                    table=lines[i+1];
                    break;
                }
            }
        }
//        System.out.println("return table is "+table);
        return table;
    }

    public static List<String> testSql(){
        List<String> str=new ArrayList<>();
        str.add("<select id=\"selectByPrimaryKey\" parameterType=\"java.lang.Integer\" resultMap=\"BaseResultMap\"> <!-- WARNING - @mbg.generated This element is automatically generated by MyBatis Generator, do not modify. This element was generated on Fri Sep 07 20:32:05 CST 2018. --> select id, promotion_id, visit_user_cnt, visit_new_user_cnt, direct_visit_user_cnt, direct_visit_new_user_cnt, transfer_page_visit_user_cnt, transfer_page_visit_new_user_cnt, invited_visit_user_cnt, invited_visit_new_user_cnt, visit_user_create_habit_user_cnt, visit_new_user_create_habit_user_cnt, visit_user_create_habit_cnt, visit_new_user_create_habit_cnt, visit_user_create_habit_visit_user_cnt, visit_user_create_habit_visit_new_user_cnt, visit_user_create_habit_join_user_cnt, visit_user_create_habit_join_new_user_cnt, visit_user_create_habit_log_user_cnt, visit_user_create_habit_log_new_user_cnt, direct_visit_user_create_habit_user_cnt, direct_visit_new_user_create_habit_user_cnt, direct_visit_user_create_habit_cnt, direct_visit_new_user_create_habit_cnt, direct_visit_user_create_habit_visit_user_cnt, direct_visit_user_create_habit_visit_new_user_cnt, direct_visit_user_create_habit_join_user_cnt, direct_visit_user_create_habit_join_new_user_cnt, direct_visit_user_create_habit_log_user_cnt, direct_visit_user_create_habit_log_new_user_cnt, invited_visit_user_create_habit_user_cnt, invited_visit_new_user_create_habit_user_cnt, invited_visit_user_create_habit_cnt, invited_visit_new_user_create_habit_cnt, invited_visit_user_create_habit_visit_user_cnt, invited_visit_user_create_habit_visit_new_user_cnt, invited_visit_user_create_habit_join_user_cnt, invited_visit_user_create_habit_join_new_user_cnt, invited_visit_user_create_habit_log_user_cnt, invited_visit_user_create_habit_log_new_user_cnt, share_user_cnt, share_new_user_cnt, share_cnt, new_user_share_cnt, create_habit_visit_user_next_1_date_active_cnt, create_habit_visit_user_next_3_date_active_cnt, create_habit_visit_user_next_7_date_active_cnt, visit_user_get_habit_user_cnt, visit_new_user_get_habit_user_cnt, visit_user_get_habit_cnt, visit_new_user_get_habit_cnt, visit_user_get_habit_visit_user_cnt, visit_user_get_habit_visit_new_user_cnt, visit_user_get_habit_join_user_cnt, visit_user_get_habit_join_new_user_cnt, visit_user_get_habit_log_user_cnt, visit_user_get_habit_log_new_user_cnt, direct_visit_user_get_habit_user_cnt, direct_visit_new_user_get_habit_user_cnt, direct_visit_user_get_habit_cnt, direct_visit_new_user_get_habit_cnt, direct_visit_user_get_habit_visit_user_cnt, direct_visit_user_get_habit_visit_new_user_cnt, direct_visit_user_get_habit_join_user_cnt, direct_visit_user_get_habit_join_new_user_cnt, direct_visit_user_get_habit_log_user_cnt, direct_visit_user_get_habit_log_new_user_cnt, invited_visit_user_get_habit_user_cnt, invited_visit_new_user_get_habit_user_cnt, invited_visit_user_get_habit_cnt, invited_visit_new_user_get_habit_cnt, invited_visit_user_get_habit_visit_user_cnt, invited_visit_user_get_habit_visit_new_user_cnt, invited_visit_user_get_habit_join_user_cnt, invited_visit_user_get_habit_join_new_user_cnt, invited_visit_user_get_habit_log_user_cnt, invited_visit_user_get_habit_log_new_user_cnt, get_habit_visit_user_next_1_date_active_cnt, get_habit_visit_user_next_3_date_active_cnt, get_habit_visit_user_next_7_date_active_cnt, next_date_visit_user_cnt, new_user_next_date_visit_user_cnt, join_habit_user_cnt, join_habit_new_user_cnt, next_date_join_habit_user_cnt, new_user_next_date_join_habit_user_cnt, log_user_cnt, log_new_user_cnt, next_date_log_user_cnt, new_user_next_date_log_user_cnt, direct_visit_user_next_date_visit_user_cnt, direct_visit_new_user_next_date_visit_user_cnt, direct_visit_user_join_habit_user_cnt, direct_visit_user_join_habit_new_user_cnt, direct_visit_user_next_date_join_habit_user_cnt, direct_visit_new_user_next_date_join_habit_user_cnt, direct_visit_user_log_user_cnt, direct_visit_user_log_new_user_cnt, direct_visit_user_next_date_log_user_cnt, direct_visit_user_new_user_next_date_log_user_cnt, invited_visit_user_next_date_visit_user_cnt, invited_visit_new_user_next_date_visit_user_cnt, invited_visit_user_join_habit_user_cnt, invited_visit_user_join_habit_new_user_cnt, invited_visit_user_next_date_join_habit_user_cnt, invited_visit_new_user_next_date_join_habit_user_cnt, invited_visit_user_log_user_cnt, invited_visit_user_log_new_user_cnt, invited_visit_user_next_date_log_user_cnt, invited_visit_user_new_user_next_date_log_user_cnt, dt from promotion_effect_detail_day where id = #{id,jdbcType=INTEGER} </select>\n");
        str.add("<select id=\"selectByPrimaryKey\" parameterType=\"java.lang.Integer\" resultMap=\"BaseResultMap\"> <!-- WARNING - @mbg.generated This element is automatically generated by MyBatis Generator, do not modify. This element was generated on Mon Feb 26 16:14:17 CST 2018. --> select id, open_id, wx_version, xdk_version, net_type, file_type, file_name, file_size, file_time, ip_head, ip, is_reload, err_type, err_code, upload_state, upload_host, event_time, habit_id, upload_begin_time, upload_end_time, create_date, update_time, create_time, wx_cash_path, err_msg from user_upload_log where id = #{id,jdbcType=INTEGER} </select>");
        str.add("<select id=\"getVUserLogin\" resultType=\"com.example.rootdeng.Model.VUserLogin\"> select * from v_user_login order by imei desc limit 10; </select>");
        str.add("<update id=\"update\"> update blogs set brief=#{brief},synopsis=#{synopsis},link=#{link} where id=#{id}; </update>");
        str.add("<delete id=\"delete\"> delete from blogs where id=#{id}; </delete>");
        return str;
    }

    /*
     *   分析Service包下的全部文件，获取每个接口中的被调用的sql功能语句，
     */
    public static void analysisService(File filename){
        List<ServiceFunction> serviceFunctions=new ArrayList<>();
        Map<String,List<String>> spaceAndSql=new HashMap<>();      //存储xml命名空间及其对应的sql功能函数
        Map<String,String> targetChange=new HashMap<>();           // 确定实例化对象后的名称 AccessMapper accessMapper;
        List<String> checkFunctions=new ArrayList<>();
        Boolean isTargetChange=false;
        ServiceFunction serviceFunction=new ServiceFunction();
        String targets=""
                ,functionName=""
                ,packageName=new String()
                ,className=new String()
                ,finalTable=new String();
        int top=0;

        String namespace="";                                  //命名空间
        try {
            BufferedReader br=new BufferedReader(new FileReader(filename.toString()));
            String line;
            while ((line=br.readLine())!=null){
                checkFunctions.add(line);
                if(line.contains("package ")) {
                    packageName=line.split(" ")[1];
                    packageName=packageName.substring(0,packageName.length()-1);
                }
                if(line.contains("public class")){
//                    System.out.println("line is "+line);
                    className=line.split(" ")[2];
                    if(className.contains("{")){
                        className=className.substring(0,className.length()-1);
                    }
//                    System.out.println("class name is : "+className);
                }
                if(className.contains("HabitScoreSrevice")) System.out.println("get it"+className);
                if(line.contains("import ")&&line.contains("Mapper")){              //获取全部的Mapper来判断被查询的模块
                    namespace=line.split("\\.")[line.split("\\.").length-1];
                    if(spaceAndSql.get(namespace)==null){
                        spaceAndSql.put(namespace,new ArrayList<>());
                    }
                    serviceFunction.setNamespace(namespace);
                    continue;
                }
                if(line.contains("{")) top++;
                if(line.contains("}")) top--;

                if(top==1){
                    if(line.equals("")) continue;                          // 可能存在某些行为空的情况
                    serviceFunction.setFuncName(functionName);
                    serviceFunction.setPackageName(packageName);
                    serviceFunction.setClassName(className);
                    serviceFunction.setUserTables(finalTable);
                    if(serviceFunction.getFuncName()!="") serviceFunctions.add(serviceFunction);

                    serviceFunction=new ServiceFunction();
                }

                /*
                 *本来是先解析的每个被调用的mapper及其对应的sql函数，后解析的java函数，
                 * 当然可以进行一下优化玩玩
                 */

                if(top>1){
                    /*
                     * 至于为什么放到这里，因为排除import和实例化对象代码中出现的变量名，需要的话可以代码顺序看看效果就知道了
                     * 改掉顺序后将import中的内容和实例化对象的代码也解析了，当然这个肯定是错的
                     */
                    if(!isTargetChange&&!line.contains("import")){
                        for(String targetName:targetChange.keySet()){
                            if(line.contains(targetName)){
                                try{
                                    if(line.indexOf(".")==-1) continue;
                                    String[] getSql=line.split(targetName);

                                    String sqlStr=getSql[1].substring(1,getSql[1].indexOf('('));            //去掉 .
                                    String mapperName=targetChange.get(targetName);

                                    if(spaceAndSql.get(mapperName)==null||spaceAndSql.get(mapperName).indexOf(sqlStr)==-1){
                                        List<String> temp=spaceAndSql
                                                .get(mapperName)==null?new ArrayList<>():
                                                spaceAndSql.get(mapperName);
                                        temp.add(sqlStr+","+functionName);
                                        spaceAndSql.put(mapperName,temp);
                                    }

                                    if(serviceFunction.getNamespace()==null){
                                        serviceFunction.setNamespace(mapperName);
                                    }else if(!serviceFunction.getNamespace().contains(mapperName)){
                                        String temp=serviceFunction.getNamespace();
                                        temp=temp+","+mapperName;
                                        serviceFunction.setNamespace(temp);
                                    }

                                    if(serviceFunction.getSqlStr()==null){
                                        serviceFunction.setSqlStr(sqlStr);
                                    }else {
                                        String sqlTemp = serviceFunction.getSqlStr();
                                        sqlTemp += "," + sqlStr;
                                        serviceFunction.setSqlStr(sqlTemp);
                                    }
                                }catch (ArrayIndexOutOfBoundsException e){
                                    System.out.println(line);
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }

                /*
                 * 通过对isTargetChange进行这样的顺序设置可以很好的解决上段代码出现的问题并不用添加任何参数
                 */
                if(isTargetChange){
                    String[]  target=line.trim().split(" ");
                    int length=target.length;
                    targetChange.put(target[length-1].substring(0,target[length-1].length()-1),target[length-2]);
                    isTargetChange=false;

                }

                if(line.contains("@Autowired")){
                    isTargetChange=true;
                }

                Pattern pattern=Pattern.compile("(public|private)\\s+([a-zA-Z0-9<,>]+\\s+)+(.*?)\\([a-zA-z@\\s+,<>]*");//匹配函数
                if(line.contains("CacheBuilder.newBuilder()")){
                    String[] exceptSing=line.split("=");
                    String[] lines2=exceptSing[0].split(" ");
                    functionName=lines2[lines2.length-1];
                }else{
                    Matcher m=pattern.matcher(line);
                    if(m.find()){
                        if(line.contains(";")) continue;
                        functionName=m.group(3);
                    }
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<ServiceFunction> getServiceList=reviewFunction(checkFunctions,serviceFunctions,0)
                .stream().collect(
                        Collectors.collectingAndThen(
                                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ServiceFunction::getFuncName))), ArrayList::new                        )
                );

        serviceList.add(getServiceList);
    }

    public static List<ServiceFunction> reviewFunction(List<String> checkFunctions,
                                                       List<ServiceFunction> serviceFunctions,
                                                       int cntReview){
        String functionName=new String()
                ,sqlStr=new String()
                ,namespace=new String();
        int top=0;

        for(String line:checkFunctions){
            if(line.contains("{")) top++;
            if(line.contains("}")) top--;
            Pattern pattern=Pattern.compile("(public|private)\\s+([a-zA-Z0-9<,>]+\\s+)+(.*?)\\([a-zA-z@\\s+,<>]*");//匹配函数
            if(line.contains("CacheBuilder.newBuilder()")){
                String[] exceptSing=line.split("=");
                String[] lines2=exceptSing[0].split(" ");
                functionName=lines2[lines2.length-1];
            }else{
                Matcher m=pattern.matcher(line);
                if(m.find()){
                    if(line.contains(";")) continue;
                    functionName=m.group(3);
                }
            }
            if(top==1){
                if(line.equals("")) continue;
                for(ServiceFunction check:serviceFunctions){
                    if(check.getFuncName().equals(functionName)){
//                        System.out.println("match correct");
                        if(check.getSqlStr()==null){
                            check.setSqlStr(sqlStr);
                        }else{
                            for(String st:sqlStr.split(",")){
                                if(!check.getSqlStr().contains(st)){
                                    String temp=check.getSqlStr();
                                    temp+=","+st;
                                    check.setSqlStr(temp);
                                }
                            }
                        }
                        if(check.getNamespace()==null){
                            check.setNamespace(namespace);
                        }else {
                            for(String na:namespace.split(",")){
                                if(!check.getNamespace().contains(na)){
                                    String temp=check.getNamespace();
                                    temp+=","+na;
                                    check.setNamespace(temp);
                                }
                            }
                        }
                    }
                }
            }
            if(top>1){
                if(!line.contains("(")) continue;
                for(ServiceFunction check:serviceFunctions){
                    if(line.contains(check.getFuncName())){
                        if(sqlStr==null){
                            sqlStr=check.getSqlStr();
                        }else {
                            if(check.getSqlStr()==null) continue;
                            else{
                                if(!check.getSqlStr().contains(",")){
                                    sqlStr=check.getSqlStr();
                                }else{
                                    String[] checkSqlStr=check.getSqlStr().split(",");
                                    for(String sqlcheck:checkSqlStr){
                                        if(!sqlStr.contains(sqlcheck)){
                                            sqlStr+=","+sqlcheck;
                                        }
                                    }
                                }
                            }
                        }
                        if(namespace==null){
                            namespace=check.getNamespace();
                        }else{
                            if(check.getNamespace()==null) continue;
                            else {
                                if(!check.getNamespace().contains(",")){
                                    namespace=check.getNamespace();
                                }else{
                                    String[] namespaceCheck=check.getNamespace().split(",");
                                    for(String nameCheck:namespaceCheck){
                                        if(!namespace.contains(nameCheck)){
                                            namespace+=","+nameCheck;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        cntReview++;
        if(cntReview>1){
            for(int i=0;i<serviceFunctions.size();i++){
                if(serviceFunctions.get(i).getNamespace().isEmpty()){
                    serviceFunctions.remove(i);
                }
            }
        }
        return cntReview>1? serviceFunctions: reviewFunction(checkFunctions,serviceFunctions,cntReview);
    }

    public static void reviewAPITables(){
        for(ApiDetail apiDetail:apiDetails){
            String tables=new String();
            for(String userService:apiDetail.getUserService().split(",")){
                for(String funcName:apiDetail.getFunctionName().split(",")){
//                    System.out.println("user Service is "+userService+"  funcName  is "+funcName);
                    if(tables.isEmpty()){
                        tables=getTables(userService,funcName);
                    }else{
                        if(!getTables(userService,funcName).isEmpty())
                        for(String tb:getTables(userService,funcName).split(",")){
                            if(!tables.contains(tb)){
                                tables+=","+tb;
                            }
                        }
                    }

                    for(List<ServiceFunction> serviceFunctions:serviceList){
                        for(ServiceFunction serviceFunction:serviceFunctions){
                            if(serviceFunction.getClassName().contains(userService)&&
                                serviceFunction.getFuncName().contains(funcName)){
                                for(String namesp:serviceFunction.getNamespace().split(",")){
                                    for(String sqlst:serviceFunction.getSqlStr().split(",")){
                                        if(tables.isEmpty()){
                                            tables=getTables(namesp,sqlst);
                                        }else{
                                            if(!getTables(namesp,sqlst).isEmpty())
                                                for(String tb:getTables(namesp,sqlst).split(",")){
                                                    if(!tables.contains(tb)){
                                                        tables+=","+tb;
                                                    }
                                                }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            apiDetail.setUserTable(tables);
        }
    }

    public static String getTables(String userService,String funcName){
        String tables=new String();
        for(List<dbShow> dbShows:xmlList){
            for(dbShow db: dbShows){
                if(db.getNameSpace().contains(userService)
                        &&db.getId().equals(funcName)){
                    if(tables.isEmpty()){
                        tables=db.getDatabase();
                    }else {
                        for(String dbt:db.getDatabase().split(",")){
                            if(!tables.contains(dbt)){
                                tables+=","+dbt;
                            }
                        }
                    }
                }
            }
        }
        return tables;
    }
}

//  ApiDetail{api='api/v1/stat/geo/latlng', userTable='', userService='GeoService', functionName='getCityBylatlngs'}
