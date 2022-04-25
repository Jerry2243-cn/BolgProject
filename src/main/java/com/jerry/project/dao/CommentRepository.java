package com.jerry.project.dao;

import com.jerry.project.vo.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findByBlogId(Long id);

    List<Comment> findBySawFalse();

    List<Comment> findBySawFalseAndBlog_UserId(Long id);
}
