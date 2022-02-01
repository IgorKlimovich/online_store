package org.academy.OnlineStoreDemo.service;

import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.exception.UserNotFoundException;
import org.academy.OnlineStoreDemo.form.UserForm;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface UserService {

    /**
     * Returns the authenticated user or a new one if the user is not authorized
     * @param principal user details authentication object
     * @return user by principal
     */
    UserDto findByLogin(Principal principal);

    /**
     * This method saves the user who came from the form and sends a welcome email
     * @param userForm user who came from the registration form
     */
    void create(UserForm userForm);

    /**
     * Update the user in the database
     * @param userForm user who came from the registration form
     * @param userDto user to whom the data from the form is assigned
     */
    void update(UserForm userForm, UserDto userDto);

    /**
     * Returns the user from database by login
     * @param login login user for search
     * @return user by login
     */
    UserDto findByLogin(String login);

    /**
     * Returns a list of all users
     * @return list of users
     */
    List<UserDto> findAll();

    /**
     * Returns the user from database by id
     * @param id id user for search
     * @return user by id
     * @throws UserNotFoundException if there is no card with the id in database
     */
    UserDto findById(Integer id);

    /**
     * This method updates the state of the user to 'BANNED'
     * @param id id user for update
     * @return an updated user
     */
    UserDto toBan(Integer id);

    /**
     * This method updates the state of the user to 'ACTIVE'
     * @param id id user for update
     * @return an updated user
     */
    UserDto unBan(Integer id);

    /**
     * This method updates the state of the user to 'DELETED'
     * @param login login user for update
     */
    void setDelete(String login);

    /**
     * This method updates the state of the user to 'ACTIVE'
     * @param login login user for update
     */
    void setActive(String login);

    /**
     * Checks if the user exists by email
     * @param email user email for search
     * @return true if user exist
     */
    Boolean existsUserByEmail(String email);

    /**
     * Checks if the user exists by login
     * @param login user login for search
     * @return true if user exist
     */
    Boolean existsUserByLogin(String login);

    /**
     * Checks if the user exists by phone number
     * @param phoneNumber user phone number for search
     * @return true if user exist
     */
    Boolean existsUserByPhoneNumber(String phoneNumber);

    /**
     * Adds a photo to the user
     * @param userDto to which the photo will be added
     * @param file photo for the user
     * @return updated user
     */
    UserDto savePhoto(MultipartFile file, UserDto userDto);

    /**
     * Deletes users photo
     * @param id id of the user to which the photo will be added
     * @return updated user
     */
    UserDto deletePhoto(Integer id);
}
