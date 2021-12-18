package org.academy.OnlineStoreDemo.service.impl;

import org.academy.OnlineStoreDemo.form.UserForm;
import org.academy.OnlineStoreDemo.model.entity.Order;
import org.academy.OnlineStoreDemo.model.entity.Role;
import org.academy.OnlineStoreDemo.model.entity.State;
import org.academy.OnlineStoreDemo.model.entity.User;
import org.academy.OnlineStoreDemo.model.repository.UserRepository;
import org.academy.OnlineStoreDemo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }


    @Override
    public void create(UserForm userForm) {
        User user = new User();
        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());
        user.setEmail(userForm.getEmail());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setLogin(userForm.getLogin());
        user.setPassword(bCryptPasswordEncoder.encode(userForm.getPassword()));
        Role role = new Role();
        State state = new State();
        List<Order> orderList = new ArrayList<>();
        state.setId(1);
        role.setId(1);
        user.setRole(role);
        user.setState(state);
        user.setOrders(orderList);

        userRepository.save(user);

    }

    @Override
    public void update(UserForm userForm, User user){

        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setEmail(userForm.getEmail());
        user.setLogin(userForm.getLogin());
        user.setPassword(bCryptPasswordEncoder.encode(userForm.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            try {
                throw new NullPointerException("user not found");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }
        return user;
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Integer id) {
        Optional<User> candidate = userRepository.findById(id);
        return candidate.orElseGet(User::new);
    }

    @Override
    public void toBan(Integer id) {
        User user = findById(id);
        State state = new State();
        state.setId(2);
        state.setName("BANNED");
        user.setState(state);
        userRepository.save(user);
    }

    @Override
    public void unBan(Integer id) {
        User user = findById(id);
        State state = new State();
        state.setId(1);
        state.setName("ACTIVE");
        user.setState(state);
        userRepository.save(user);
    }

    @Override
    public void setDelete(String login) {
        User user = findByLogin(login);
        State state = new State();
        state.setId(3);
        state.setName("DELETED");
        user.setState(state);
        userRepository.save(user);
    }

    @Override
    public void setActive(String login) {
        User user = findByLogin(login);
        State state = new State();
        state.setId(1);
        state.setName("ACTIVE");
        user.setState(state);
        userRepository.save(user);
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

}

