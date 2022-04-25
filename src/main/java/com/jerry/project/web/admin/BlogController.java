package com.jerry.project.web.admin;

import com.jerry.project.service.*;
import com.jerry.project.vo.BlogQuery;
import com.jerry.project.vo.User;
import com.jerry.project.vo.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class BlogController {

    public static final String INPUT = "admin/blogs-input";
    public static final String LIST = "admin/blogs";
    public static final String REDIRECT_LIST = "redirect:/admin/blogs";


    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @Autowired
    private FileService fileService;
    @Autowired
    private CommentService commentService;

    @GetMapping("blogs")
    public String blogs(@PageableDefault(size = 10,sort ={"createDate"},direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery bq, Model model, HttpSession session){
        User user = (User)session.getAttribute("user");
        bq.setUserId(user.getId());
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page",blogService.listBlog(pageable,bq));
        return LIST;
    }

    @PostMapping("blogs/search")
    public String search(@PageableDefault(size = 10,sort ={"createDate"},direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery bq, Model model, HttpSession session){
        User user = (User)session.getAttribute("user");
        bq.setUserId(user.getId());
        model.addAttribute("page",blogService.listBlog(pageable,bq));
        return "admin/blogs :: blogList";
    }

    @GetMapping("blogs/{id}/preview")
    public String preview(@PathVariable Long id,Model model){
        model.addAttribute("blog",blogService.getAndPreView(id));
        model.addAttribute("comments",commentService.getCommentsByBlogId(id));
        commentService.setSawByBlogId(id);
        return "admin/blog";
    }

    @GetMapping("blogs/{bId}/preview/{id}")
    public String deleteComment(@PathVariable Long id,@PathVariable Long bId){
        commentService.delete(id);
        return "redirect:/admin/blogs/"+bId+"/preview";
    }


    @GetMapping("/blogs/input")
    public String input(Model model){
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.ListTag());
        model.addAttribute("blog",new Blog());
        return INPUT;
    }

    @GetMapping("blogs/{id}/publish")
    public String changePublishState(@PathVariable Long id,RedirectAttributes attributes) {
        blogService.changePublishState(id);
        typeService.setPublishedCount();
        tagService.setPublishedCount();
        attributes.addFlashAttribute("message", "操作成功");
        return REDIRECT_LIST;
    }

    @GetMapping("blogs/{id}/comment")
    public String changeCommentState(@PathVariable Long id,RedirectAttributes attributes) {
        blogService.changeCommentState(id);
        attributes.addFlashAttribute("message", "操作成功");
        return REDIRECT_LIST;
    }

    @GetMapping("blogs/closeAllComments")
    public String closeAllComments(RedirectAttributes attributes, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if(user.getId() == 1){
            blogService.closeAllComments();
            attributes.addFlashAttribute("message", "操作成功");
        }else{
            attributes.addFlashAttribute("message", "只有Jerry才能关闭所有留言区");
        }
        return REDIRECT_LIST;
    }

    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id,Model model){
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.ListTag());
//        model.addAttribute("content", blogContentService.getBlogContent(id).getContent());
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        return INPUT;
    }

    @PostMapping("/blogs")
    public String post(@RequestParam(value = "file",required = false) MultipartFile file, Blog blog, RedirectAttributes attributes, HttpSession session){
        blog.setUser((User)session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.ListTag(blog.getTagIds()));
        Blog b;
        if(blog.getId()==null){
            if(!file.isEmpty()) {
                blog.setFirstPicture(fileService.saveFile(file));
            }
            b = blogService.saveBlog(blog);
        }else{
            if(!file.isEmpty()){
                blog.setFirstPicture(fileService.saveFile(file,blogService.getBlog(blog.getId()).getFirstPicture()));
            }
            b = blogService.updateBlog(blog.getId(),blog);
        }
        if (b == null){
            attributes.addFlashAttribute("message","操作失败");
        }else{
            attributes.addFlashAttribute("message","操作成功");
        }
        return REDIRECT_LIST;
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes) {
        if(commentService.getCommentsByBlogId(id) != null){
            commentService.deleteByBlogId(id);
        }
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return REDIRECT_LIST;
    }

}
