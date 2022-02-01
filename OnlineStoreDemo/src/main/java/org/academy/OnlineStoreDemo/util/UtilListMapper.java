package org.academy.OnlineStoreDemo.util;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class UtilListMapper {

    private final ModelMapper modelMapper;

    public <S, T> List<T> mapList(List<S> list, Class<T> clazz) {

        return list.stream().map(item -> modelMapper.map(item, clazz)).collect(Collectors.toList());
    }
}
