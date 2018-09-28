package com.sweeter.controllers;

import com.sweeter.domain.Message;
import com.sweeter.domain.User;
import com.sweeter.repos.MessageRepo;
import com.sweeter.service.MssageService;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class MessagesController {
    
    @Autowired
    private MessageRepo messageRepo;
    
    @Autowired
    private MssageService mssageService;
    
    @GetMapping("/user-messages/{user}")
    public String userMessaages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            Model model,
            @RequestParam(required = false) Message message
    ){
            Set<Message> messages;
        if(message != null){
            messages = new HashSet<>();
            messages.add(message);
        } else {
            messages = user.getMessages();
        }
        model.addAttribute("userChannel", user);
        model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("subscribersCount", user.getSubscribers().size());
        model.addAttribute("userId", currentUser.getId());
        model.addAttribute("messages", messages);
        model.addAttribute("message", message);
        model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
        model.addAttribute("isCurrentUser", currentUser.equals(user));
        
        return "usermessages";
    }
    
    @PostMapping("/user-messages/{user}")
    public String saveUserMessage(
            @AuthenticationPrincipal User currentUser,
            @RequestParam("id") Message message,
            @RequestParam("text") String text,
            @RequestParam("tag") String tag,
            @RequestParam("file") MultipartFile file
    ) throws IOException{
        if(message.getAuthor().equals(currentUser)){
            if(!StringUtils.isEmpty(text)){
                message.setText(text);
            }
            
            if(!StringUtils.isEmpty(tag)){
                message.setTag(tag);
            }
            mssageService.saveFile(file, message);
            
            messageRepo.save(message);
        }
        
        return "redirect:/user-messages/"+currentUser.getId();
    }
    
}
