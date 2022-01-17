package org.academy.OnlineStoreDemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StateDto {

    private Integer id;

    private String name;

    private List<UserDto> usersDto;

    @Override
    public String toString() {
        return "StateDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
