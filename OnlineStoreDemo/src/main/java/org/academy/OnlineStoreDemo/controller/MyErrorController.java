package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.model.repository.UserRepository;
import org.academy.OnlineStoreDemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class MyErrorController implements ErrorController {

    @Autowired
    private UserService userService;

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model, Principal principal) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
               model.addAttribute("notFound", true);
            }
            if (statusCode==HttpStatus.FORBIDDEN.value()){
                model.addAttribute("forbidden", true);
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("errorServer", true);
            }
        }
        if (principal!=null){
            model.addAttribute("userProf",userService.findByLogin(principal.getName()));
        }
        return "error";
    }
}