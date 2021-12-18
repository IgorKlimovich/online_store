package org.academy.OnlineStoreDemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {

    private Integer id;


    private String name;


    private String number;


    private String CVV;


    private Double totalAmount;

    private UserDto userDto;
}
