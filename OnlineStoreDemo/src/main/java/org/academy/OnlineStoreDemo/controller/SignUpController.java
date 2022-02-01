package org.academy.OnlineStoreDemo.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.form.UserForm;
import org.academy.OnlineStoreDemo.service.UserService;
import org.academy.OnlineStoreDemo.valid.UserValidatorService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import static org.academy.OnlineStoreDemo.constants.Constants.*;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/sign-up")
public class SignUpController {

    private final UserService userService;
    private final UserValidatorService userValidatorService;

    @GetMapping
    public String getSignUpPage(Model model, Authentication authentication) {
        if (authentication != null) {
            log.info("in get sign up page: user authenticated, return shop page");
            return "redirect:/shop";
        }
        model.addAttribute(USER_FORM, new UserForm());
        log.info("in get sign up page: return sign up page");
        return SIGN_UP;
    }

    @PostMapping
    public String signUp(@ModelAttribute("userForm") @Valid UserForm userForm, BindingResult bindingResult) {
        String message = userValidatorService.validateUser(userService.findAll(), userForm);
        if (!message.isEmpty()) {
            ObjectError objectError = new ObjectError(GLOBAL_ERRORS, message);
            bindingResult.addError(objectError);
        }
        if (bindingResult.hasErrors()) {
            log.warn("in sign up: binding result has {} errors", bindingResult.getErrorCount());
            return SIGN_UP;
        }
        userService.create(userForm);
        log.info("in sign up: sign up successfully for new user {}", userForm);
        return "redirect:/login";
    }
}
