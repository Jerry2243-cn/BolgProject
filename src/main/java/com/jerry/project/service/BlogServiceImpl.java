package com.jerry.project.service;


import com.jerry.project.NotFoundException;
import com.jerry.project.cache.CacheProvider;
import com.jerry.project.dao.BlogRepository;
import com.jerry.project.vo.Blog;
import com.jerry.project.vo.Type;
import com.jerry.project.util.MarkdownUtils;
import com.jerry.project.util.MyBeanUtils;
import com.jerry.project.vo.BlogQuery;
import com.jerry.project.vo.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService{

    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private FileService fileService;

    @Autowired
    private CacheProvider cacheProvider;

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
        if(cacheProvider.get("listBlog") == null){
            Page<Blog> blogPage = blogRepository.findAll((Specification<Blog>) (root, cq, cb) -> {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicates.add(cb.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
                }
                if (blog.getUserId() != null && blog.getUserId() != 1) {
                    predicates.add(cb.equal(root.<User>get("user").get("id"), blog.getUserId()));
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
            }, pageable);
            cacheProvider.put("listBlog", blogPage);
        }
        return cacheProvider.get("listBlog");

    }

    @Override
    public Page<Blog> listBlogByUser(Pageable pageable, BlogQuery blog) {
        if(cacheProvider.get("listBlogByUser") == null){
            Page<Blog> blogPage =  blogRepository.findAll((Specification<Blog>) (root, cq, cb) -> {
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
            }, pageable);;
            cacheProvider.put("listBlogByUser", blogPage);
        }
        return cacheProvider.get("listBlogByUser");
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        if(size == 3){
            if(cacheProvider.get("listRecommendBlogTop3") == null){
                Sort sort = Sort.by(Sort.Direction.DESC,"createDate");
                Pageable pageable = PageRequest.of(0,size,sort);
                List<Blog> blogs = blogRepository.findTop(pageable);
                cacheProvider.put("listRecommendBlogTop3", blogs);
            }
            return cacheProvider.get("listRecommendBlogTop3");
        }else {
            if(cacheProvider.get("listRecommendBlogTop") == null){
                Sort sort = Sort.by(Sort.Direction.DESC,"createDate");
                Pageable pageable = PageRequest.of(0,size,sort);
                List<Blog> blogs = blogRepository.findTop(pageable);
                cacheProvider.put("listRecommendBlogTop", blogs);
            }
            return cacheProvider.get("listRecommendBlogTop");
        }
    }

    @Override
    public Map<String, List<Blog>> archiveBlogs() {
        if(cacheProvider.get("archiveBlogs") == null){
            List<String> years = blogRepository.findGroupYear();
            Map<String,List<Blog>> map = new HashMap<>();
            for(String year:years){
                map.put(year, blogRepository.findByYear(year));
            }
            cacheProvider.put("archiveBlogs",map);
        }
       return cacheProvider.get("archiveBlogs");
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        flushCache();
        if(blog.getId() == null){
            Date date = new Date();
            blog.setCreateDate(date);
            blog.setUpdateDate(date);
            blog.setViews(0);
        }else{
            blog.setUpdateDate(new Date());
        }

        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        flushCache();
        Blog b = blogRepository.findById(id).get();
        if (b == null) {
            throw new NotFoundException("The blog is not found");
        }
        BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateDate(new Date());
        return blogRepository.save(b);
    }


    @Override
    public void changePublishState(Long id) {
        flushCache();
        Blog t =blogRepository.findById(id).get();
        if(t == null){
            throw new NotFoundException("The blog is not found");
        }
        if(!t.isFirstPublish() && !t.isPublished()){
            t.setCreateDate(new Date());
            t.setFirstPublish(true);
        }
        t.setPublished(t.isPublished()? false:true);
        blogRepository.save(t);
    }


    @Override
    public void addView(Long id) {
        flushBlogs();
        blogRepository.addViews(id);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        flushCache();
        fileService.deleteFile(blogRepository.findById(id).get().getFirstPicture());
        blogRepository.deleteById(id);
    }

    @Override
    public long blogCount() {
        return blogRepository.findByPublishedBlogsCount();
    }

    @Override
    public void changeCommentState(Long id) {
        flushCache();
        Blog t =blogRepository.findById(id).get();
        if(t == null){
            throw new NotFoundException("The blog is not found");
        }
        t.setAllowComment(t.isAllowComment() ? false:true);
        blogRepository.save(t);
    }

    @Override
    public void closeAllComments() {
        flushCache();
        List<Blog> blogs  = blogRepository.findAll();
        for (Blog b: blogs
             ) {
            b.setAllowComment(false);
        }
        blogRepository.saveAll(blogs);
    }

    private void flushCache(){
        cacheProvider.remove("listBlog");
        cacheProvider.remove("listBlogByUser");
        cacheProvider.remove("listRecommendBlogTop");
        cacheProvider.remove("listRecommendBlogTop3");
        cacheProvider.remove("archiveBlogs");
    }

    private void flushBlogs(){
        cacheProvider.remove("listBlog");
    }

}
