package org.academy.OnlineStoreDemo.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

import static org.academy.OnlineStoreDemo.constants.Constants.CONTACTS;
import static org.academy.OnlineStoreDemo.constants.Constants.USER_PROF;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/contacts")
public class ContactController {

    private final UserService userService;

    @GetMapping
    public String getContactsPage(Principal principal, Model model){
        model.addAttribute(USER_PROF,userService.findByLogin(principal));
        log.info("in get contacts page: return user to contact page");
        return CONTACTS;
    }
}

