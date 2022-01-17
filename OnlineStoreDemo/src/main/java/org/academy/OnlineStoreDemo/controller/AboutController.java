package org.academy.OnlineStoreDemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/about")
public class AboutController {


    private final UserService userService;

    public AboutController( UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getAboutPage(Principal principal, Model model){
        if (principal!=null){
        model.addAttribute("userProf" , userService.findByLogin(principal.getName()));}
        log.info("in get about page: return user to about page");
        return "about";
    }
}
