package org.academy.OnlineStoreDemo.service.impl;

import org.academy.OnlineStoreDemo.dto.CardDto;
import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.model.entity.Card;
import org.academy.OnlineStoreDemo.model.entity.User;
import org.academy.OnlineStoreDemo.model.repository.CardRepository;
import org.academy.OnlineStoreDemo.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class CardServiceImplTest {

    @Autowired
    private CardService cardService;

    @MockBean
    private CardRepository cardRepository;

    @Autowired
    private ModelMapper modelMapper;

    private CardDto cardDto;

    private UserDto userDto;

    private List<CardDto> cardsDto;

    @BeforeEach
    public void setUp() {
        cardDto = new CardDto();
        CardDto cardDto1 = new CardDto();
        cardDto.setId(1);
        cardDto.setName("card name");
        cardDto.setNumber("1212312312312312");
        cardDto.setCvv("676");
        cardDto1.setId(2);
        userDto = new UserDto();
        userDto.setId(1);
        cardsDto = new ArrayList<>();
        cardsDto.add(cardDto);
        cardsDto.add(cardDto1);
        userDto.setCardsDto(cardsDto);
    }

    @Test
    void save() {
        UserDto userDto = new UserDto();
        userDto.setId(1);
        cardService.save(cardDto, userDto);
        System.out.println(cardDto.getUserDto());
        Card card = modelMapper.map(cardDto, Card.class);
        assertNotNull(card.getTotalAmount());
        assertNotNull(card.getUser());
        Mockito.verify(cardRepository, Mockito.times(1)).save(card);
    }

    @Test
    void findById() {
        Card card = new Card();
        card.setId(1);
        when(cardRepository.findById(1)).thenReturn(Optional.of(card));
        CardDto cardDto = cardService.findById(card.getId());
        verify(cardRepository, times(1)).findById(card.getId());
        assertEquals(1, cardDto.getId());
    }

    @Test
    void remove() {
        Card card = new Card();
        card.setId(1);
        cardService.remove(card.getId());
        verify(cardRepository, times(1)).deleteById(card.getId());
    }

    @Test
    void update() {
        Card card = new Card();
        card.setId(1);
        when(cardRepository.getById(cardDto.getId())).thenReturn(card);
        cardService.update(cardDto);
        System.out.println(card);
        assertEquals("676", card.getCvv());
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    void findAllByUser() {
        cardService.findAllByUser(userDto);
        User user = modelMapper.map(userDto, User.class);
        List<Card> cards = new ArrayList<>();
        for (CardDto cardDto : cardsDto) {
            Card card = modelMapper.map(cardDto, Card.class);
            cards.add(card);
        }
        when(cardRepository.findAllByUser(user)).thenReturn(cards);
        cards.forEach(System.out::println);
        verify(cardRepository, times(1)).findAllByUser(user);
        assertEquals(2, cards.size());
    }
}