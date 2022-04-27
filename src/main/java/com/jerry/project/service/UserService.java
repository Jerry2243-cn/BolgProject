package com.jerry.project.service;

import com.jerry.project.vo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserService {

    User checkUser(String username, String password);

    Page<User> listUser(Pageable pageable);

    User getUser();

    User getUser(Long id);

    User getUser(String username);

    User save(User user);

    User delete(Long id);

}
