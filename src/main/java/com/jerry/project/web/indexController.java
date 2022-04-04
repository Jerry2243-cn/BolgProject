package com.jerry.project.web;

import com.jerry.project.service.BlogService;
import com.jerry.project.service.TagService;
import com.jerry.project.service.TypeService;
import com.jerry.project.service.UserService;
import com.jerry.project.vo.Blog;
import com.jerry.project.vo.BlogQuery;
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

    @GetMapping("/")
    public String index(@PageableDefault(size = 8,sort = "updateDate",direction = Sort.Direction.DESC)Pageable pageable,
                        Model model){
        typeService.setPublishedCount();
        tagService.setPublishedCount();
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setPublished(true);
        model.addAttribute("page",blogService.listBlog(pageable,blogQuery));
        model.addAttribute("types",typeService.listTypeTop(6));
        model.addAttribute("tags",tagService.ListTagTop(10));
        model.addAttribute("recommendBlogs",blogService.listRecommendBlogTop(8));
        return "index";
    }

    @PostMapping("/search")
    public String search(@PageableDefault(size = 8,sort = "updateDate",direction = Sort.Direction.DESC)Pageable pageable,
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
        if(blog == null)
            return "error/404";
        model.addAttribute("blog",blog);
        return "blog";
    }

    @GetMapping("/footer/newBlogs")
    public String newBlogs(Model model) {
        model.addAttribute("newBlogs", blogService.listRecommendBlogTop(3));
        return "_fragments :: newBlogList";
    }

    @GetMapping("/footer/info")
    public String getInfo(Model model) {
        model.addAttribute("userEmail","E-mail: "+userService.getUser().getEmail());
        model.addAttribute("userQQ","QQ: "+userService.getUser().getQq());
        return "_fragments :: info";
    }

    @GetMapping("/footer/wechat")
    public String getWechat(Model model) {
        model.addAttribute("wechatQRCode", userService.getUser().getWeChatQRCode());
        return "_fragments :: wechat";
    }

}