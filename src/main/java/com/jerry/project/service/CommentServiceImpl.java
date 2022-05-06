package com.jerry.project.service;

import com.jerry.project.dao.CommentRepository;
import com.jerry.project.vo.Comment;
import com.jerry.project.vo.NewComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment saveComment(Comment comment) {
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getCommentsByBlogId(Long id) {
        List<Comment> comments = commentRepository.findByBlogId(id);
        if(comments.size() == 0){
            return null;
        }
        comments.sort((o1, o2) -> (int) (o2.getCreateTime().getTime() - o1.getCreateTime().getTime()));
        return comments;
    }

    @Transactional
    @Override
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    public List<NewComment> newComments(Long id) {
        List<NewComment> newCommentNotice = new ArrayList<>();
        List<Comment> comments = id != 1 ? commentRepository.findBySawFalseAndBlog_UserId(id) : commentRepository.findBySawFalse();
        if(comments.size() == 0){
            return null;
        }
        comments.sort((o1, o2) -> (int) (o2.getBlog().getCreateDate().getTime() - o1.getBlog().getCreateDate().getTime()));
        String bolg = comments.get(0).getBlog().getTitle();
        Long blogId = comments.get(0).getBlog().getId();
        int count = 1;
        for(int i = 0; i < comments.size()-1;i++){
            if(comments.get(i).getBlog().getTitle().equals(comments.get(i+1).getBlog().getTitle())){
                count++;
            }else {
                newCommentNotice.add(new NewComment(blogId.toString(),bolg,count));
                bolg = comments.get(i + 1).getBlog().getTitle();
                blogId = comments.get(i+1).getBlog().getId();
                count = 1;
            }
        }
        newCommentNotice.add(new NewComment(blogId.toString(),bolg,count));
        return newCommentNotice;
    }

    @Override
    public void setSawByBlogId(Long id) {
        List<Comment> comments = getCommentsByBlogId(id);
        if (comments == null) return;
        for(int i = 0; i < comments.size(); i++) {
            comments.get(i).setSaw(true);
        }
        commentRepository.saveAll(comments);
    }

    @Override
    public void deleteByBlogId(Long id) {
        List<Comment> comments = commentRepository.findByBlogId(id);
        commentRepository.deleteAll(comments);
    }
}
