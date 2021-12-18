package org.academy.OnlineStoreDemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.form.UserForm;
import org.academy.OnlineStoreDemo.model.entity.*;
import org.academy.OnlineStoreDemo.service.CardService;
import org.academy.OnlineStoreDemo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/profile")
public class ProfileController {



    private final ModelMapper modelMapper;

    private final UserService userService;

    private final PersistentTokenRepository persistentTokenRepository;

    private final CardService cardService;

    public ProfileController(ModelMapper modelMapper, UserService userService, PersistentTokenRepository persistentTokenRepository, CardService cardService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.persistentTokenRepository = persistentTokenRepository;
        this.cardService = cardService;
    }

    @GetMapping
    public String getProfilePage(Authentication authentication, Model model){

        if (authentication==null){
            return "/login";
        }
        User user = userService.findByLogin(authentication.getName());
        List<Card> cards = user.getCards();
        UserForm userForm=modelMapper.map(user,UserForm.class);
        model.addAttribute("userForm",userForm);
        model.addAttribute("cards", cards);
        model.addAttribute("card", new Card());
        return "profile";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("userForm") @Valid UserForm userForm, BindingResult bindingResult,
                             Model model, Authentication authentication, HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse){

        List<User> users =userService.findAll();
        User user = userService.findByLogin(authentication.getName());
        users.remove(user);

        List<Card> cards = user.getCards();
        if (bindingResult.hasErrors()){
            model.addAttribute("cards", cards);
            model.addAttribute("userForm",userForm);
            model.addAttribute("error",7);
            model.addAttribute("card",new Card());
            return "profile";
        }


        if (users.stream().anyMatch(item->item.getEmail().equals(userForm.getEmail()))){
            model.addAttribute("existEmail", "такой email уже существует");
            model.addAttribute("userForm",userForm);
            model.addAttribute("error",7);
            model.addAttribute("card",new Card());
            model.addAttribute("cards", cards);
            return"profile";
        }

        if (users.stream().anyMatch(item->item.getPhoneNumber().equals(userForm.getPhoneNumber()))){
            model.addAttribute("existPhoneNumber", "такой номер телефона уже существует");
            model.addAttribute("userForm",userForm);
            model.addAttribute("error",7);
            model.addAttribute("card",new Card());
            model.addAttribute("cards", cards);
            return"profile";
        }
        if (users.stream().anyMatch(item->item.getLogin().equals(userForm.getLogin()))){
            model.addAttribute("existLogin", "такой логин уже существует");
            model.addAttribute("userForm",userForm);
            model.addAttribute("error",7);
            model.addAttribute("card",new Card());
            model.addAttribute("cards", cards);
            return"profile";
        }
        new SecurityContextLogoutHandler().logout(httpServletRequest,httpServletResponse,authentication);
        persistentTokenRepository.removeUserTokens(authentication.getName());
      userService.update(userForm, user);
        return"login";
    }

    @PostMapping("/save_card")
    public String saveCard(@ModelAttribute("card") @Valid Card card, BindingResult bindingResult,
                           Principal principal, Model model) {
        User user = userService.findByLogin(principal.getName());
        List<Card> cards = user.getCards();
        UserForm userForm=modelMapper.map(user,UserForm.class);

        if (bindingResult.hasErrors()) {
            model.addAttribute("userForm",userForm);
            model.addAttribute("cards", cards);
            model.addAttribute("error", 8);
            model.addAttribute("card", card);
            return "profile";
        }

        if (cards.stream().anyMatch(item->item.getNumber().equals(card.getNumber()))){
            model.addAttribute("userForm",userForm);
            model.addAttribute("cards", cards);
            model.addAttribute("error", 8);
            model.addAttribute("card", card);
            model.addAttribute("errorAddCard", "такая какрта у вас уже есть");
            return "profile";
        }


        cardService.save(card, user);
        List<Card> cardsAfterSave= cardService.findAllByUser(userService.findByLogin(principal.getName()));
        model.addAttribute("error", 9);
        model.addAttribute("userForm",userForm);
        model.addAttribute("card", card);
        model.addAttribute("cards", cardsAfterSave);
        return "profile";
    }

    @PostMapping("/remove_card/{id}")
    public String removeCard(@PathVariable("id")Integer cardId, Model model, Principal principal){
        Card card = cardService.findById(cardId);

        cardService.remove(card);
        User user = userService.findByLogin(principal.getName());
        List<Card> cards = user.getCards();
        UserForm userForm=modelMapper.map(user,UserForm.class);
        model.addAttribute("userForm",userForm);
        model.addAttribute("card", new Card());
        model.addAttribute("error", 9);
        model.addAttribute("cards", cards);
        return "profile";
    }

    @GetMapping("update_form/{id}")
    public String getUpdateCardForm(@PathVariable("id")Integer cardId, Model model, Principal principal){
       Card card = cardService.findById(cardId);
        User user = userService.findByLogin(principal.getName());
        List<Card> cards = user.getCards();
        UserForm userForm=modelMapper.map(user,UserForm.class);
        model.addAttribute("userForm",userForm);
        model.addAttribute("card", card);
        model.addAttribute("error", 10);
        model.addAttribute("cards", cards);
        return "profile";
    }

    @PostMapping("/update_card")
    public String updateCard(@ModelAttribute("card")@Valid Card card, BindingResult bindingResult,
                             Model model, Principal principal){


        User user = userService.findByLogin(principal.getName());
        List<Card> cardListForUpdate=user.getCards();
        Card cardForUpdate=cardService.findById(card.getId());
        cardListForUpdate.remove(cardForUpdate);
        UserForm userForm=modelMapper.map(user,UserForm.class);
        if (bindingResult.hasErrors()){
            List<Card> cards= cardService.findAllByUser(userService.findByLogin(principal.getName()));
            model.addAttribute("cards", cards);
            model.addAttribute("userForm",userForm);
            model.addAttribute("card" ,card);
            model.addAttribute("error" , 11);
            return "profile";
        }
        if (cardListForUpdate.stream().anyMatch(item->item.getNumber().equals(card.getNumber()))){
            List<Card> cards= cardService.findAllByUser(userService.findByLogin(principal.getName()));
            model.addAttribute("cards", cards);
            model.addAttribute("userForm",userForm);
            model.addAttribute("card" ,card);
            model.addAttribute("error" , 11);
            model.addAttribute("errorUpdateCard", "такая карта у вас уже есть");
            return "profile";
        }
        cardService.update(card);
       List<Card> cardsAfterUpdate= cardService.findAllByUser(userService.findByLogin(principal.getName()));
        model.addAttribute("cards", cardsAfterUpdate);
        model.addAttribute("userForm",userForm);
        model.addAttribute("card" ,card);
        model.addAttribute("error" , 9);
        return "profile";
    }
}
