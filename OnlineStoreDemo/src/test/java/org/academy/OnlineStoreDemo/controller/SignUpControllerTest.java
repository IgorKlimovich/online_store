package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.form.UserForm;

import org.academy.OnlineStoreDemo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
class SignUpControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private UserForm userForm;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        userDto = new UserDto();
        userDto.setLogin("login");
        userDto.setEmail("email@mail.rr");
        userDto.setPhoneNumber("8765456787654");
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
                .andExpect(MockMvcResultMatchers.view().name("auth/sign-up"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void signUp() throws Exception {
        mockMvc.perform(post("/sign-up")
                        .param("firstName", userForm.getFirstName())
                        .param("lastName", userForm.getLastName())
                        .param("email", userForm.getEmail())
                        .param("phoneNumber", userForm.getPhoneNumber())
                        .param("login", userForm.getLogin())
                        .param("password", userForm.getPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.model().size(0))
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
                .andExpect(MockMvcResultMatchers.view().name("auth/sign-up"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void signUpFailExistByLogin() throws Exception {
        when(userService.findAll()).thenReturn(Collections.singletonList(userDto));
        mockMvc.perform(post("/sign-up")
                        .param("firstName", userForm.getFirstName())
                        .param("lastName", userForm.getLastName())
                        .param("email", userForm.getEmail())
                        .param("phoneNumber", userForm.getPhoneNumber())
                        .param("login", userForm.getLogin())
                        .param("password", userForm.getPassword()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.view().name("auth/sign-up"))
                .andDo(MockMvcResultHandlers.print());
    }
}