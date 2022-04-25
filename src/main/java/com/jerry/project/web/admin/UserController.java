package com.jerry.project.web.admin;

import com.jerry.project.service.FileService;
import com.jerry.project.service.UserService;
import com.jerry.project.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    FileService fileService;

    @RequestMapping("/user")
    public String getUser(Model model,HttpSession session){
        User user0 = (User)session.getAttribute("user");
        User user = userService.getUser(user0.getId());
        user.setPassword(null);
        model.addAttribute("user",user);
        return "admin/user-settings";
    }

    @PostMapping("/submit")
    public String update(@RequestParam(value = "file", required = false) MultipartFile file,
                         @RequestParam(value = "file2", required = false) MultipartFile file2,
                         User user,
                         RedirectAttributes attributes,
                         HttpSession session){
        if(!file.isEmpty())
            user.setAvatar(fileService.saveFile(file,userService.getUser(user.getId()).getAvatar()));
        if(!file2.isEmpty())
            user.setWeChatQRCode(fileService.saveFile(file2,userService.getUser(user.getId()).getWeChatQRCode()));
        if(userService.save(user) == null){
            session.removeAttribute("user");
            return "admin/login";
        }
        user.setPassword(null);
        session.setAttribute("user",user);
        attributes.addFlashAttribute("message","操作成功");
        return "redirect:user";
    }

}
