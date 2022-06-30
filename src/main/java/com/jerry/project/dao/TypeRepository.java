package com.jerry.project.dao;

import com.jerry.project.vo.Blog;
import com.jerry.project.vo.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface TypeRepository extends JpaRepository <Type,Long> {

    Type findByName(String name);

    @Query("select t from Type t ")
    List<Type> findTop(Pageable pageable);

    @Query("select count (b) from Blog b where b.published = true and b.type.id = :id")
    int findPublishedBlogs(@Param("id") Long id);

    @Query("select b.type from Blog b where b.id = :id")
    Type findByBlogId(@Param("id") Long id);

    @Query("select b from Blog b where b.type.id = :id")
    List<Blog> hasBlogs(@Param("id") Long id);
}
