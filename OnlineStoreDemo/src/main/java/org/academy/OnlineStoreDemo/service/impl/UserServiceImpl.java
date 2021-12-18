package org.academy.OnlineStoreDemo.service.impl;

import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.form.UserForm;
import org.academy.OnlineStoreDemo.model.entity.*;
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
    public UserDto findByLogin(String login) {
        User user= userRepository.findByLogin(login);
        return modelMapper.map(user,UserDto.class);
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
    public void update(UserForm userForm, UserDto userDto) {
        User user = modelMapper.map(userDto,User.class);
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
    public UserDto findByPhoneNumber(String phoneNumber) {
        User user=userRepository.findByPhoneNumber(phoneNumber);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        List<UserDto> usersDto = new ArrayList<>();
        for (User user : users) {
            UserDto map = modelMapper.map(user, UserDto.class);
            usersDto.add(map);
        }
        return usersDto;
    }

        @Override
        public UserDto findById (Integer id){
            Optional<User> candidate = userRepository.findById(id);
            User user= null;
            try {
                user = candidate.orElseThrow(Exception::new);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return modelMapper.map(user,UserDto.class);

        }

        @Override
        public void toBan (Integer id){
            User user = userRepository.getById(id);
            State state = new State();
            state.setId(2);
            state.setName("BANNED");
            user.setState(state);
            userRepository.save(user);
        }

        @Override
        public void unBan (Integer id){
            User user = userRepository.getById(id);
            State state = new State();
            state.setId(1);
            state.setName("ACTIVE");
            user.setState(state);
            userRepository.save(user);
        }

        @Override
        public void setDelete (String login){
            User user = userRepository.findByLogin(login);
            State state = new State();
            state.setId(3);
            state.setName("DELETED");
            user.setState(state);
            userRepository.save(user);
        }

        @Override
        public void setActive (String login){
            User user = userRepository.findByLogin(login);
            State state = new State();
            state.setId(1);
            state.setName("ACTIVE");
            user.setState(state);
            userRepository.save(user);
        }

        @Override
        public Boolean existsUserByEmail (String email){
            return userRepository.existsUserByEmail(email);
        }

        @Override
        public Boolean existsUserByLogin (String login){
            return userRepository.existsUserByLogin(login);
        }

        @Override
        public Boolean existsUserByPhoneNumber (String phoneNumber){
            return userRepository.existsUserByPhoneNumber(phoneNumber);
        }

    }

