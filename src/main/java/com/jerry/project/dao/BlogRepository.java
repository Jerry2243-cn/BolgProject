package com.jerry.project.dao;

import com.jerry.project.vo.Blog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog,Long>, JpaSpecificationExecutor<Blog>{

    @Query("select b from Blog b where b.recommend = true and b.published = true")
    List<Blog> findTop(Pageable pageable);

    @Query("select count(b) from Blog b where b.published = true")
    long findByPublishedBlogsCount();

    @Query("select function('date_format',b.createDate,'%Y') as yaer from Blog b group by function('date_format',b.createDate,'%Y') ")
    List<String> findGroupYear();

    @Query("select b from Blog b where function('date_format',b.createDate,'%Y') = ?1 and b.published = true")
    List<Blog> findByYear(String year);
}
