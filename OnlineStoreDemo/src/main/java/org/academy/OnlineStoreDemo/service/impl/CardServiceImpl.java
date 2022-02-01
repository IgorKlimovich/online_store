package org.academy.OnlineStoreDemo.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.CardDto;
import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.exception.CardNotFoundException;
import org.academy.OnlineStoreDemo.model.entity.Card;
import org.academy.OnlineStoreDemo.model.entity.User;
import org.academy.OnlineStoreDemo.model.repository.CardRepository;
import org.academy.OnlineStoreDemo.service.CardService;
import org.academy.OnlineStoreDemo.util.UtilListMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class CardServiceImpl implements CardService {

    private final ModelMapper modelMapper;
    private final CardRepository cardRepository;
    private final UtilListMapper utilListMapper;

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
        Card card = cardRepository.findById(cardId).orElseThrow(()->new CardNotFoundException("card not found"));
        log.info("in find by id: card{} founded by id{}", card,cardId);
        return modelMapper.map(card, CardDto.class);
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
        log.info("in find all cards by user: founded {} cards by user {}", cards.size(), user);
       return utilListMapper.mapList(cards,CardDto.class) ;
    }
}
