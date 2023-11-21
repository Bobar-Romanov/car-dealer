package com.service.carDealer.controller;

import com.service.carDealer.service.UserService;
import com.service.carDealer.util.RegForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class SingInController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(Model model)
    {
        model.addAttribute("RegForm", new RegForm());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute RegForm form,
                          Model model) {
        if(userService.existByLogin(form.getLogin())){
            model.addAttribute("loginError", "User already exists");
            model.addAttribute("RegForm", form);
            return "registration";
        }
        if(userService.existByPhone(form.getPhone())){
            model.addAttribute("phoneError", "Phone number already exists");
            model.addAttribute("RegForm", form);
            return "registration";
        }

        if(!userService.regUser(form)){
            model.addAttribute("sqlError", "Database error...");
            return "registration";
        }
        log.warn("New user registered: {} ", form.getLogin());
        return "redirect:/login";
    }
    @GetMapping("/login")
    String login() {
        return "login";
    }
}