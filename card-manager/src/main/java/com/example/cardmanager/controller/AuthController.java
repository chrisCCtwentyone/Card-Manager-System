package com.example.cardmanager.controller;

import jakarta.validation.Valid;
import com.example.cardmanager.dto.UserDto;
import com.example.cardmanager.entity.User;
import com.example.cardmanager.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // handler per la pagina iniziale
    @GetMapping("/index")
    public String home() {
        return "index";
    }

    // handler per la pagina iniziale
    @GetMapping("/")
    public String stillHome() {
        return "index";
    }

    // handler per la pagina di registrazione
    @GetMapping("/register")

    public String showRegistrationForm(Model model) {

        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
            BindingResult result,
            Model model) {
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null,
                    "Esiste un utente con la stessa email");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/register";
        }

        userService.saveUser(userDto);
        return "redirect:/register?success";
    }

    @GetMapping("/users")
    public String users(Model model) {
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/blockseller")
    public String showBlockSellerForm(Model model) {
        return "blockseller";

    }

    @RequestMapping(value = "/blockseller", method = RequestMethod.POST, params = "block")
    public String blockUser(String email, Model model) {
        User user = userService.findUserByEmail(email);
        if (user == null || user.getEmail() == null || user.getEmail().isEmpty()) {
            model.addAttribute("errorNotFound", "Utente non trovato");
            return "blockseller";
        } else {
            if (!user.getEmail().contains("@seller")) {
                model.addAttribute("notASeller", "Utente non trovato");
                return "blockseller";

            } else {

                userService.blockUser(email);
                model.addAttribute("success", "Utente bloccato");
                model.addAttribute("nome_venditore", user.getName());
                model.addAttribute("email", user.getEmail());
                model.addAttribute("statoutente", user.getStatoutente());
                showBlockSellerForm(model);
                return "blockseller";
            }
        }
    }

    @RequestMapping(value = "/blockseller", method = RequestMethod.POST, params = "active")
    public String activeUser(String email, Model model) {
        User user = userService.findUserByEmail(email);
        if (user == null || user.getEmail() == null || user.getEmail().isEmpty()) {
            model.addAttribute("errorNotFound", "Utente non trovato");
            return "blockseller";
        } else {
            if (user.getEmail().contains("@seller")) {
                userService.activeUser(email);
                model.addAttribute("nome_venditore", user.getName());
                model.addAttribute("email", user.getEmail());
                model.addAttribute("statoutente", user.getStatoutente());
                model.addAttribute("success", "Utente attivato");

                showBlockSellerForm(model);
                return "blockseller";

            } else {
                model.addAttribute("notASeller", "Utente non trovato");
                return "blockseller";
            }
        }
    }

}