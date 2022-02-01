package org.academy.OnlineStoreDemo.exception;
import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.service.UserService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Slf4j
@ControllerAdvice
class GlobalDefaultExceptionHandler {
    public static final String DEFAULT_ERROR_VIEW = "error";

    private final UserService userService;

    GlobalDefaultExceptionHandler(UserService userService) {
        this.userService = userService;
    }

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e, Principal principal) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", e.getMessage());
        mav.addObject("url", req.getRequestURL());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        mav.addObject("userProf",userService.findByLogin(principal));
        log.error("in default error handler",e);
        return mav;
    }
}