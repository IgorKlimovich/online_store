package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.form.UserForm;

import org.academy.OnlineStoreDemo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SignUpControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private SignUpController signUpController;

    private MockMvc mockMvc;

    private UserForm userForm;

    @BeforeEach
    void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/resources/templates/");
        viewResolver.setSuffix(".html");
        mockMvc = MockMvcBuilders.standaloneSetup(signUpController).setViewResolvers(viewResolver).build();
        userForm = new UserForm();
        userForm.setFirstName("name");
        userForm.setLastName("last");
        userForm.setEmail("mail@mn.yy");
        userForm.setPhoneNumber("123456789123");
        userForm.setLogin("login");
        userForm.setPassword("pass");
    }

    @Test
    void getSignUpPage() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attribute("userForm", new UserForm()))
                .andExpect(MockMvcResultMatchers.view().name("sign-up"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void signUp() throws Exception {
        when(userService.existsUserByLogin(userForm.getLogin())).thenReturn(false);
        when(userService.existsUserByEmail(userForm.getEmail())).thenReturn(false);
        when(userService.existsUserByPhoneNumber(userForm.getPhoneNumber())).thenReturn(false);
        mockMvc.perform(post("/sign-up")
                        .param("firstName", userForm.getFirstName())
                        .param("lastName", userForm.getLastName())
                        .param("email", userForm.getEmail())
                        .param("phoneNumber", userForm.getPhoneNumber())
                        .param("login", userForm.getLogin())
                        .param("password", userForm.getPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"))
                .andDo(MockMvcResultHandlers.print());
        verify(userService, times(1)).create(userForm);
    }

    @Test
    void signUpFailBindingResultError() throws Exception {
        mockMvc.perform(post("/sign-up")
                        .param("firstName", "")
                        .param("lastName", userForm.getLastName())
                        .param("email", userForm.getEmail())
                        .param("phoneNumber", userForm.getPhoneNumber())
                        .param("login", userForm.getLogin())
                        .param("password", userForm.getPassword()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.view().name("sign-up"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void signUpFailExistByLogin() throws Exception {
        when(userService.existsUserByLogin(userForm.getLogin())).thenReturn(true);
        mockMvc.perform(post("/sign-up")
                        .param("firstName", userForm.getFirstName())
                        .param("lastName", userForm.getLastName())
                        .param("email", userForm.getEmail())
                        .param("phoneNumber", userForm.getPhoneNumber())
                        .param("login", userForm.getLogin())
                        .param("password", userForm.getPassword()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attribute("existLogin", true))
                .andExpect(MockMvcResultMatchers.view().name("sign-up"))
                .andDo(MockMvcResultHandlers.print());
        verify(userService, times(1)).existsUserByLogin(userForm.getLogin());
    }
}