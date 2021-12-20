package org.academy.OnlineStoreDemo.controller;


import org.academy.OnlineStoreDemo.form.UserForm;
import org.academy.OnlineStoreDemo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;


@Controller
@RequestMapping("/sign-up")
public class SignUpController {

    private static final String SIGN_UP="sign-up";
    private final UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getSignUpPage(Model model){
        model.addAttribute("userForm", new UserForm());
        return SIGN_UP;
    }

    @PostMapping
    public String signUp(@ModelAttribute("userForm") @Valid UserForm userForm, BindingResult bindingResult, Model model){

        if (bindingResult.hasErrors()){
            return SIGN_UP;
        }
        if (Boolean.TRUE.equals(userService.existsUserByLogin(userForm.getLogin()))){
            model.addAttribute("existLogin", "такой логин уже существует");
            return SIGN_UP;
        }
        if (Boolean.TRUE.equals(userService.existsUserByEmail(userForm.getEmail()))){
            model.addAttribute("existEmail", "такой email уже существует");
            return SIGN_UP;
        }

        if (Boolean.TRUE.equals(userService.existsUserByPhoneNumber(userForm.getPhoneNumber()))){
            model.addAttribute("existPhoneNumber", "такой номер уже существует");
            return SIGN_UP;
        }

        userService.create(userForm);
        return "redirect:/login";
    }
}
