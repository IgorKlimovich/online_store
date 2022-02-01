package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.dto.CardDto;
import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.model.entity.Card;
import org.academy.OnlineStoreDemo.model.entity.User;
import org.academy.OnlineStoreDemo.model.repository.UserRepository;
import org.academy.OnlineStoreDemo.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @MockBean
    CardService cardService;

    @MockBean
    PersistentTokenRepository persistentTokenRepository;

    @MockBean
    private UserRepository userRepository;

    private UserDto userDto;

    private UserDto userDto1;

    private List<User> users;

    private CardDto cardDto;

    private CardDto cardDto1;

    private User user;

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
        user = new User();
        user.setId(1);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email@ml.lm");
        user.setPhoneNumber("123456789123");
        user.setLogin("login");
        user.setPassword("password");
        cardDto = new CardDto();
        cardDto1 = new CardDto();
        cardDto.setName("cardName");
        cardDto.setNumber("1234512345123456");
        cardDto.setCvv("123");
        Card card = new Card();
        card.setName("cardName");
        card.setNumber("1234512345123456");
        card.setCvv("123");
        Card card1 = new Card();
        card1.setId(2);
        card1.setNumber("1111111111111111");
        List<Card> cards = new ArrayList<>();
        cards.add(card);
        cards.add(card1);
        user.setCards(cards);
        cardDto1.setId(2);
        cardDto1.setNumber("1111111111111111");
        List<CardDto> cardsDto = new ArrayList<>();
        cardsDto.add(cardDto1);
        cardsDto.add(cardDto);
        userDto.setCardsDto(cardsDto);
        userDto1 = new UserDto();
        userDto1.setId(2);
        userDto1.setFirstName("firstName1");
        userDto1.setLastName("lastName1");
        userDto1.setEmail("email@ml.lm1");
        userDto1.setPhoneNumber("1123456789123");
        userDto1.setLogin("logi1n");
        userDto1.setPassword("password1");
        User user1 = new User();
        user1.setId(2);
        user1.setFirstName("firstName1");
        user1.setLastName("lastName1");
        user1.setEmail("email@ml.lm1");
        user1.setPhoneNumber("1123456789123");
        user1.setLogin("logi1n");
        user1.setPassword("password1");
        users = new ArrayList<>();
        users.add(user);
        users.add(user1);
        UserDto userDto2 = new UserDto();
        userDto2.setId(3);
        UserDto userDto3 = new UserDto();
        userDto3.setId(4);
        userDto3.setNamePhoto("photo");
        userDto3.setLogin("login4");
        userDto.setPassword("password4");
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void getProfilePage() throws Exception {
        when(userRepository.findByLogin("login")).thenReturn(user);
        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(MockMvcResultMatchers.model().attribute("cardDto", new CardDto()))
                .andExpect(MockMvcResultMatchers.view().name("profile/profile"))
                .andDo(MockMvcResultHandlers.print());
        verify(userRepository, times(1)).findByLogin(userDto.getLogin());
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void updateUser() throws Exception {
        when(userRepository.findByLogin("login")).thenReturn(user);
        mockMvc.perform(post("/profile/update")
                        .param("id", userDto.getId().toString())
                        .param("firstName", userDto.getFirstName())
                        .param("lastName", userDto.getLastName())
                        .param("email", userDto.getEmail())
                        .param("phoneNumber", userDto.getPhoneNumber())
                        .param("login", userDto.getLogin())
                        .param("password", userDto.getPassword()))
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.view().name("auth/login"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void updateUserFailBindingResultError() throws Exception {
        when(userRepository.findByLogin("login")).thenReturn(user);
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
                .andExpect(MockMvcResultMatchers.view().name("profile/profile"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void updateUserFailExistPhoneNumber() throws Exception {
        when(userRepository.findByLogin("login")).thenReturn(user);
        when(userRepository.findAll()).thenReturn(users);
        mockMvc.perform(post("/profile/update")
                        .param("id", userDto.getId().toString())
                        .param("firstName", userDto.getFirstName())
                        .param("lastName", userDto.getLastName())
                        .param("email", userDto.getEmail())
                        .param("phoneNumber", userDto1.getPhoneNumber())
                        .param("login", userDto.getLogin())
                        .param("password", userDto.getPassword()))
                .andExpect(MockMvcResultMatchers.model().size(5))
                .andExpect(MockMvcResultMatchers.view().name("profile/profile"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void saveCardFailBindingResultErrors() throws Exception {
        when(userRepository.findByLogin("login")).thenReturn(user);
        mockMvc.perform(post("/profile/save_card")
                        .param("name", cardDto.getName())
                        .param("number", "")
                        .param("cvv", cardDto.getCvv()))
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(MockMvcResultMatchers.view().name("profile/profile"))
                .andDo(MockMvcResultHandlers.print());
        assertEquals(2, userDto.getCardsDto().size());
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void saveCardFailExistCard() throws Exception {
        when(userRepository.findByLogin("login")).thenReturn(user);
        mockMvc.perform(post("/profile/save_card")
                        .param("name", cardDto.getName())
                        .param("number", "1111111111111111")
                        .param("cvv", cardDto.getCvv()))
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(MockMvcResultMatchers.view().name("profile/profile"))
                .andDo(MockMvcResultHandlers.print());
        assertEquals(2, userDto.getCardsDto().size());
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void saveCard() throws Exception {
        when(userRepository.findByLogin("login")).thenReturn(user);
        mockMvc.perform(post("/profile/save_card")
                        .param("name", cardDto.getName())
                        .param("number", "9878987656789321")
                        .param("cvv", cardDto.getCvv()))
                .andExpect(MockMvcResultMatchers.model().size(5))
                .andExpect(MockMvcResultMatchers.view().name("profile/profile"))
                .andDo(MockMvcResultHandlers.print());
        assertEquals(2, userDto.getCardsDto().size());
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void removeCard() throws Exception {
        when(userRepository.findByLogin("login")).thenReturn(user);
        when(cardService.findAllByUser(userDto)).thenReturn(Collections.singletonList(cardDto));
        mockMvc.perform(post("/profile/remove_card/2"))
                .andExpect(MockMvcResultMatchers.model().size(5))
                .andExpect(MockMvcResultMatchers.view().name("profile/profile"))
                .andDo(MockMvcResultHandlers.print());
        verify(cardService, times(1)).remove(2);
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void getUpdateCardForm() throws Exception {
        when(cardService.findById(2)).thenReturn(cardDto1);
        when(userRepository.findByLogin("login")).thenReturn(user);
        mockMvc.perform(get("/profile/update_form/2"))
                .andExpect(MockMvcResultMatchers.model().size(5))
                .andExpect(MockMvcResultMatchers.view().name("profile/profile"))
                .andDo(MockMvcResultHandlers.print());
        verify(cardService, times(1)).findById(2);
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void updateCard() throws Exception {
        cardDto.setNumber("1259856947386940");
        when(cardService.findById(2)).thenReturn(cardDto1);
        when(userRepository.findByLogin("login")).thenReturn(user);
        mockMvc.perform(post("/profile/update_card")
                        .param("name", cardDto.getName())
                        .param("number", cardDto.getNumber())
                        .param("cvv", cardDto.getCvv()))
                .andExpect(MockMvcResultMatchers.model().size(5))
                .andExpect(MockMvcResultMatchers.view().name("profile/profile"))
                .andDo(MockMvcResultHandlers.print());
        verify(cardService, times(1)).update(cardDto);
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void updateCardFailBindingResultErrors() throws Exception {
        when(cardService.findById(2)).thenReturn(cardDto1);
        when(userRepository.findByLogin("login")).thenReturn(user);
        mockMvc.perform(post("/profile/update_card")
                        .param("name", cardDto.getName())
                        .param("number", "")
                        .param("cvv", cardDto.getCvv()))
                .andExpect(MockMvcResultMatchers.model().size(5))
                .andExpect(MockMvcResultMatchers.model().errorCount(2))
                .andExpect(MockMvcResultMatchers.view().name("profile/profile"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void addPhoto() throws Exception {
        when(userRepository.findByLogin("login")).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt",
                MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
        mockMvc.perform(multipart("/profile/add_photo").file(file))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(MockMvcResultMatchers.view().name("profile/profile"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void deletePhoto() throws Exception {
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByLogin("login")).thenReturn(user);
        mockMvc.perform(post("/profile/delete_photo")
                        .param("id", "1"))
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(MockMvcResultMatchers.view().name("profile/profile"))
                .andDo(MockMvcResultHandlers.print());
    }
}