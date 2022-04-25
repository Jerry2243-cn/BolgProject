package com.jerry.project.service;

import com.jerry.project.vo.User;
import com.jerry.project.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        return userRepository.findByUsernameAndPassword(username,password);
    }

    @Override
    public User getUser() {
        return userRepository.findById((long)1).get();
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User save(User user) {
        user.setCreateTime(getUser().getCreateTime());
        user.setUpdateTime(new Date());
        if("".equals(user.getPassword())) {
            user.setPassword(getUser().getPassword());
        }else if(!user.getPassword().equals(getUser().getPassword())){
            userRepository.save(user);
            return null;
        }
        return userRepository.save(user);
    }


}
