package org.academy.OnlineStoreDemo.controller;


import org.academy.OnlineStoreDemo.form.UserForm;
import org.academy.OnlineStoreDemo.service.UserService;
import org.academy.OnlineStoreDemo.service.UtilService;
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

    private final UserService userService;
    private final UtilService utilService;

    public SignUpController(UserService userService, UtilService utilService) {
        this.userService = userService;
        this.utilService = utilService;
    }

    @GetMapping
    public String getSignUpPage(Model model){
        model.addAttribute("userForm", new UserForm());
        return "sign-up";
    }

    @PostMapping
    public String signUp(@ModelAttribute @Valid UserForm userForm,
                         BindingResult bindingResult, Model model){
        System.out.println(userForm);
        if (bindingResult.hasErrors()){
            return "sign-up";
        }
        if (utilService.isExistUserByLogin(userForm.getLogin())){
            model.addAttribute("existLogin", "такой логин уже существует");
            return "sign-up";
        }
        if (utilService.isExistUserByEmail(userForm.getEmail())){
            model.addAttribute("existEmail", "такой email уже существует");
            return "sign-up";
        }

        if (utilService.isExistUserByPhoneNumber(userForm.getPhoneNumber())){
            model.addAttribute("existPhoneNumber", "такой номер уже существует");
            return "sign-up";
        }

        userService.create(userForm);
        return "redirect:/login";
    }
}
