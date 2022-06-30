package com.jerry.project;

import com.jerry.project.dao.CommentRepository;
import com.jerry.project.dao.TagRepository;
import com.jerry.project.service.CommentService;
import com.jerry.project.vo.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class MyProjectApplicationTests {

    @Autowired
    TagRepository tagRepository;
    @Test
    void contextLoads() {


    }

}
