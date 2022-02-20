package com.example.rootdeng.ApiAnalysis;

public class ApiDetail {
    private String api;
    private String userTable;
    private String userService;
    private String functionName;

    public ApiDetail(){}

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getUserTable() {
        return userTable;
    }

    public void setUserTable(String userTable) {
        this.userTable = userTable;
    }

    public String getUserService() {
        return userService;
    }

    public void setUserService(String userService) {
        this.userService = userService;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    @Override
    public String toString() {
        return "ApiDetail{" +
                "api='" + api + '\'' +
                ", userTable='" + userTable + '\'' +
                ", userService='" + userService + '\'' +
                ", functionName='" + functionName + '\'' +
                '}';
    }
}
