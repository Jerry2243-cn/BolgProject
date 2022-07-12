package com.jerry.project.service;

import com.jerry.project.dao.CommentRepository;
import com.jerry.project.vo.Blog;
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
    public List<Comment> getALl() {
        return commentRepository.findAll();
    }

    @Override
    public Comment saveComment(Comment comment) {
//        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getCommentsByBlogId(Long id) {
        List<Comment> comments = commentRepository.findByBlogId(id);
        if(comments.size() == 0){
            return null;
        }
        comments.sort((o1, o2) -> (o2.getCreateTime().getTime() > o1.getCreateTime().getTime() ? 0 : -1));
        return comments;

    }

    @Transactional
    @Override
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public List<NewComment> newComments(Long id) {
        List<NewComment> newCommentNotice = new ArrayList<>();
        List<Comment> comments = id != 1 ? commentRepository.findBySawFalseAndBlog_UserId(id) : commentRepository.findBySawFalse();
        if(comments.size() == 0){
            return null;
        }
        comments.sort((o1, o2) -> (o2.getCreateTime().getTime() > o1.getCreateTime().getTime() ? 0 : -11));
        int count = 0;
        String p = comments.get(0).getBlog().getTitle();
        for(int i = 0; i < comments.size();i++){
            if(p.equals(comments.get(i).getBlog().getTitle())){
                count++;
            }
            if(i == comments.size() - 1 || !p.equals(comments.get(i+1).getBlog().getTitle())){
                Blog bolg = comments.get(i).getBlog();
//                bolg.setContent("");
                newCommentNotice.add(new NewComment(bolg,count));
                count = 0;
                if(i != comments.size() - 1){
                    p = comments.get(i+1).getBlog().getTitle();
                }
            }
        }
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
