package org.academy.OnlineStoreDemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

    private Integer id;

    private String name;

    private List<UserDto> usersDto;

    @Override
    public String toString() {
        return "RoleDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleDto roleDto = (RoleDto) o;
        return Objects.equals(id, roleDto.id) && Objects.equals(name, roleDto.name);
            //    && Objects.equals(usersDto.size(), roleDto.usersDto.size());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, usersDto);
    }
}
