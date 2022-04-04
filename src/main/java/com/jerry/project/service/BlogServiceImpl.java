package com.jerry.project.service;


import com.jerry.project.NotFoundException;
import com.jerry.project.dao.BlogRepository;
import com.jerry.project.vo.Blog;
import com.jerry.project.vo.Type;
import com.jerry.project.util.MarkdownUtils;
import com.jerry.project.util.MyBeanUtils;
import com.jerry.project.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService{

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Blog getBlog(Long id) {
        Blog blog = null;
        Optional<Blog> blogCheck = blogRepository.findById(id);
        if(blogCheck.isPresent()){
            blog=blogCheck.get();
        }else{
            return null;
        }
        return blog;
    }

    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = null;
        Optional<Blog> blogCheck = blogRepository.findById(id);
        if(blogCheck.isPresent()){
            blog=blogCheck.get();
        }else{
            return null;
        }
        if(!blog.isPublished()){
            throw new NotFoundException("The bolg is not public");
        }else{
            addView(id);
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        return b;
    }

    @Override
    public Blog getAndPreView(Long id) {
        Blog blog = null;
        Optional<Blog> blogCheck = blogRepository.findById(id);
        if(blogCheck.isPresent()){
            blog=blogCheck.get();
        }else{
            return null;
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        return b;
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {

        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicates.add(cb.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
                }
                if (blog.getTypeId() != null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if(blog.getTagId()!=null){
                    Join join = root.join("tags");
                    predicates.add(cb.equal(join.get("id"),blog.getTagId()));
                }
                if (blog.isRecommend()) {
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                if(blog.isPublished()){
                    predicates.add(cb.equal(root.<Boolean>get("published"), blog.isPublished()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);

    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"updateDate");
        Pageable pageable = PageRequest.of(0,size,sort);
        return blogRepository.findTop(pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlogs() {
        List<String> years = blogRepository.findGroupYear();
        Map<String,List<Blog>> map = new HashMap<>();
        for(String year:years){
            map.put(year,blogRepository.findByYear(year));
        }
        return map;
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if(blog.getId() == null){
            blog.setCreateDate(new Date());
            blog.setUpdateDate(new Date());
            blog.setViews(0);
        }else{
            blog.setUpdateDate(new Date());
        }

        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.findById(id).get();
        if (b == null) {
            throw new NotFoundException("The blog is not found");
        }
        BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateDate(new Date());
        return blogRepository.save(b);
    }


    @Override
    public Blog changePublishState(Long id) {
        Blog t =blogRepository.findById(id).get();
        if(t == null){
            throw new NotFoundException("The blog is not found");
        }
        t.setPublished(t.isPublished()? false:true);
        return blogRepository.save(t);
    }

    @Override
    public void addView(Long id) {
        Blog blog = blogRepository.findById(id).get();
        blog.setViews(blog.getViews()+1);
        blogRepository.save(blog);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public long blogCount() {
        return blogRepository.findByPublishedBlogsCount();
    }

}