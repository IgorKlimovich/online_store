package org.academy.OnlineStoreDemo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.model.entity.Card;
import org.academy.OnlineStoreDemo.model.entity.User;
import org.academy.OnlineStoreDemo.model.repository.CardRepository;
import org.academy.OnlineStoreDemo.service.CardService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public void save(Card card, User user) {
        card.setUser(user);
        card.setTotalAmount(1500.0);
        cardRepository.save(card);
        log.info("in save card: card {} successfully saved for user{}", card, user);
    }

    @Override
    public Card findById(Integer cardId){
        Card card= null;
        try {
            card = cardRepository.findById(cardId).orElseThrow(Exception::new);
        } catch (Exception e) {
            log.warn("in find by id: card by id{} not found",cardId);
        }
        log.info("in find by id: card{} founded by id{}", card,cardId);
        return card;

    }

    @Override
    public void remove(Card card) {
        cardRepository.delete(card);
        log.info("in remove card: card{} successfully removed",card);
    }

    @Override
    public void update(Card card) {
        Card cardFromDb=findById(card.getId());
        cardFromDb.setName(card.getName());
        cardFromDb.setCVV(card.getCVV());
        cardFromDb.setNumber(card.getNumber());
        cardRepository.save(cardFromDb);
        log.info("in update card: card updated to card{}", cardFromDb);
    }

    @Override
    public List<Card> findAllByUser(User user) {
       return cardRepository.findAllByUser(user);
    }
}
