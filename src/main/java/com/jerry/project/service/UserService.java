package com.jerry.project.service;

import com.jerry.project.vo.User;

public interface UserService {

    User checkUser(String username, String password);

    User getUser();

    User save(User user);

}
