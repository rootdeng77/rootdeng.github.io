package com.example.rootdeng.ApiAnalysis;

public class dbShow {
    private String id;
    private String nameSpace;
    private String database;
    private String sqlStr;

    public dbShow() {
    }

    public String getSqlStr() {
        return sqlStr;
    }

    public void setSqlStr(String sqlStr) {
        this.sqlStr = sqlStr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    @Override
    public String toString() {
        return "dbShow{" +
                "id='" + id + '\'' +
                ", nameSpace='" + nameSpace + '\'' +
                ", database='" + database + '\'' +
                ", sqlStr='" + sqlStr + '\'' +
                '}';
    }
}
