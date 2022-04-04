package com.jerry.project.web;

import com.jerry.project.service.UserService;
import com.jerry.project.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AboutController {
    @Autowired
    private UserService userService;

    @RequestMapping("/about")
    public String loadUser(Model model){
        User user = userService.getUser();
        user.setPassword(null);
        user.setUsername(null);
        model.addAttribute("user",user);
        return "about";
    }
}
