package org.academy.OnlineStoreDemo.exception;
import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.service.UserService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

import static org.academy.OnlineStoreDemo.constants.Constants.*;

@Slf4j
@ControllerAdvice
class GlobalDefaultExceptionHandler {

    private final UserService userService;

    GlobalDefaultExceptionHandler(UserService userService) {
        this.userService = userService;
    }

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e, Principal principal) {
        ModelAndView mav = new ModelAndView();
        mav.addObject(MESSAGE, e.getMessage());
        mav.addObject(URL, req.getRequestURL());
        mav.setViewName(ERROR);
        mav.addObject(USER_PROF,userService.findByLogin(principal));
        log.error("in default error handler",e);
        return mav;
    }
}