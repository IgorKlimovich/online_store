package org.academy.OnlineStoreDemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


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

    private RoleDto roleDto;

    private StateDto stateDto;

    private List<OrderDto> ordersDto;

    private List<CardDto> cardsDto;


}