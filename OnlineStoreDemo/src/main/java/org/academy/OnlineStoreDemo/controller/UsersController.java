package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.model.User;
import org.academy.OnlineStoreDemo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping
//    public String getUsersPage(Model model){
//        model.addAttribute("users", new ArrayList<User>());
//        return "users";
//    }
}
