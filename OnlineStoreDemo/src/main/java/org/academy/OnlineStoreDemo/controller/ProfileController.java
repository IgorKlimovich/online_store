package org.academy.OnlineStoreDemo.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.CardDto;
import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.form.UserForm;

import org.academy.OnlineStoreDemo.service.CardService;
import org.academy.OnlineStoreDemo.service.UserService;
import org.academy.OnlineStoreDemo.valid.CardValidatorService;
import org.academy.OnlineStoreDemo.valid.UserValidatorService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static org.academy.OnlineStoreDemo.constants.Constants.*;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final CardService cardService;
    private final UserValidatorService userValidatorService;
    private final CardValidatorService cardValidatorService;
    private final PersistentTokenRepository persistentTokenRepository;

    @GetMapping
    public String getProfilePage(Principal principal, Model model) {
        UserDto userDto = userService.findByLogin(principal);
        UserForm userForm = modelMapper.map(userDto, UserForm.class);
        model.addAttribute(USER_FORM, userForm)
                .addAttribute(CARD_DTO, new CardDto())
                .addAttribute(CARDS_DTO, userDto.getCardsDto())
                .addAttribute(USER_PROF, userDto);
        log.info("in get profile page: return profile page for user {}", userDto);
        return PROFILE;
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("userForm") @Valid UserForm userForm, BindingResult bindingResult,
                             Model model, Authentication authentication, HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse, Principal principal) {
        List<UserDto> usersDto = userService.findAll();
        UserDto userDto = usersDto.stream().filter(item -> item.getId().equals(userForm.getId()))
                .findFirst().orElse(new UserDto());
        usersDto.remove(userDto);
        String message = userValidatorService.validateUser(usersDto, userForm);
        if (!message.isEmpty()) {
            ObjectError objectError = new ObjectError(GLOBAL_ERROR, message);
            bindingResult.addError(objectError);
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute(USER_FORM, userForm)
                    .addAttribute(ERROR, ERROR_UPDATE_PROFILE)
                    .addAttribute(CARD_DTO, new CardDto())
                    .addAttribute(CARDS_DTO, userDto.getCardsDto())
                    .addAttribute(USER_PROF, userService.findByLogin(principal));
            log.warn("in update profile: binding result has {} errors", bindingResult.getErrorCount());
            return PROFILE;
        }
        new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, authentication);
        persistentTokenRepository.removeUserTokens(authentication.getName());
        userService.update(userForm, userDto);
        log.info("in update profile: user {} updated", userForm);
        return LOGIN;
    }

    @PostMapping("/save_card")
    public String saveCard(@ModelAttribute("cardDto") @Valid CardDto cardDto, BindingResult bindingResult,
                           Principal principal, Model model) {
        UserDto userDto = userService.findByLogin(principal);
        List<CardDto> cardsDto = userDto.getCardsDto();
        UserForm userForm = modelMapper.map(userDto, UserForm.class);
        String message = cardValidatorService.validateCard(cardsDto, cardDto);
        if (message != null) {
            ObjectError objectError = new ObjectError(GLOBAL_ERROR, message);
            bindingResult.addError(objectError);
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute(USER_FORM, userForm);
            model.addAttribute(ERROR, ERROR_UPDATE_CARD);
            model.addAttribute(CARD_DTO, cardDto);
            model.addAttribute(USER_PROF, userDto);
            log.warn("in save card: binding result has {} errors", bindingResult.getErrorCount());
            return PROFILE;
        }
        cardService.save(cardDto, userDto);
        List<CardDto> cardsAfterSaveDto = cardService.findAllByUser(userService.findByLogin(principal));
        model.addAttribute(ERROR, UPDATE_CARD)
                .addAttribute(USER_FORM, userForm)
                .addAttribute(CARD_DTO, cardDto)
                .addAttribute(CARDS_DTO, cardsAfterSaveDto)
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in save card: card {} saved for user {}", cardDto, userDto);
        return PROFILE;
    }

    @PostMapping("/remove_card/{id}")
    public String removeCard(@PathVariable("id") Integer cardId, Model model, Principal principal) {
        cardService.remove(cardId);
        UserDto userDto = userService.findByLogin(principal);
        List<CardDto> cardsAfterRemove = cardService.findAllByUser(userDto);
        UserForm userForm = modelMapper.map(userDto, UserForm.class);
        model.addAttribute(USER_FORM, userForm)
                .addAttribute(CARD_DTO, new CardDto())
                .addAttribute(ERROR, UPDATE_CARD)
                .addAttribute(CARDS_DTO, cardsAfterRemove)
                .addAttribute(USER_PROF, userDto);
        log.info("in remove card: card with id {} removed for user {}", cardId, userDto);
        return PROFILE;
    }

    @GetMapping("update_form/{id}")
    public String getUpdateCardForm(@PathVariable("id") Integer cardId, Model model, Principal principal) {
        CardDto cardDto = cardService.findById(cardId);
        UserDto userDto = userService.findByLogin(principal);
        UserForm userForm = modelMapper.map(userDto, UserForm.class);
        model.addAttribute(USER_FORM, userForm)
                .addAttribute(CARD_DTO, cardDto)
                .addAttribute(CARDS_DTO, userDto.getCardsDto())
                .addAttribute(ERROR, UPDATE_CARD_FORM)
                .addAttribute(USER_PROF, userDto);
        log.info("in get update card form: return user {} to update card form", userDto);
        return PROFILE;
    }

    @PostMapping("/update_card")
    public String updateCard(@ModelAttribute("cardDto") @Valid CardDto cardDto, BindingResult bindingResult,
                             Model model, Principal principal) {
        UserDto userDto = userService.findByLogin(principal);
        List<CardDto> cardListForUpdateDto = userDto.getCardsDto();
        CardDto cardForUpdateDto = cardService.findById(cardDto.getId());
        cardListForUpdateDto.remove(cardForUpdateDto);
        UserForm userForm = modelMapper.map(userDto, UserForm.class);
        String message = cardValidatorService.validateCard(cardListForUpdateDto, cardDto);
        if (message != null) {
            ObjectError objectError = new ObjectError(GLOBAL_ERROR, message);
            bindingResult.addError(objectError);
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute(USER_FORM, userForm)
                    .addAttribute(CARD_DTO, cardDto)
                    .addAttribute(CARDS_DTO, userDto.getCardsDto())
                    .addAttribute(ERROR, ERROR_CARD)
                    .addAttribute(USER_PROF, userDto);
            log.warn("in update card: binding result has {} errors", bindingResult.getErrorCount());
            return PROFILE;
        }
        cardService.update(cardDto);
        List<CardDto> cardsAfterUpdateDto = cardService.findAllByUser(userService.findByLogin(principal));
        model.addAttribute(CARDS_DTO, cardsAfterUpdateDto)
                .addAttribute(USER_FORM, userForm)
                .addAttribute(CARD_DTO, cardDto)
                .addAttribute(ERROR, UPDATE_CARD)
                .addAttribute(USER_PROF, userDto);
        log.info("in update card: card {} updated to card {} for user {}", cardForUpdateDto, cardDto, userDto);
        return PROFILE;
    }

    @PostMapping("/add_photo")
    public String addPhoto(@RequestParam("file") MultipartFile multipartFile, Model model, Principal principal) {
        UserDto userDto = userService.findByLogin(principal);
        UserForm userForm = modelMapper.map(userDto, UserForm.class);
        if (multipartFile.isEmpty()) {
            model.addAttribute(CARD_DTO, new CardDto())
                    .addAttribute(USER_FORM, userForm)
                    .addAttribute(CARDS_DTO, userDto.getCardsDto())
                    .addAttribute(USER_PROF, userDto);
            return PROFILE;
        }
        UserDto userDtoAfterSave = userService.savePhoto(multipartFile, userDto);
        UserForm userAfterSavePhoto = modelMapper.map(userDtoAfterSave, UserForm.class);
        model.addAttribute(USER_FORM, userAfterSavePhoto)
                .addAttribute(CARD_DTO, new CardDto())
                .addAttribute(CARDS_DTO, userDto.getCardsDto())
                .addAttribute(USER_PROF, userDtoAfterSave);
        log.info("in add photo: photo saved for user {}", userDto);
        return PROFILE;
    }

    @PostMapping("delete_photo")
    public String deletePhoto(@RequestParam("id") Integer id, Model model) {
        UserDto userDto = userService.deletePhoto(id);
        UserForm userForm = modelMapper.map(userDto, UserForm.class);
        model.addAttribute(USER_FORM, userForm)
                .addAttribute(CARD_DTO, new CardDto())
                .addAttribute(CARDS_DTO, userDto.getCardsDto())
                .addAttribute(USER_PROF, userDto);
        log.info("in delete photo: set null photo name for user {}", userDto);
        return PROFILE;
    }
}