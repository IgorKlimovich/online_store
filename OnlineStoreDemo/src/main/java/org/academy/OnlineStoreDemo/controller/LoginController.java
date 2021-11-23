package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String getLoginPage(Model model, Authentication authentication,
                               HttpServletRequest httpServletRequest){
        if (httpServletRequest.getParameterMap().containsKey("error")){
            model.addAttribute("error", "такого пользователя не существует");
        }
        if (authentication!=null){
            return "redirect:/shop";
        }
        model.addAttribute("user", new User());
        return "login";
    }
}
