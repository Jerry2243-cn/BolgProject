package com.jerry.project.web;

import com.jerry.project.service.TagService;
import com.jerry.project.vo.Tag;
import com.jerry.project.service.BlogService;
import com.jerry.project.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TagViewController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TagService tagService;


    @GetMapping("/tags/{id}")
    public String Tags(@PageableDefault(size = 8, sort = {"updateDate"}, direction = Sort.Direction.DESC) Pageable pageable,
                        @PathVariable Long id, Model model) {
        tagService.setPublishedCount();
        List<Tag> tags = tagService.ListTagTop(10000);
        if (id == -1) {
            id = tags.get(0).getId();
        }
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setPublished(true);
        blogQuery.setTagId(id);
        model.addAttribute("tags", tags);
        model.addAttribute("page", blogService.listBlog(pageable,blogQuery));
        model.addAttribute("activeTagId", id);
        return "tags";
    }

}
