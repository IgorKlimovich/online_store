package org.academy.OnlineStoreDemo.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.exception.UserNotFoundException;
import org.academy.OnlineStoreDemo.form.UserForm;
import org.academy.OnlineStoreDemo.mail.EmailService;
import org.academy.OnlineStoreDemo.model.entity.*;
import org.academy.OnlineStoreDemo.model.repository.UserRepository;
import org.academy.OnlineStoreDemo.service.UserService;
import org.academy.OnlineStoreDemo.service.UtilService;
import org.academy.OnlineStoreDemo.util.UtilListMapper;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND ="user not found";

    private final ModelMapper modelMapper;

    private final UtilService utilService;

    private final EmailService emailService;

    private final UserRepository userRepository;

    private final UtilListMapper utilListMapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto findByLogin(Principal principal) {
        if (principal == null) {
            return new UserDto();
        }
        return modelMapper.map(userRepository.findByLogin(principal.getName()), UserDto.class);
    }

    @Override
    public UserDto findByLogin(String login) {
        User user = userRepository.findUserByLogin(login).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        log.info("in find user by login: founded user {} by login {}", user, login);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional
    public void create(UserForm userForm) {
        userForm.setRole(new Role(1));
        userForm.setState(new State(1));
        userForm.setPassword(bCryptPasswordEncoder.encode(userForm.getPassword()));
        User user = modelMapper.map(userForm, User.class);
        userRepository.save(user);
        emailService.sendWelcomeMessage(user.getEmail(), user.getFirstName());
        log.info("in save user: user{} saved", user);
    }

    @Override
    @Transactional
    public void update(UserForm userForm, UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setEmail(userForm.getEmail());
        user.setLogin(userForm.getLogin());
        user.setPassword(bCryptPasswordEncoder.encode(userForm.getPassword()));
        userRepository.save(user);
        log.info("in update user: user {} updated", user);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        return utilListMapper.mapList(users, UserDto.class);
    }

    @Override
    public UserDto findById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        log.info("in find user by id: user {} founded by id {}", user, id);
        return modelMapper.map(user, UserDto.class);
    }

    public User findUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        log.info("in find user by id: user {} founded by id {}", user, id);
        return user;
    }

    @Override
    public UserDto toBan(Integer id) {
        User user = findUserById(id);
        user.setState(new State(2, "BANNED"));
        log.info("in ban user: user {} banned", user);
        return modelMapper.map(userRepository.save(user), UserDto.class);
    }

    @Override
    public UserDto unBan(Integer id) {
        User user = findUserById(id);
        user.setState(new State(1, "ACTIVE"));
        log.info("in unban user: user {} unbanned", user);
        return modelMapper.map(userRepository.save(user), UserDto.class);
    }

    @Override
    public void setDelete(String login) {
        User user = userRepository.findByLogin(login);
        user.setState(new State(3, "DELETED"));
        log.info("in set delete user: user {} set delete", user);
        modelMapper.map(userRepository.save(user), UserDto.class);
    }

    @Override
    public void setActive(String login) {
        User user = userRepository.findByLogin(login);
        user.setState(new State(1, "ACTIVE"));
        log.info("in set active user: user {} set active", user);
        modelMapper.map(userRepository.save(user), UserDto.class);
    }

    @Override
    public Boolean existsUserByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    @Override
    public Boolean existsUserByLogin(String login) {
        return userRepository.existsUserByLogin(login);
    }

    @Override
    public Boolean existsUserByPhoneNumber(String phoneNumber) {
        return userRepository.existsUserByPhoneNumber(phoneNumber);
    }

    @Override
    public UserDto savePhoto(MultipartFile file, UserDto userDto) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uploadDir = "./user-photos/" + userDto.getId();
        utilService.savePhotoWithPath(uploadDir, fileName, file);
        userDto.setNamePhoto(fileName);
        User user = modelMapper.map(userDto, User.class);
        log.info("in save photo name: photo name{} saved for user {}", fileName, user);
        return modelMapper.map(userRepository.save(user), UserDto.class);
    }

    public UserDto deletePhoto(Integer id) {
        User user = findUserById(id);
        user.setNamePhoto(null);
        return modelMapper.map(userRepository.save(user), UserDto.class);
    }
}
