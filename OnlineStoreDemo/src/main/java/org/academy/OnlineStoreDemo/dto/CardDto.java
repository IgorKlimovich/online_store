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

    @NotEmpty(message = "название не может быть пустым")
    private String name;

    @NotEmpty(message = "номер не может быть пустым")
    @Pattern(regexp = "[0-9]{16}",
            message = "номер должен содержать 16 цифр")
    private String number;

    @Pattern(regexp = "[0-9]{3}",
            message = "CVV должен содержать 3 цифры")
    @NotEmpty(message = "CVV не может быть пустым")
    private String cvv;

    private Double totalAmount;

    private User user;

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
