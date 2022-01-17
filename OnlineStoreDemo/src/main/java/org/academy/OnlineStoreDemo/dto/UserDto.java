package org.academy.OnlineStoreDemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Integer id;

    @NotEmpty(message = "имя не может быть пустое")
    @Size(max = 100, message = "имя не может содержать более 100 символов")
    private String firstName;

    @NotEmpty(message = "фамилия не может быть пустой")
    @Size(max = 100, message = "фамилия не может содержать более 100 символов")
    private String lastName;

    @NotEmpty(message = "логин не может быть пустым ")
    @Size(max = 100, message = "фамилия не может содержать более 100 символов")
    private String login;

    @NotEmpty(message = "пароль не может быть пустым")
    @Size(max = 100, message = "пароль не может содержать более 100 символов")
    private String password;

    @NotEmpty(message = "email не может быть пустым")
    @Email(message = "email должен содержать символы @ и .")
    @Size(max = 100, message = "email не может содержать более 100 символов")
    private String email;

    @Pattern(regexp = "[^a-zA-Z]{10,15}",
            message = "номер не может содержать буквы и должен содержать от 10 до 15 цифр")
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
//                && Objects.equals(ordersDto, userDto.ordersDto) && Objects.equals(cardsDto, userDto.cardsDto);
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