package org.academy.OnlineStoreDemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.academy.OnlineStoreDemo.model.entity.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {


    private Integer id;

    @NotEmpty(message = "{cardNameCanNotBeEmpty}")
    private String name;

    @NotEmpty(message = "{cardNumberCanNotBeEmpty}")
    @Pattern(regexp = "[0-9]{16}",
            message = "{numberMustContainDigits}")
    private String number;

    @Pattern(regexp = "[0-9]{3}",
            message = "{cvvMustContainDigits}")
    @NotEmpty(message = "{cvvCanNotBeEmpty}")
    private String cvv;

    private Double totalAmount;

    private UserDto userDto;

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", CVV='" + cvv + '\'' +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
