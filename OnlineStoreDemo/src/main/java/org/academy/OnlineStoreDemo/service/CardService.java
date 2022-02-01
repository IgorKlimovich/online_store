package org.academy.OnlineStoreDemo.service;


import org.academy.OnlineStoreDemo.dto.CardDto;
import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.exception.CardNotFoundException;

import java.util.List;

public interface CardService {

    /**
     * Save the card in the database
     * @param cardDto card for save
     * @param userDto user to whom the card will be saved
     */
    void save(CardDto cardDto, UserDto userDto);

    /**
     * Returns the card from database by id
     * @param cardId id card for search
     * @return card by id
     * @throws CardNotFoundException if there is no card with the id in database
     */
    CardDto findById(Integer cardId);

    /**
     * Removes the card from the database
     * @param cardId id card for remove
     */
    void remove(Integer cardId);

    /**
     * Update the card in the database
     * @param cardDto card for update
     */
    void update(CardDto cardDto);

    /**
     * Returns a list all user cards
     * @param userDto searching a cards by this user
     * @return list of cards
     */
    List<CardDto> findAllByUser(UserDto userDto);
}
