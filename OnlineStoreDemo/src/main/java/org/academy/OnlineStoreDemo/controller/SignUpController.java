package org.academy.OnlineStoreDemo.controller;


import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.form.UserForm;
import org.academy.OnlineStoreDemo.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/sign-up")
public class SignUpController {

    private static final String SIGN_UP="sign-up";
    private final UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getSignUpPage(Model model, Authentication authentication){
        if (authentication!=null){
            log.info("in get sign up page: user authenticated, return shop page");
            return "redirect:/shop";
        }
        model.addAttribute("userForm", new UserForm());
        log.info("in get sign up page: return sign up page");
        return SIGN_UP;
    }

    @PostMapping
    public String signUp(@ModelAttribute("userForm") @Valid UserForm userForm, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            log.warn("in sign up: binding result has {} errors ",bindingResult.getErrorCount());
            return SIGN_UP;
        }
        if (Boolean.TRUE.equals(userService.existsUserByLogin(userForm.getLogin().trim()))){
            model.addAttribute("existLogin", true);
            log.warn("in sign up: login {} already exist",userForm.getLogin());
            return SIGN_UP;
        }
        if (Boolean.TRUE.equals(userService.existsUserByEmail(userForm.getEmail().trim()))){
            model.addAttribute("existEmail", true);
            log.warn("in sign up: email {} already exist",userForm.getEmail());
            return SIGN_UP;
        }
        if (Boolean.TRUE.equals(userService.existsUserByPhoneNumber(userForm.getPhoneNumber().trim()))){
            model.addAttribute("existPhoneNumber", true);
            log.warn("in sign up: phone number {} already exist",userForm.getPhoneNumber());
            return SIGN_UP;
        }
        userService.create(userForm);
        log.info("in sign up: sign up successfully for new user {}", userForm);
        return "redirect:/login";
    }
}
