package com.jerry.project.service;

import com.jerry.project.vo.Comment;

import java.util.List;

public interface CommentService {

    Comment saveComment(Comment comment);

    List<Comment> getCommentsByBlogId(Long id);

    void delete(Long id);

    List<String> newComments(Long id);

    void setSawByBlogId(Long id);

    void deleteByBlogId(Long id);
}
