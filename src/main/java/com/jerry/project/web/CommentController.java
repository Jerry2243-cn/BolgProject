package com.jerry.project.web;

import com.jerry.project.service.BlogService;
import com.jerry.project.service.CommentService;
import com.jerry.project.util.IPUtils;
import com.jerry.project.vo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private BlogService blogService;

    @PostMapping("/blog/sendComment")
    public String sendComment( Comment comment,
                              Long blogId,
                              HttpServletRequest httpServletRequest,
                               Model model) throws Exception {
        comment.setBlog(blogService.getBlog(blogId));
        comment.setIp(httpServletRequest.getRemoteAddr());
        comment.setIpAddress(IPUtils.getCommentIp(httpServletRequest.getRemoteAddr()));
        commentService.saveComment(comment);
        model.addAttribute("comments",commentService.getCommentsByBlogId(blogId));
        return "blog :: commentList";
    }



}
