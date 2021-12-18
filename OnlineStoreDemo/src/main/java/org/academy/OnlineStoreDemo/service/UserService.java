package org.academy.OnlineStoreDemo.service;

import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.form.UserForm;
import org.academy.OnlineStoreDemo.model.entity.User;

import java.util.List;

public interface UserService {

    void create(UserForm userForm);

    void update(UserForm userForm, UserDto userDto);

    UserDto findByLogin(String login);

    User findByEmail(String email);

    UserDto findByPhoneNumber(String phoneNumber);

    List<UserDto> findAll();

    UserDto findById(Integer id);

    void toBan(Integer id);

    void unBan(Integer id);

    void setDelete(String login);

    void setActive(String login);

    Boolean existsUserByEmail(String email);

    Boolean existsUserByLogin(String login);

    Boolean existsUserByPhoneNumber(String phoneNumber);
}
