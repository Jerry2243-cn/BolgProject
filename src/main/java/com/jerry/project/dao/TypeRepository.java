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

    @Query("select b from Blog b where b.published = true ")
    List<Blog> findPublishedBlogs();

    @Query("select b from Blog b where b.type.id = :id")
    List<Blog> hasBlogs(@Param("id") Long id);
}
