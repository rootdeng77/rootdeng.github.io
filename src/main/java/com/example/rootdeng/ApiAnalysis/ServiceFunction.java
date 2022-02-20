package com.example.rootdeng.ApiAnalysis;

public class ServiceFunction {
    private String namespace;
    private String className;
    private String funcName;
    private String packageName;
    private String sqlStr;
    private String userTables;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getSqlStr() {
        return sqlStr;
    }

    public void setSqlStr(String sqlStr) {
        this.sqlStr = sqlStr;
    }

    public String getUserTables() {
        return userTables;
    }

    public void setUserTables(String userTables) {
        this.userTables = userTables;
    }

    @Override
    public String toString() {
        return "ServiceFunction{" +
                "namespace='" + namespace + '\'' +
                ", className='" + className + '\'' +
                ", funcName='" + funcName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", sqlStr='" + sqlStr + '\'' +
                ", userTables='" + userTables + '\'' +
                '}';
    }
}
