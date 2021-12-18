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
        Card card =modelMapper.map(cardDto,Card.class);
        User user =modelMapper.map(userDto, User.class);
        card.setUser(user);
        card.setTotalAmount(1500.0);
        cardRepository.save(card);
        log.info("in save card: card {} successfully saved for user{}", card, user);
    }

    @Override
    public CardDto findById(Integer cardId){
        Card card= null;
        try {
            card = cardRepository.findById(cardId).orElseThrow(Exception::new);
        } catch (Exception e) {
            log.warn("in find by id: card by id{} not found",cardId);
        }
        CardDto cardDto=modelMapper.map(card, CardDto.class);

        log.info("in find by id: card{} founded by id{}", card,cardId);
        return cardDto;

    }

    @Override
    public void remove(CardDto cardDto) {
        Card card = modelMapper.map(cardDto, Card.class);
        cardRepository.delete(card);
        log.info("in remove card: card{} successfully removed",card);
    }

    @Override
    public void update(CardDto cardDto) {
        Card card = modelMapper.map(cardDto, Card.class);
        Card cardFromDb=cardRepository.getById(card.getId());
        cardFromDb.setName(card.getName());
        cardFromDb.setCvv(card.getCvv());
        cardFromDb.setNumber(card.getNumber());
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
       return cardsDto;
    }
}
