package org.academy.OnlineStoreDemo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/resources/templates/");
        viewResolver.setSuffix(".html");
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).setViewResolvers(viewResolver).build();
    }

    @Test
    void getLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.view().name("auth/login"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void logFailUserDisabled() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .requestAttr("error", "User is disabled"))
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attribute("disabled", "Пользователь удален"))
                .andExpect(MockMvcResultMatchers.view().name("auth/login"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void logFailBadCredentials() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .requestAttr("error", "Bad credentials"))
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attribute("badCredentials", true))
                .andExpect(MockMvcResultMatchers.view().name("auth/login"))
                .andDo(MockMvcResultHandlers.print());
    }
}