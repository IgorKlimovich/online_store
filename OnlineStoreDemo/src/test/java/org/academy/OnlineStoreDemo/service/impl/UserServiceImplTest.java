package org.academy.OnlineStoreDemo.service.impl;

import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.exception.UserNotFoundException;
import org.academy.OnlineStoreDemo.form.UserForm;
import org.academy.OnlineStoreDemo.mail.EmailService;
import org.academy.OnlineStoreDemo.model.entity.User;
import org.academy.OnlineStoreDemo.model.repository.UserRepository;
import org.academy.OnlineStoreDemo.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private EmailService emailService;

    @Autowired
    private ModelMapper modelMapper;

    private User user;

    private UserForm userForm;

    private UserDto userDto;

    private List<User> users;

    @BeforeEach
    public void setUp() {
        users = new ArrayList<>();
        user = new User();
        user.setId(1);
        User user1 = new User();
        user1.setId(2);
        users.add(user);
        users.add(user1);
        userForm = new UserForm();
        userDto = new UserDto();
        userForm.setId(1);
        userForm.setEmail("1@1.re");
        userForm.setFirstName("user");
    }

    @Test
    void findByLogin() {
        user.setLogin("login");
        when(userRepository.findUserByLogin(("login"))).thenReturn(Optional.of(user));
        UserDto userDto = userService.findByLogin("login");
        verify(userRepository, times(1)).findUserByLogin("login");
        assertEquals("login", userDto.getLogin());
    }

    @Test
    void create() {
        userService.create(userForm);
        User user = modelMapper.map(userForm, User.class);
        assertNotNull(user.getRole());
        assertNotNull(user.getState());
        verify(userRepository, times(1)).save(user);
        verify(emailService, times(1))
                .sendWelcomeMessage(user.getEmail(), user.getFirstName());
    }

    @Test
    void update() {
        User user = modelMapper.map(userForm, User.class);
        userDto.setId(1);
        userService.update(userForm, userDto);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdate() {
        User user = modelMapper.map(userForm, User.class);
        userDto.setId(1);
        userService.update(userForm, userDto);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void findAll() {
        when(userRepository.findAll()).thenReturn(users);
        List<UserDto> userDtoList = userService.findAll();
        verify(userRepository, times(1)).findAll();
        assertEquals(2, userDtoList.size());
        assertEquals(1, userDtoList.get(0).getId());
    }

    @Test
    void findById() {
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));
        UserDto userDto = userService.findById(1);
        verify(userRepository, times(1)).findById(1);
        assertEquals(1, userDto.getId());
    }

    @Test()
    void findByIdFail() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        Exception exception = assertThrows(UserNotFoundException.class, () -> userService.findById(1));
        assertEquals("user not found", exception.getMessage());
    }

    @Test
    void toBan() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        userService.toBan(user.getId());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void unBan() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        userService.unBan(user.getId());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void setDelete() {
        user.setLogin("login");
        when(userRepository.findByLogin(user.getLogin())).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        userService.setDelete(user.getLogin());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void setActive() {
        user.setLogin("login");
        when(userRepository.findByLogin(user.getLogin())).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        userService.setActive(user.getLogin());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void existsUserByEmail() {
        when(userRepository.existsUserByEmail("email@email.by")).thenReturn(true);
        Boolean exist = userService.existsUserByEmail("email@email.by");
        verify(userRepository, times(1)).existsUserByEmail("email@email.by");
        assertTrue(exist);
    }

    @Test
    void existUserByEmailFail() {
        when(userRepository.existsUserByEmail("email@email.by")).thenReturn(false);
        Boolean notExist = userService.existsUserByEmail("email@email.by");
        verify(userRepository, times(1)).existsUserByEmail("email@email.by");
        assertFalse(notExist);
    }

    @Test
    void existsUserByLogin() {
        when(userRepository.existsUserByLogin("login")).thenReturn(true);
        Boolean exist = userService.existsUserByLogin("login");
        verify(userRepository, times(1)).existsUserByLogin("login");
        assertTrue(exist);
    }

    @Test
    void existUserByLoginFail() {
        when(userRepository.existsUserByLogin("login")).thenReturn(false);
        Boolean notExist = userService.existsUserByLogin("login");
        verify(userRepository, times(1)).existsUserByLogin("login");
        assertFalse(notExist);
    }

    @Test
    void existsUserByPhoneNumber() {
        when(userRepository.existsUserByPhoneNumber("12345")).thenReturn(true);
        Boolean exist = userService.existsUserByPhoneNumber("12345");
        verify(userRepository, times(1)).existsUserByPhoneNumber("12345");
        assertTrue(exist);
    }

    @Test
    void existUserBYPhoneNumberFail() {
        when(userRepository.existsUserByPhoneNumber("12345")).thenReturn(false);
        Boolean notExist = userService.existsUserByPhoneNumber("12345");
        verify(userRepository, times(1)).existsUserByPhoneNumber("12345");
        assertFalse(notExist);
    }

    @Test
    void savePhoto() {
        UserDto userDto = new UserDto();
        userDto.setId(1);
        when(userRepository.save(user)).thenReturn(user);
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt",
                MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
        userService.savePhoto(file, userDto);
        User user = modelMapper.map(userDto, User.class);
        verify(userRepository, times(1)).save(user);
        assertEquals("hello.txt", userDto.getNamePhoto());
    }
}