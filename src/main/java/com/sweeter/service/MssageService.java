package com.sweeter.service;

import com.sweeter.domain.Message;
import com.sweeter.domain.User;
import com.sweeter.repos.MessageRepo;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MssageService {
    @Autowired
    private MessageRepo messageRepo;
    
    @Value("${upload.path}")
    private String uploadPath;
    
    public void addNewMessage(Message message, User user, MultipartFile file) throws IOException{
        message.setAuthor(user);

        saveFile(file, message);
        
        messageRepo.save(message);
    }

    public void saveFile(MultipartFile file, Message message) throws IOException, IllegalStateException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            message.setFilename(resultFilename);
        }
    }
}
