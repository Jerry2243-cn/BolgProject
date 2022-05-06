package com.jerry.project.web;

import com.jerry.project.service.*;
import com.jerry.project.vo.Blog;
import com.jerry.project.vo.BlogQuery;
import com.jerry.project.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import java.util.HashMap;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

@Controller
public class indexController {

    @Autowired
    private UserService userService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/")
    public String index(@PageableDefault(size = 8,sort = "createDate",direction = Sort.Direction.DESC)Pageable pageable,
                        Model model){
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setPublished(true);
        model.addAttribute("page",blogService.listBlog(pageable,blogQuery));
        model.addAttribute("types",typeService.listTypeTop(6));
        model.addAttribute("tags",tagService.ListTagTop(10));
        model.addAttribute("recommendBlogs",blogService.listRecommendBlogTop(8));
        return "index";
    }

    @ResponseBody
    @GetMapping("/json")
    public HashMap<String,Object> indexJson(@PageableDefault(size = 8,sort = "createDate",direction = Sort.Direction.DESC)Pageable pageable){
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setPublished(true);
        HashMap<String,Object> map = new HashMap<>();
        map.put("types",typeService.listTypeTop(6));
        map.put("tags",tagService.ListTagTop(10));
        map.put("recommendBlogs",blogService.listRecommendBlogTop(8));
       return map;
    }

    @ResponseBody
    @GetMapping("/blogJson/{id}")
    public HashMap<String,Object> blogJson(@PathVariable Long id ){
        HashMap<String,Object> map = new HashMap<>();
        Blog blog = blogService.getAndConvert(id);
        if(blog == null) {
            return null;
        }
        map.put("blog",blog);
        map.put("comments",commentService.getCommentsByBlogId(id));
        return map;
    }

    @PostMapping("/search")
    public String search(@PageableDefault(size = 8,sort = "createDate",direction = Sort.Direction.DESC)Pageable pageable,
                         @RequestParam String query, Model model){
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTitle("%"+query+"%");
        blogQuery.setPublished(true);
        model.addAttribute("page",blogService.listBlog(pageable,blogQuery));
        model.addAttribute("query",query);
        return "search";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id,Model model ){
        Blog blog = blogService.getAndConvert(id);
        if(blog == null) {
            return "error/404";
        }
        model.addAttribute("blog",blog);
        model.addAttribute("comments",commentService.getCommentsByBlogId(id));
        return "blog";
    }

    @GetMapping("/footer/info")
    public String getInfo(Model model) {
        User user = userService.getUser();
        model.addAttribute("wechatQRCode", user.getWeChatQRCode());
        model.addAttribute("newBlogs", blogService.listRecommendBlogTop(3));
        model.addAttribute("userEmail","E-mail: " + user.getEmail());
        model.addAttribute("userQQ","QQ: " + user.getQq());
        return "_fragments :: info";
    }

}