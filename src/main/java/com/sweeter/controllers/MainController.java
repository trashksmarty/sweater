package com.sweeter.controllers;

import com.sweeter.domain.Message;
import com.sweeter.domain.User;
import com.sweeter.repos.MessageRepo;
import com.sweeter.service.MssageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.validation.BindingResult;


@Controller
public class MainController {

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private MssageService mssageService;

    @GetMapping("/")
    public String greeting(Model model) {
        return "home";
    }

    @GetMapping("/main")
    public String main(
            @RequestParam(required = false, defaultValue = "") String filter,
            @AuthenticationPrincipal User user,
            Model model
    ) {
        model.addAttribute("userId", user.getId());
        Iterable<Message> messages = messageRepo.findAll();

        if (filter != null && !filter.isEmpty()) {
            messages = messageRepo.findByTag(filter);
        } else {
            messages = messageRepo.findAll();
        }

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @Valid Message message,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        model.addAttribute("userId", user.getId());
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = UtilsController.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);
        } else {
            model.addAttribute("message", null);
            mssageService.addNewMessage(message, user, file);
        }
        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages", messages);
        return "main";
    }

    @GetMapping("/login")
    public String login(@AuthenticationPrincipal User user) {
        if (user == null) {
            return "login";
        } else {
            return "redirect:/";
        }
    }
}
