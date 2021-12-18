package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.model.entity.User;
import org.academy.OnlineStoreDemo.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getLoginPage(Model model, Authentication authentication,
                               HttpServletRequest httpServletRequest) {
        if (authentication != null) {
            return "redirect:/shop";
        }
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping
    public String log(Model model, HttpServletRequest httpServletRequest) {
        if (httpServletRequest.getAttribute("error").equals("User is disabled")) {
            model.addAttribute("disabled", "Пользователь удален");
            String login = (String) httpServletRequest.getAttribute("login");
            model.addAttribute("login", login);
        }
        if (httpServletRequest.getAttribute("error").equals("Bad credentials")) {
            model.addAttribute("badCredentials", "Логин или пароль введены неверно");
        }
        return "login";
    }
}
