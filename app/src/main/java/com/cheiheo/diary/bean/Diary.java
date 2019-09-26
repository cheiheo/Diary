package com.cheiheo.diary.bean;

/**
 * author:chen hao
 * email::
 * time:2019/09/20
 * desc:
 * version:1.0
 */
public class Diary {

    private int id;
    private String date;
    private String title;
    private String content;
    private String tag;

    public Diary(){}

    public Diary(String date, String title, String content) {
        this.date = date;
        this.title = title;
        this.content = content;
    }

    public Diary(String date, String title, String content, String tag) {
        this.date = date;
        this.title = title;
        this.content = content;
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Diary) {
            if (((Diary) obj).getId() == (getId())) {
                return true;
            }
        }
        return false;
    }
}
