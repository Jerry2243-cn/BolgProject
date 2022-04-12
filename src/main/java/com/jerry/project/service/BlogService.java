package com.jerry.project.service;

import com.jerry.project.vo.Blog;
import com.jerry.project.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {

    Blog getBlog(Long id);

    Blog getAndConvert(Long id);

    Blog getAndPreView(Long id);

    Page<Blog>  listBlog(Pageable pageable, BlogQuery blogQuery);

//    Page<Blog>  listBlog(Pageable pageable);

    List<Blog> listRecommendBlogTop(Integer size);

    Map<String,List<Blog>> archiveBlogs();

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id, Blog blog);

    void changePublishState(Long id);

    void addView(Long id);

    void deleteBlog(Long id);

    long blogCount();

    void changeCommentState(Long id);

    void closeAllComments();
}
