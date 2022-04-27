package com.jerry.project.dao;

import com.jerry.project.vo.Blog;
import com.jerry.project.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsernameAndPassword(String username,String password);

    User findByUsername(String username);

    @Query("select b from Blog b where b.user.id = :id")
    List<Blog> hasBlogs(@Param("id") Long id);
}
