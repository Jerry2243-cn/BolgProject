package com.jerry.project.web;

import com.jerry.project.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArchiveController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/archives")
    public String archive(Model model){
        model.addAttribute("archiveMap",blogService.archiveBlogs());
        model.addAttribute("blogCount",blogService.blogCount());
        return "archives";
    }
}
