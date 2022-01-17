package org.academy.OnlineStoreDemo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.CardDto;
import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.model.entity.Card;
import org.academy.OnlineStoreDemo.model.entity.User;
import org.academy.OnlineStoreDemo.model.repository.CardRepository;
import org.academy.OnlineStoreDemo.service.CardService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final ModelMapper modelMapper;

    public CardServiceImpl(CardRepository cardRepository, ModelMapper modelMapper) {
        this.cardRepository = cardRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void save(CardDto cardDto, UserDto userDto) {
        cardDto.setUserDto(userDto);
        cardDto.setTotalAmount(1500.0+Math.random()*1500.0);
        Card card =modelMapper.map(cardDto,Card.class);
        cardRepository.save(card);
        log.info("in save card: card {} successfully saved for user{}", card, card.getUser());
    }

    @Override
    public CardDto findById(Integer cardId){
        Card card= null;
        try {
            card = cardRepository.findById(cardId).orElseThrow(Exception::new);
        } catch (Exception e) {
            log.error("in find by id: card by id{} not found",cardId);
            return null;
        }
        CardDto cardDto=modelMapper.map(card, CardDto.class);
        log.info("in find by id: card{} founded by id{}", card,cardId);
        return cardDto;

    }

    @Override
    public void remove(Integer cardId) {
        cardRepository.deleteById(cardId);
        log.info("in remove card: card with id {} successfully removed",cardId);
    }

    @Override
    public void update(CardDto cardDto) {
        Card cardFromDb=cardRepository.getById(cardDto.getId());
        cardFromDb.setName(cardDto.getName());
        cardFromDb.setCvv(cardDto.getCvv());
        cardFromDb.setNumber(cardDto.getNumber());
        cardRepository.save(cardFromDb);
        log.info("in update card: card updated to card{}", cardFromDb);
    }

    @Override
    public List<CardDto> findAllByUser(UserDto userDto) {
        User user= modelMapper.map(userDto, User.class);
        List<Card> cards=cardRepository.findAllByUser(user);
        List<CardDto> cardsDto =new ArrayList<>();
        for (Card card : cards) {
            CardDto map = modelMapper.map(card, CardDto.class);
            cardsDto.add(map);
        }
        log.info("in find all cards by user: founded {} cards by user {}", cards.size(), user);
       return cardsDto;
    }
}
