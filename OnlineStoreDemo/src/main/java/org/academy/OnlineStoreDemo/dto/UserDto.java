package org.academy.OnlineStoreDemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Integer id;

    private String firstName;

    private String lastName;

    private String login;

    private String password;

    private String email;

    private String phoneNumber;

    private String namePhoto;

    private RoleDto roleDto;

    private StateDto stateDto;

    private List<OrderDto> ordersDto;

    private List<CardDto> cardsDto;

    public String getPhotosImagePath() {
        if (namePhoto == null || id == null) return null;
        return "/user-photos/" + id + "/" + namePhoto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(id, userDto.id) && Objects.equals(firstName, userDto.firstName)
                && Objects.equals(lastName, userDto.lastName) && Objects.equals(login, userDto.login)
                && Objects.equals(password, userDto.password) && Objects.equals(email, userDto.email)
                && Objects.equals(phoneNumber, userDto.phoneNumber) && Objects.equals(namePhoto, userDto.namePhoto)
                && Objects.equals(roleDto.getId(), userDto.roleDto.getId())
                && Objects.equals(stateDto.getId(), userDto.stateDto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, login, password, email, phoneNumber,
                namePhoto, roleDto, stateDto, ordersDto, cardsDto);
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", namePhoto='" + namePhoto + '\'' +
                '}';
    }
}