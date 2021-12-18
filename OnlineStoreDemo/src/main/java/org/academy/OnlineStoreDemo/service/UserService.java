package org.academy.OnlineStoreDemo.service;

import org.academy.OnlineStoreDemo.form.UserForm;
import org.academy.OnlineStoreDemo.model.entity.User;

import java.util.List;

public interface UserService {

    void create(UserForm userForm);

    void update(UserForm userForm, User user);

    User findByLogin(String login);

    User findByEmail(String email);

    User findByPhoneNumber(String phoneNumber);

    List<User> findAll();

    User findById(Integer id);

    void toBan(Integer id);

    void unBan(Integer id);

    void setDelete(String login);

    void setActive(String login);

    Boolean existsUserByEmail(String email);

    Boolean existsUserByLogin(String login);

    Boolean existsUserByPhoneNumber(String phoneNumber);
}
