package com.jerry.project.vo;

public class NewComment {

    private String id;
    private String blog;
    private int count;

    public NewComment(String id, String blog, int count) {
        this.id = id;
        this.blog = blog;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
