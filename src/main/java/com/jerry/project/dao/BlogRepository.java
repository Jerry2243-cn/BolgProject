package com.jerry.project.dao;

import com.jerry.project.vo.Blog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional(rollbackFor = Exception.class)
public interface BlogRepository extends JpaRepository<Blog,Long>, JpaSpecificationExecutor<Blog>{

    @Query("select b from Blog b where b.recommend = true and b.published = true")
    List<Blog> findTop(Pageable pageable);

    @Query("select count(b) from Blog b where b.published = true")
    long findByPublishedBlogsCount();

    @Query("select function('date_format',b.createDate,'%Y') as yaer from Blog b group by function('date_format',b.createDate,'%Y') ")
    List<String> findGroupYear();

    @Query("select b from Blog b where function('date_format',b.createDate,'%Y') = ?1 and b.published = true")
    List<Blog> findByYear(String year);

    @Modifying
    @Query("update Blog b set b.views = b.views+1 where b.id = :id")
    void addViews(@Param("id")Long id);

    @Query("select b.views from Blog b where b.id = :id")
    int findBlogViewsById(@Param("id")Long id);
}
