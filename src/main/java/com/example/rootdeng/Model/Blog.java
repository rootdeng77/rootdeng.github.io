package com.example.rootdeng.Model;

public class Blog {
    private int id;
    private String brief;
    private String synopsis;
    private String link;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", brief='" + brief + '\'' +
                ", synopsis='" + synopsis + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
