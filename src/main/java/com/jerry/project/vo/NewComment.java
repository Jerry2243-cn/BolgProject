package com.jerry.project.vo;

public class NewComment {

    private Blog blog;
    private int count;


    public NewComment(Blog blog, int count) {
        this.blog = blog;
        this.count = count;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
