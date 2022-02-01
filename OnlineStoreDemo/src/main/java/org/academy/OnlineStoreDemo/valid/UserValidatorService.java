package org.academy.OnlineStoreDemo.valid;

import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.form.UserForm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserValidatorService {

    public String validateUser(List<UserDto> usersDto, UserForm userForm) {
        String message = "";
        if (usersDto.stream().anyMatch(item -> item.getEmail().equals(userForm.getEmail()))) {
            message = "existEmail";
        }
        if (usersDto.stream().anyMatch(item -> item.getPhoneNumber().equals(userForm.getPhoneNumber()))) {
            message = "existPhoneNumber";
        }
        if (usersDto.stream().anyMatch(item -> item.getLogin().equals(userForm.getLogin()))) {
            message = "existLogin";
        }
        return message;
    }
}
