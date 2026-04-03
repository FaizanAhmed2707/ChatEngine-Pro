package com.chat.app_realtime_chat.controller;

import com.chat.app_realtime_chat.model.ChatMessage;
import com.chat.app_realtime_chat.model.MessageType;
import com.chat.app_realtime_chat.repository.ChatMessageRepository;
import com.chat.app_realtime_chat.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ChatMessageRepository messageRepository;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("sender") String sender) {
        try {
            String fileUrl = cloudinaryService.uploadFile(file);

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setSender(sender);
            chatMessage.setContent(fileUrl); // We store the Cloudinary URL in the content field
            chatMessage.setType(MessageType.FILE);

            messageRepository.save(chatMessage);
            messagingTemplate.convertAndSend("/topic/messages", chatMessage);

            return ResponseEntity.ok(chatMessage);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Upload failed");
        }
    }
}