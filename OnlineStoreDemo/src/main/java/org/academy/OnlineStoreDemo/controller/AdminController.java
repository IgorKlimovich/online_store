package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.model.User;
import org.academy.OnlineStoreDemo.service.UserService;
import org.academy.OnlineStoreDemo.service.UtilService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final UtilService utilService;

    public AdminController(UserService userService, UtilService utilService) {
        this.userService = userService;
        this.utilService = utilService;
    }


    @GetMapping
    public String getAdminPage(Model model) {
        model.addAttribute("user", new User());
        return "admin";
    }

    @GetMapping("/search")
    public String searchUser(@RequestParam("parameter") String parameter,
                             @RequestParam("name") String name, Model model) {
        if (parameter.equals("all")){
            List<User> users = userService.findAll();
            model.addAttribute("users" , users);
            return "users";
        }

       User user= utilService.findUserByParameters(parameter, name);
        if (user==null){
            model.addAttribute("error", "такого пользователя не существует");
            model.addAttribute("user", new User());
            return "/admin";
        }else {
            model.addAttribute("user" ,user);
        }
        return "user";
    }

    @GetMapping("/users")
    public String getUsersPage(@RequestParam("parameter") String parameter,
                               Model model){

        List<User> sortedUsers=utilService.sortUsersByParameters(userService.findAll(), parameter);
        model.addAttribute("users", sortedUsers);
        return "users";
    }

    @GetMapping("/user/{id}")
    public String getUserPage(@PathVariable Integer id, Model model){
        model.addAttribute("user", userService.findById(id));
        return "user";
    }


    @PostMapping("/user/{id}/ban")
    public String ban(@PathVariable("id") Integer id,Model model ){
        userService.toBan(id);
        model.addAttribute("user", userService.findById(id));
        return "redirect:/admin/user/{id}";
    }

    @PostMapping("/user/{id}/unban")
    public String unBan(@PathVariable("id") Integer id,Model model ){
        userService.unBan(id);
        model.addAttribute("user",userService.findById(id));
        return "redirect:/admin/user/{id}";
    }
}
