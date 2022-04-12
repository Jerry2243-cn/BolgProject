package com.jerry.project.dao;

import com.jerry.project.vo.Tag;
import com.jerry.project.vo.Blog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Long> {

    @Query("select b from Blog b inner join b.tags t where t.id = :id")
    List<Blog> hasBlogs(@Param("id") Long id);

    Tag findByName(String name);

    @Query("select t from Tag t")
    List<Tag> findTop(Pageable pageable);

//    @Query("select b from Blog b where b.published = true ")
//    List<Blog> findPublishedBlogs();

    @Query("select count (b) from Blog b inner join b.tags t where t.id = :id and b.published = true ")
    int findPublishedBlogsCount(@Param("id") Long id);

}
