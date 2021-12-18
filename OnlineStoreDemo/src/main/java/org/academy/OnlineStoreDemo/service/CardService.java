package org.academy.OnlineStoreDemo.service;


import org.academy.OnlineStoreDemo.model.entity.Card;
import org.academy.OnlineStoreDemo.model.entity.User;

import java.util.List;

public interface CardService {
    void save(Card card, User user);
    Card findById(Integer cardId);
    void remove(Card card);
    void update(Card card);
    List<Card> findAllByUser(User user);
}
