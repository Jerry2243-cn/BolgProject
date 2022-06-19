package com.jerry.project.service;

import com.jerry.project.cache.CacheProvider;
import com.jerry.project.vo.User;
import com.jerry.project.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CacheProvider cacheProvider;

    @Override
    public User checkUser(String username, String password) {
        return userRepository.findByUsernameAndPassword(username,password);
    }

    @Override
    public Page<User> listUser(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public List<User> ListUser() {
        List<User> list = userRepository.findAll();
        list.remove(0);
        for (int i = 0; i < list.size(); i++) {
            User user = list.get(i);
            user.setUsername(null);
            user.setPassword(null);
            list.set(i,user);
        }
        return list;
    }

    @Override
    public User getUser() {
        if (cacheProvider.get("adminUser") == null){
            User admin = userRepository.findById((long)1).get();
            cacheProvider.put("adminUser",admin);
        }
       return cacheProvider.get("adminUser");
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User save(User user) {
        if(user.getId() == 1){
            flushCache();
        }
        if(user.getId() == null){
            Date date = new Date();
            user.setCreateTime(date);
            user.setUpdateTime(date);
            return userRepository.save(user);
        }
        user.setCreateTime(getUser(user.getId()).getCreateTime());
        user.setUpdateTime(new Date());
        if("".equals(user.getPassword())) {
            user.setPassword(getUser(user.getId()).getPassword());
        }else if(!user.getPassword().equals(getUser(user.getId()).getPassword())){
            userRepository.save(user);
            return null;
        }
        return userRepository.save(user);
    }

    @Override
    public User delete(Long id) {
        if(!userRepository.hasBlogs(id).isEmpty()){
            return null;
        }
        User user = getUser(id);
        userRepository.delete(getUser(id));
        return user;
    }


    private void flushCache(){
        cacheProvider.remove("adminUser");
    }
}
