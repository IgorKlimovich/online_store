package org.academy.OnlineStoreDemo.controller;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.CardDto;
import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.form.UserForm;
import org.academy.OnlineStoreDemo.model.entity.Card;
import org.academy.OnlineStoreDemo.service.CardService;
import org.academy.OnlineStoreDemo.service.UserService;
import org.academy.OnlineStoreDemo.service.UtilService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/profile")
public class ProfileController {

    private static final String ERROR = "error";
    private static final String PROFILE = "profile";
    private static final String CARDS_DTO = "cardsDto";
    private static final String CARD_DTO = "cardDto";
    private static final String USER_PROF = "userProf";

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final PersistentTokenRepository persistentTokenRepository;
    private final CardService cardService;
    private final UtilService utilService;

    public ProfileController(ModelMapper modelMapper, UserService userService, PersistentTokenRepository persistentTokenRepository, CardService cardService, UtilService utilService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.persistentTokenRepository = persistentTokenRepository;
        this.cardService = cardService;
        this.utilService = utilService;
    }

    @GetMapping
    public String getProfilePage(Authentication authentication, Model model, Principal principal) {
        if (authentication == null) {
            log.warn("in get profile page: authentication = null redirect to login");
            return "/login";
        }
        UserDto userDto = userService.findByLogin(authentication.getName());
        List<CardDto> cardsDto = userDto.getCardsDto();
        UserForm userForm = modelMapper.map(userDto, UserForm.class);
        model.addAttribute("userForm", userForm);
        model.addAttribute(CARDS_DTO, cardsDto);
        model.addAttribute(CARD_DTO, new CardDto());
        if (principal != null) {
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
        }
        log.info("in get profile page: return profile page for user {}", userDto);
        return PROFILE;
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("userForm") @Valid UserForm userForm, BindingResult bindingResult,
                             Model model, Authentication authentication, HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse, Principal principal) {

        List<UserDto> usersDto = userService.findAll();
        UserDto userDto = usersDto.stream()
                .filter(item -> item.getId().equals(userForm.getId()))
                .findFirst().orElse(new UserDto());
        List<CardDto> cardsDto = userDto.getCardsDto();
        usersDto.remove(userDto);
        if (bindingResult.hasErrors()) {
            model.addAttribute(CARDS_DTO, cardsDto);
            model.addAttribute("userForm", userForm);
            model.addAttribute(ERROR, 7);
            model.addAttribute(CARD_DTO, new CardDto());
            if (principal != null) {
                model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
            }
            log.warn("in update profile: binding result has {} errors", bindingResult.getErrorCount());
            return PROFILE;
        }

        if (usersDto.stream().anyMatch(item -> item.getEmail().equals(userForm.getEmail()))) {
            model.addAttribute("existEmail", true);
            model.addAttribute("userForm", userForm);
            model.addAttribute(ERROR, 7);
            model.addAttribute(CARD_DTO, new CardDto());
            model.addAttribute(CARDS_DTO, cardsDto);
            if (principal != null) {
                model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
            }
            log.warn("in update profile: email {} already exist", userForm.getEmail());
            return PROFILE;
        }

        if (usersDto.stream().anyMatch(item -> item.getPhoneNumber().equals(userForm.getPhoneNumber()))) {
            model.addAttribute("existPhoneNumber", true);
            model.addAttribute("userForm", userForm);
            model.addAttribute(ERROR, 7);
            model.addAttribute(CARD_DTO, new CardDto());
            model.addAttribute(CARDS_DTO, cardsDto);
            if (principal != null) {
                model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
            }
            log.warn("in update profile: phone number {} already exist", userForm.getPhoneNumber());
            return PROFILE;
        }
        if (usersDto.stream().anyMatch(item -> item.getLogin().equals(userForm.getLogin()))) {
            model.addAttribute("existLogin", true);
            model.addAttribute("userForm", userForm);
            model.addAttribute(ERROR, 7);
            model.addAttribute(CARD_DTO, new CardDto());
            model.addAttribute(CARDS_DTO, cardsDto);
            if (principal != null) {
                model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
            }
            log.warn("in update profile: login {} already exist", userForm.getLogin());
            return PROFILE;
        }
        new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, authentication);
        persistentTokenRepository.removeUserTokens(authentication.getName());
        userService.update(userForm, userDto);
        log.info("in update profile: user {} updated", userForm);
        return "login";
    }

    @PostMapping("/save_card")
    public String saveCard(@ModelAttribute("cardDto") @Valid CardDto cardDto, BindingResult bindingResult,
                           Principal principal, Model model, Authentication authentication) {

        UserDto userDto = userService.findByLogin(principal.getName());
        List<CardDto> cardsDto = userDto.getCardsDto();
        UserForm userForm = modelMapper.map(userDto, UserForm.class);

        if (bindingResult.hasErrors()) {
            model.addAttribute("userForm", userForm);
            model.addAttribute(CARDS_DTO, cardsDto);
            model.addAttribute(ERROR, 8);
            model.addAttribute(CARD_DTO, cardDto);
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
            log.warn("in save card: binding result has {} errors", bindingResult.getErrorCount());
        } else if (cardsDto.stream().anyMatch(item -> item.getNumber().equals(cardDto.getNumber()))) {
            model.addAttribute("userForm", userForm);
            model.addAttribute(CARDS_DTO, cardsDto);
            model.addAttribute(ERROR, 8);
            model.addAttribute(CARD_DTO, cardDto);
            model.addAttribute("existCard", "такая какрта у вас уже есть");
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
            log.warn("in save card: card {} already exist in user {}", cardDto.getNumber(), userDto);
        } else {
            cardService.save(cardDto, userDto);
            List<CardDto> cardsAfterSaveDto = cardService.findAllByUser(userService.findByLogin(principal.getName()));
            model.addAttribute(ERROR, 9);
            model.addAttribute("userForm", userForm);
            model.addAttribute(CARD_DTO, cardDto);
            model.addAttribute(CARDS_DTO, cardsAfterSaveDto);
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
            log.info("in save card: card {} saved for user {}", cardDto, userDto);
        }
        return PROFILE;
    }

    @PostMapping("/remove_card/{id}")
    public String removeCard(@PathVariable("id") Integer cardId, Model model, Principal principal) {
        cardService.remove(cardId);
        UserDto userDto = userService.findByLogin(principal.getName());
        List<CardDto> cardsAfterRemove=cardService.findAllByUser(userService.findByLogin(principal.getName()));
        UserForm userForm = modelMapper.map(userDto, UserForm.class);
        model.addAttribute("userForm", userForm);
        model.addAttribute(CARD_DTO, new CardDto());
        model.addAttribute(ERROR, 9);
        model.addAttribute(CARDS_DTO, cardsAfterRemove);
        model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
        log.info("in remove card: card with id {} removed for user {}", cardId, userDto);
        return PROFILE;
    }

    @GetMapping("update_form/{id}")
    public String getUpdateCardForm(@PathVariable("id") Integer cardId, Model model, Principal principal) {
        CardDto cardDto = cardService.findById(cardId);
        UserDto userDto = userService.findByLogin(principal.getName());
        List<CardDto> cardsDto = userDto.getCardsDto();
        UserForm userForm = modelMapper.map(userDto, UserForm.class);
        model.addAttribute("userForm", userForm);
        model.addAttribute(CARD_DTO, cardDto);
        model.addAttribute(ERROR, 10);
        model.addAttribute(CARDS_DTO, cardsDto);
        model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
        log.info("in get update card form: return user {} to update card form", userDto);
        return PROFILE;
    }

    @PostMapping("/update_card")
    public String updateCard(@ModelAttribute("cardDto") @Valid CardDto cardDto, BindingResult bindingResult,
                             Model model, Principal principal) {
        UserDto userDto = userService.findByLogin(principal.getName());
        List<CardDto> cardListForUpdateDto = userDto.getCardsDto();
        CardDto cardForUpdateDto = cardService.findById(cardDto.getId());
        cardListForUpdateDto.remove(cardForUpdateDto);
        UserForm userForm = modelMapper.map(userDto, UserForm.class);
        if (bindingResult.hasErrors()) {
            List<CardDto> cardsDto = cardService.findAllByUser(userService.findByLogin(principal.getName()));
            model.addAttribute(CARDS_DTO, cardsDto);
            model.addAttribute("userForm", userForm);
            model.addAttribute(CARD_DTO, cardDto);
            model.addAttribute(ERROR, 11);
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
            log.warn("in update card: binding result has {} errors", bindingResult.getErrorCount());
        } else if (cardListForUpdateDto.stream().anyMatch(item -> item.getNumber().equals(cardDto.getNumber()))) {
            List<CardDto> cardsDto = cardService.findAllByUser(userService.findByLogin(principal.getName()));
            model.addAttribute(CARDS_DTO, cardsDto);
            model.addAttribute("userForm", userForm);
            model.addAttribute(CARD_DTO, cardDto);
            model.addAttribute(ERROR, 11);
            model.addAttribute("existCard", "такая карта у вас уже есть");
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
            log.warn("in update card: card {} already exist at user {}", cardDto, userDto);
        } else {
            cardService.update(cardDto);
            List<CardDto> cardsAfterUpdateDto = cardService.findAllByUser(userService.findByLogin(principal.getName()));
            model.addAttribute(CARDS_DTO, cardsAfterUpdateDto);
            model.addAttribute("userForm", userForm);
            model.addAttribute(CARD_DTO, cardDto);
            model.addAttribute(ERROR, 9);
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
            log.info("in update card: card {} updated to card {} for user {}", cardForUpdateDto, cardDto, userDto);
        }
        return PROFILE;
    }

    @PostMapping("/add_photo")
    public String addPhoto(@RequestParam("file") MultipartFile multipartFile, Model model, Principal principal) {
        UserDto userDto = userService.findByLogin(principal.getName());
        UserForm userForm = modelMapper.map(userDto, UserForm.class);
        if(multipartFile.isEmpty()){
            model.addAttribute(CARD_DTO, new CardDto());
            model.addAttribute(CARDS_DTO, cardService.findAllByUser(userDto));
            model.addAttribute("userForm", userForm);
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
            return PROFILE;
        }

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String uploadDir = "./user-photos/" + userDto.getId();
        utilService.savePhotoWithPath(uploadDir,fileName,multipartFile);
        userService.savePhoto(fileName, userDto);
        log.info("in add photo: photo name {} saved for user {}", fileName, userDto);
        UserForm userAfterSavePhoto = modelMapper.map(userService.findByLogin(principal.getName()),UserForm.class);
        model.addAttribute(CARDS_DTO, cardService.findAllByUser(userService.findByLogin(principal.getName())));
        model.addAttribute("userForm", userAfterSavePhoto);
        model.addAttribute(CARD_DTO, new CardDto());
        model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
        log.info("in add photo: file saved to hard disc");
        return PROFILE;
    }

    @PostMapping ("delete_photo")
    public String deletePhoto(@RequestParam("id") Integer id, Principal principal, Model model){

        UserDto userDto=userService.findById(id);
        userDto.setNamePhoto(null);
        userService.update(userDto);
        UserForm userForm = modelMapper.map(userDto, UserForm.class);
        model.addAttribute(CARDS_DTO, cardService.findAllByUser(userService.findByLogin(principal.getName())));
        model.addAttribute("userForm", userForm);
        model.addAttribute(CARD_DTO, new CardDto());
        model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
        log.info("in delete photo: set null photo name for user {}",userDto);
        return PROFILE;
    }

}