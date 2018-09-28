package com.sweeter.controllers;

import com.sweeter.domain.Role;
import com.sweeter.domain.User;
import com.sweeter.repos.UserRepo;
import com.sweeter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;
    
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userList(Model model){
        model.addAttribute("users", userService.findAll());
        return "userList";
    }
    
    @GetMapping("{user}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userEditForm(
            @PathVariable User user,
            Model model
    ){
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "useredit";
    }
    
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam(name = "userId") User user
    ){
        userService.saveUser(user, username, form);
        return "redirect:/user";
    }
    
    @GetMapping("profile")
    public String getProfile(
            @AuthenticationPrincipal User currentUser,
            Model model
    ){
        User user = userRepo.findByUsername(currentUser.getUsername());
        
        model.addAttribute("userChannel", currentUser);
        model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("subscribersCount", user.getSubscribers().size());
        return "profile";
    }
    
    @PostMapping("profile")
    public String updateProfile (
            Model model,
            @AuthenticationPrincipal User user,
            @RequestParam String password,
            @RequestParam String confirmPass
    ){
        if(StringUtils.isEmpty(password) || StringUtils.isEmpty(confirmPass)){
            //<editor-fold defaultstate="collapsed" desc="Password can not be empty!">
            if(StringUtils.isEmpty(password)){model.addAttribute("passwordError", "Password can not be empty!");}
            if(StringUtils.isEmpty(confirmPass)){model.addAttribute("password2Error", "Password can not be empty!");}
            //</editor-fold>
            return "profile";
        }
        
        if(password != null && !password.equals(confirmPass)){
            model.addAttribute("passwordError", "Passwords are different");
            model.addAttribute("password2Error", "Passwords are different");
            return "profile";
        }
        
        userService.updateUser(user, password, confirmPass);
        model.addAttribute("alertMessage", "Password change successful!");
        return "profile";
    }
    
    @GetMapping("subscribe/{user}")
    public String subscribe(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user
    ){
        userService.subscribe(currentUser, user);
        return "redirect:/user-messages/"+user.getId();
    }
    
    @GetMapping("unsubscribe/{user}")
    public String unsubscribe(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user
    ){
        userService.unsubscribe(currentUser, user);
        return "redirect:/user-messages/"+user.getId();
    }
    
    @GetMapping("{type}/{user}/list")
    public String userList(
            Model model,
            @PathVariable User user,
            @PathVariable String type,
            @AuthenticationPrincipal User CurrentUser
    ){
        model.addAttribute("userChannel", user);
        model.addAttribute("type", type);
        
        if("subscriptions".equals(type)){
            model.addAttribute("users", user.getSubscriptions());
        } else {
            model.addAttribute("users", user.getSubscribers());
        }
        
        return "subscriptions";
    }
}
