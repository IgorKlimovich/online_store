package org.academy.OnlineStoreDemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.CardDto;
import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.form.UserForm;
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
        UserDto userDto = userService.findByLogin(authentication.getName());
        System.out.println(userDto.getCardsDto());
        List<CardDto> cardsDto = userDto.getCardsDto();
        UserForm userForm=modelMapper.map(userDto,UserForm.class);
        model.addAttribute("userForm",userForm);
        model.addAttribute("cardsDto", cardsDto);
        model.addAttribute("cardDto", new CardDto());
        return "profile";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("userForm") @Valid UserForm userForm, BindingResult bindingResult,
                             Model model, Authentication authentication, HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse){

       List<UserDto> usersDto =userService.findAll();
        UserDto userDto = null;
        try {
            userDto = usersDto.stream()
                    .filter(item->item.getId().equals(userForm.getId()))
                    .findFirst().orElseThrow(Exception::new);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<CardDto> cardsDto = userDto.getCardsDto();
        usersDto.remove(userDto);


        if (bindingResult.hasErrors()){
            model.addAttribute("cardsDto", cardsDto);
            model.addAttribute("userForm",userForm);
            model.addAttribute("error",7);
            model.addAttribute("cardDto",new CardDto());
            return "profile";
        }


        if (usersDto.stream().anyMatch(item->item.getEmail().equals(userForm.getEmail()))){
            model.addAttribute("existEmail", "такой email уже существует");
            model.addAttribute("userForm",userForm);
            model.addAttribute("error",7);
            model.addAttribute("cardDto",new CardDto());
            model.addAttribute("cardsDto", cardsDto);
            return"profile";
        }

        if (usersDto.stream().anyMatch(item->item.getPhoneNumber().equals(userForm.getPhoneNumber()))){
            model.addAttribute("existPhoneNumber", "такой номер телефона уже существует");
            model.addAttribute("userForm",userForm);
            model.addAttribute("error",7);
            model.addAttribute("cardDto",new CardDto());
            model.addAttribute("cards", cardsDto);
            return"profile";
        }
        if (usersDto.stream().anyMatch(item->item.getLogin().equals(userForm.getLogin()))){
            model.addAttribute("existLogin", "такой логин уже существует");
            model.addAttribute("userForm",userForm);
            model.addAttribute("error",7);
            model.addAttribute("cardDto",new CardDto());
            model.addAttribute("cardsDto", cardsDto);
            return"profile";
        }
        new SecurityContextLogoutHandler().logout(httpServletRequest,httpServletResponse,authentication);
        persistentTokenRepository.removeUserTokens(authentication.getName());
      userService.update(userForm, userDto);
        return"login";
    }

    @PostMapping("/save_card")
    public String saveCard(@ModelAttribute("cardDto") @Valid CardDto cardDto, BindingResult bindingResult,
                           Principal principal, Model model) {
        UserDto userDto = userService.findByLogin(principal.getName());
        List<CardDto> cardsDto = userDto.getCardsDto();
        UserForm userForm=modelMapper.map(userDto,UserForm.class);

        if (bindingResult.hasErrors()) {
            model.addAttribute("userForm",userForm);
            model.addAttribute("cardsDto", cardsDto);
            model.addAttribute("error", 8);
            model.addAttribute("cardDto", cardDto);
            return "profile";
        }

        if (cardsDto.stream().anyMatch(item->item.getNumber().equals(cardDto.getNumber()))){
            model.addAttribute("userForm",userForm);
            model.addAttribute("cardsDto", cardsDto);
            model.addAttribute("error", 8);
            model.addAttribute("cardDto", cardDto);
            model.addAttribute("errorAddCard", "такая какрта у вас уже есть");
            return "profile";
        }


        cardService.save(cardDto, userDto);
        List<CardDto> cardsAfterSaveDto= cardService.findAllByUser(userService.findByLogin(principal.getName()));
        model.addAttribute("error", 9);
        model.addAttribute("userForm",userForm);
        model.addAttribute("cardDto", cardDto);
        model.addAttribute("cardsDto", cardsAfterSaveDto);
        return "profile";
    }

    @PostMapping("/remove_card/{id}")
    public String removeCard(@PathVariable("id")Integer cardId, Model model, Principal principal){
        CardDto cardDto = cardService.findById(cardId);
        cardService.remove(cardDto);
        UserDto userDto = userService.findByLogin(principal.getName());
        List<CardDto> cardsDto = userDto.getCardsDto();
        UserForm userForm=modelMapper.map(userDto,UserForm.class);
        model.addAttribute("userForm",userForm);
        model.addAttribute("cardDto", new CardDto());
        model.addAttribute("error", 9);
        model.addAttribute("cardsDto", cardsDto);
        return "profile";
    }

    @GetMapping("update_form/{id}")
    public String getUpdateCardForm(@PathVariable("id")Integer cardId, Model model, Principal principal){
       CardDto cardDto = cardService.findById(cardId);
        UserDto userDto = userService.findByLogin(principal.getName());
        List<CardDto> cardsDto = userDto.getCardsDto();
        UserForm userForm=modelMapper.map(userDto,UserForm.class);
        model.addAttribute("userForm",userForm);
        model.addAttribute("cardDto", cardDto);
        model.addAttribute("error", 10);
        model.addAttribute("cardsDto", cardsDto);
        return "profile";
    }

    @PostMapping("/update_card")
    public String updateCard(@ModelAttribute("cardDto")@Valid CardDto cardDto, BindingResult bindingResult,
                             Model model, Principal principal){


        UserDto userDto = userService.findByLogin(principal.getName());
        List<CardDto> cardListForUpdateDto=userDto.getCardsDto();
        CardDto cardForUpdateDto=cardService.findById(cardDto.getId());
        cardListForUpdateDto.remove(cardForUpdateDto);
        UserForm userForm=modelMapper.map(userDto,UserForm.class);
        if (bindingResult.hasErrors()){
            List<CardDto> cardsDto= cardService.findAllByUser(userService.findByLogin(principal.getName()));
            model.addAttribute("cardsDto", cardsDto);
            model.addAttribute("userForm",userForm);
            model.addAttribute("cardDto" ,cardDto);
            model.addAttribute("error" , 11);
            return "profile";
        }
        if (cardListForUpdateDto.stream().anyMatch(item->item.getNumber().equals(cardDto.getNumber()))){
            List<CardDto> cardsDto= cardService.findAllByUser(userService.findByLogin(principal.getName()));
            model.addAttribute("cardsDto", cardsDto);
            model.addAttribute("userForm",userForm);
            model.addAttribute("cardDto" ,cardDto);
            model.addAttribute("error" , 11);
            model.addAttribute("errorUpdateCard", "такая карта у вас уже есть");
            return "profile";
        }
        cardService.update(cardDto);
       List<CardDto> cardsAfterUpdateDto= cardService.findAllByUser(userService.findByLogin(principal.getName()));
        model.addAttribute("cardsDto", cardsAfterUpdateDto);
        model.addAttribute("userForm",userForm);
        model.addAttribute("cardDto" ,cardDto);
        model.addAttribute("error" , 9);
        return "profile";
    }
}
