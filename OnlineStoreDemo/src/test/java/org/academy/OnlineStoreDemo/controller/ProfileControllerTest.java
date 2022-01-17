package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.dto.CardDto;
import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.service.CardService;
import org.academy.OnlineStoreDemo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
class ProfileControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Mock
    Principal principal;

    @MockBean
    UserService userService;

    @MockBean
    CardService cardService;

    @MockBean
    PersistentTokenRepository persistentTokenRepository;

    private UserDto userDto;

    private UserDto userDto1;

    private UserDto userDto3;

    private List<UserDto> usersDto;

    private CardDto cardDto;

    private CardDto cardDto1;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        userDto = new UserDto();
        userDto.setId(1);
        userDto.setFirstName("firstName");
        userDto.setLastName("lastName");
        userDto.setEmail("email@ml.lm");
        userDto.setPhoneNumber("123456789123");
        userDto.setLogin("login");
        userDto.setPassword("password");
        cardDto = new CardDto();
        cardDto1 = new CardDto();
        cardDto.setName("cardName");
        cardDto.setNumber("1234512345123456");
        cardDto.setCvv("123");
        cardDto1.setId(2);
        cardDto1.setNumber("1111111111111111");
        List<CardDto> cardsDto = new ArrayList<>();
        cardsDto.add(cardDto1);
        userDto.setCardsDto(cardsDto);
        userDto1 = new UserDto();
        userDto1.setId(2);
        userDto1.setFirstName("firstName1");
        userDto1.setLastName("lastName1");
        userDto1.setEmail("email@ml.lm1");
        userDto1.setPhoneNumber("1123456789123");
        userDto1.setLogin("logi1n");
        userDto1.setPassword("password1");
        UserDto userDto2 = new UserDto();
        userDto2.setId(3);
        usersDto = new ArrayList<>();
        usersDto.add(userDto);
        usersDto.add(userDto1);
        userDto3 = new UserDto();
        userDto3.setId(4);
        userDto3.setNamePhoto("photo");
        userDto3.setLogin("login4");
        userDto.setPassword("password4");
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void getProfilePage() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(MockMvcResultMatchers.model().attribute("cardDto", new CardDto()))
                .andExpect(MockMvcResultMatchers.view().name("profile"))
                .andDo(MockMvcResultHandlers.print());
        verify(userService, times(2)).findByLogin(userDto.getLogin());
    }

    @Test
    void getProfilePageRedirect() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void updateUser() throws Exception {
        when(userService.findAll()).thenReturn(usersDto);
        mockMvc.perform(post("/profile/update")
                        .param("id", userDto.getId().toString())
                        .param("firstName", userDto.getFirstName())
                        .param("lastName", userDto.getLastName())
                        .param("email", userDto.getEmail())
                        .param("phoneNumber", userDto.getPhoneNumber())
                        .param("login", userDto.getLogin())
                        .param("password", userDto.getPassword()))
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.view().name("login"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void updateUserFailBindingResultError() throws Exception {
        when(userService.findAll()).thenReturn(usersDto);
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        mockMvc.perform(post("/profile/update")
                        .param("id", userDto.getId().toString())
                        .param("firstName", userDto.getFirstName())
                        .param("lastName", userDto.getLastName())
                        .param("email", "")
                        .param("phoneNumber", userDto.getPhoneNumber())
                        .param("login", userDto.getLogin())
                        .param("password", userDto.getPassword()))
                .andExpect(MockMvcResultMatchers.model().size(5))
                .andExpect(MockMvcResultMatchers.model().errorCount(1))
                .andExpect(MockMvcResultMatchers.view().name("profile"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void updateUserFailExistPhoneNumber() throws Exception {
        when(userService.findAll()).thenReturn(usersDto);
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        mockMvc.perform(post("/profile/update")
                        .param("id", userDto.getId().toString())
                        .param("firstName", userDto.getFirstName())
                        .param("lastName", userDto.getLastName())
                        .param("email", userDto.getEmail())
                        .param("phoneNumber", userDto1.getPhoneNumber())
                        .param("login", userDto.getLogin())
                        .param("password", userDto.getPassword()))
                .andExpect(MockMvcResultMatchers.model().size(6))
                .andExpect(MockMvcResultMatchers.model().attribute("existPhoneNumber", true))
                .andExpect(MockMvcResultMatchers.view().name("profile"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void saveCardFailBindingResultErrors() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        mockMvc.perform(post("/profile/save_card")
                        .param("name", cardDto.getName())
                        .param("number", "")
                        .param("cvv", cardDto.getCvv()))
                .andExpect(MockMvcResultMatchers.model().size(5))
                .andExpect(MockMvcResultMatchers.view().name("profile"))
                .andDo(MockMvcResultHandlers.print());
        assertEquals(1, userDto.getCardsDto().size());
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void saveCardFailExistCard() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        mockMvc.perform(post("/profile/save_card")
                        .param("name", cardDto.getName())
                        .param("number", "1111111111111111")
                        .param("cvv", cardDto.getCvv()))
                .andExpect(MockMvcResultMatchers.model().size(6))
                .andExpect(MockMvcResultMatchers.model().attribute("existCard", "такая какрта у вас уже есть"))
                .andExpect(MockMvcResultMatchers.view().name("profile"))
                .andDo(MockMvcResultHandlers.print());
        assertEquals(1, userDto.getCardsDto().size());
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void saveCard() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        mockMvc.perform(post("/profile/save_card")
                        .param("name", cardDto.getName())
                        .param("number", cardDto.getNumber())
                        .param("cvv", cardDto.getCvv()))
                .andExpect(MockMvcResultMatchers.model().size(5))
                .andExpect(MockMvcResultMatchers.view().name("profile"))
                .andDo(MockMvcResultHandlers.print());
        assertEquals(1, userDto.getCardsDto().size());
        verify(cardService, times(1)).save(cardDto, userDto);
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void removeCard() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        mockMvc.perform(post("/profile/remove_card/2"))
                .andExpect(MockMvcResultMatchers.model().size(5))
                .andExpect(MockMvcResultMatchers.view().name("profile"))
                .andDo(MockMvcResultHandlers.print());
        verify(cardService, times(1)).remove(2);
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void getUpdateCardForm() throws Exception {
        when(cardService.findById(2)).thenReturn(cardDto1);
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        mockMvc.perform(get("/profile/update_form/2"))
                .andExpect(MockMvcResultMatchers.model().size(5))
                .andExpect(MockMvcResultMatchers.view().name("profile"))
                .andDo(MockMvcResultHandlers.print());
        verify(cardService, times(1)).findById(2);
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void updateCard() throws Exception {
        when(cardService.findById(2)).thenReturn(cardDto1);
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        mockMvc.perform(post("/profile/update_card")
                        .param("name", cardDto.getName())
                        .param("number", cardDto.getNumber())
                        .param("cvv", cardDto.getCvv()))
                .andExpect(MockMvcResultMatchers.model().size(5))
                .andExpect(MockMvcResultMatchers.view().name("profile"))
                .andDo(MockMvcResultHandlers.print());
        verify(cardService, times(1)).update(cardDto);
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void updateCardFailBindingResultErrors() throws Exception {
        when(cardService.findById(2)).thenReturn(cardDto1);
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        mockMvc.perform(post("/profile/update_card")
                        .param("name", cardDto.getName())
                        .param("number", "")
                        .param("cvv", cardDto.getCvv()))
                .andExpect(MockMvcResultMatchers.model().size(5))
                .andExpect(MockMvcResultMatchers.model().errorCount(2))
                .andExpect(MockMvcResultMatchers.view().name("profile"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void addPhoto() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt",
                MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
        mockMvc.perform(multipart("/profile/add_photo").file(file))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(MockMvcResultMatchers.view().name("profile"))
                .andDo(MockMvcResultHandlers.print());
        verify(userService, times(1)).savePhoto("hello.txt", userDto);
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void deletePhoto() throws Exception {
        when(userService.findById(4)).thenReturn(userDto3);
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        mockMvc.perform(post("/profile/delete_photo")
                        .param("id", "4"))
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(MockMvcResultMatchers.view().name("profile"))
                .andDo(MockMvcResultHandlers.print());
        verify(userService, times(1)).update(userDto3);
    }
}