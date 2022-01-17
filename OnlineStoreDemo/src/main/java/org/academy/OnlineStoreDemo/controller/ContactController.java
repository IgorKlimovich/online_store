package org.academy.OnlineStoreDemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.form.UserForm;
import org.academy.OnlineStoreDemo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/contacts")
public class ContactController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    public ContactController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public String getContactsPage(Principal principal, Model model){
        if (principal!=null){
        model.addAttribute("userProf" ,
                modelMapper.map(userService.findByLogin(principal.getName()), UserForm.class));}
        log.info("in get contacts page: return user to contact page");
        return "contacts";
    }
}

