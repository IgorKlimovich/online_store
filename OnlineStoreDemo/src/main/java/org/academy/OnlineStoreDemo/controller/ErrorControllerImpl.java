package org.academy.OnlineStoreDemo.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.service.UserService;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

import static org.academy.OnlineStoreDemo.constants.Constants.*;

@Slf4j
@AllArgsConstructor
@Controller
public class ErrorControllerImpl implements ErrorController {

    private final UserService userService;

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model, Principal principal) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                log.error("in handle error: 404 error {}", NOT_FOUND);
               model.addAttribute(NOT_FOUND, true);
            }
            if (statusCode==HttpStatus.FORBIDDEN.value()){
                model.addAttribute(FORBIDDEN, true);
                log.error("in handle error: 403 error {}", FORBIDDEN);
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute(ERROR_SERVER, true);
                log.error("in handle error: 500 error {}", ERROR_SERVER);
            }
        }
            model.addAttribute(USER_PROF,userService.findByLogin(principal));
        return ERROR_PAGE;
    }
}