package com.jerry.project.web.admin;

import com.jerry.project.service.FileService;
import com.jerry.project.service.UserService;
import com.jerry.project.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/users")
    public String list(@PageableDefault(size = 10,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable, Model model,HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user.getId() != 1){
            return "error/404";
        }
        model.addAttribute("page", userService.listUser(pageable));
        return "admin/users";
    }

    @GetMapping("/users/input")
    public String addInput(Model model, HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user.getId() != 1){
            return "error/404";
        }
        model.addAttribute("user",new User());
        return "admin/users-input";
    }


    @GetMapping("/users/{id}/input")
    public String editInput(@PathVariable Long id, Model model, HttpSession session){
        User checkUser = (User) session.getAttribute("user");
        if(checkUser.getId() != 1){
            return "error/404";
        }
        User user = userService.getUser(id);
        user.setPassword(null);
        model.addAttribute("user",user);
        return "admin/users-input";
    }

    @RequestMapping("/user")
    public String getUser(Model model,HttpSession session){
        User user0 = (User)session.getAttribute("user");
        User user = userService.getUser(user0.getId());
        user.setPassword(null);
        model.addAttribute("user",user);
        return "admin/user-settings";
    }

//    @PostMapping("/saveUser/{id}")
//    public String updateUser(@RequestParam(value = "file", required = false) MultipartFile file,
//                           @RequestParam(value = "file2", required = false) MultipartFile file2,
//                           User user,
//                           RedirectAttributes attributes,
//                           HttpSession session) {
//        return saveUser()
//    }

    @PostMapping("/saveUser")
    public String saveUser(@RequestParam(value = "file", required = false) MultipartFile file,
                           @RequestParam(value = "file2", required = false) MultipartFile file2,
                           User user,
                           RedirectAttributes attributes,
                           HttpSession session,
                           BindingResult result){
        User user1 = (User) session.getAttribute("user");
        if(user1.getId() != 1){
            return "error/404";
        }
        if(user.getId() == null && userService.getUser(user.getUsername()) != null ){
            result.rejectValue("username","nameError","该用户名已存在");
        }
        if (result.hasErrors()){
            return "admin/users-input";
        }
        if(!file.isEmpty())
            user.setAvatar(fileService.saveFile(file,userService.getUser(user.getId()).getAvatar()));
        if(!file2.isEmpty())
            user.setWeChatQRCode(fileService.saveFile(file2,userService.getUser(user.getId()).getWeChatQRCode()));
        userService.save(user);
        attributes.addFlashAttribute("message","操作成功");
        return "redirect:/admin/users";
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

    @GetMapping("/users/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        if(id == 1){
            attributes.addFlashAttribute("message", "管理员账户无法删除");
            return "redirect:/admin/users";
        }
        if(userService.delete(id) != null){
            attributes.addFlashAttribute("message", "删除成功");
        }else{
            attributes.addFlashAttribute("message", "删除失败，该用户有关联博客");
        }
        return "redirect:/admin/users";
    }

}
