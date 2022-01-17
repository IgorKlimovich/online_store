package org.academy.OnlineStoreDemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.model.entity.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController {

    private static final String LOGIN="login";


    @GetMapping
    public String getLoginPage(Model model, Authentication authentication) {
        if (authentication != null) {
            log.info("in get login page: user authenticated, returned to main page");
            return "redirect:/shop";
        }
        model.addAttribute("user", new User());
        log.info("in get login page: user not authenticated, get login page");
        return LOGIN;
    }

    @PostMapping
    public String log(Model model, HttpServletRequest httpServletRequest) {
        if (httpServletRequest.getAttribute("error").equals("User is disabled")) {
            model.addAttribute("disabled", "Пользователь удален");
            String login = (String) httpServletRequest.getAttribute(LOGIN);
            model.addAttribute(LOGIN, login);
            log.warn("in login: user with login {} deleted",login);
        }
        if (httpServletRequest.getAttribute("error").equals("Bad credentials")) {
            model.addAttribute("badCredentials",true);
            log.warn("in login: incorrect login or password");
        }
        return LOGIN;
    }
}
