package com.sweeter.controllers;

import com.sweeter.domain.User;
import com.sweeter.service.UserService;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")//Password confirmation cannot be empty
    public String addUser(
            @RequestParam String confirmPass,
            @Valid User user,
            BindingResult bindingResult,
            Model model
    ) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = UtilsController.getErrors(bindingResult);
            model.mergeAttributes(errors);
            if (StringUtils.isEmpty(confirmPass)){model.addAttribute("password2Error", "Password can not be empty!");}
            return "registration";
        }
        
        if(StringUtils.isEmpty(confirmPass)) {
            model.addAttribute("password2Error", "Password can not be empty!");
            return "registration";
        }
        
        if (user.getPassword() != null && !user.getPassword().equals(confirmPass)) {
            model.addAttribute("passwordError", "Passwords are different");
            model.addAttribute("password2Error", "Passwords are different");
            return "registration";
        }

        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }
        return "redirect:/login";
    }
}
