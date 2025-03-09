package com.Marcelina.RealTimeTalk.controller;

import com.Marcelina.RealTimeTalk.dto.RequestMessagesDto;
import com.Marcelina.RealTimeTalk.dto.RespondMessagesDto;
import com.Marcelina.RealTimeTalk.dto.RespondUserDto;
import com.Marcelina.RealTimeTalk.mapper.MessagesMappers;
import com.Marcelina.RealTimeTalk.model.Messages;
import com.Marcelina.RealTimeTalk.service.FileStorageService;
import com.Marcelina.RealTimeTalk.service.MessagesService;
import com.Marcelina.RealTimeTalk.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:4200")
public class MessageController {

    private final MessagesService messagesService;
    private  final UserService userService;
    private final MessagesMappers messagesMappers;
    private final FileStorageService fileStorageService;



    @GetMapping
    public ResponseEntity<List<RespondMessagesDto>> getAllMessages(){
        return ResponseEntity.ok(messagesService.getAllMessages());
    }


    @DeleteMapping("/user/{username}")
    public ResponseEntity<String> deleteAllMessagesForUser(@PathVariable String username) {
        messagesService.deleteMessage(username);
        return ResponseEntity.ok("All messages for user with Username " + username + " have been deleted.");
    }

    @PostMapping("/save")
    public ResponseEntity<RespondMessagesDto> save(@RequestBody RequestMessagesDto requestMessagesDto) {

        if (requestMessagesDto.getReceiverId() == null || requestMessagesDto.getContent() == null) {
            throw new IllegalArgumentException("ReceiverId and Content must not be null");
        }

        RespondUserDto sender = userService.getUserById(requestMessagesDto.getSenderId());
        RespondUserDto receiver = userService.getUserById(requestMessagesDto.getReceiverId());

        messagesService.save(requestMessagesDto);

        Messages savedMessage =messagesMappers.mapToEntity(requestMessagesDto);

        return ResponseEntity.ok(messagesMappers.mapToResponse(savedMessage));
    }


    @GetMapping("/conversation/{senderId}/{receiverId}")
    public ResponseEntity<List<RespondMessagesDto>> getMessagesBetweenUsers(@PathVariable Long senderId, @PathVariable Long receiverId) {
        List<RespondMessagesDto> messages = messagesService.getMessagesBetweenUsers(senderId, receiverId);
        return ResponseEntity.ok(messages);
    }


    @PostMapping(value = "/saveWithFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RespondMessagesDto> saveMessage(
            @RequestParam("senderId") Long senderId,
            @RequestParam("receiverId") Long receiverId,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        String fileName = fileStorageService.saveFile(file);

        RequestMessagesDto requestMessagesDto = new RequestMessagesDto();
        requestMessagesDto.setSenderId(senderId);
        requestMessagesDto.setReceiverId(receiverId);
        requestMessagesDto.setContent(content);
        requestMessagesDto.setFileName(fileName);
        requestMessagesDto.setFileUrl("/uploads/" + fileName);

        RespondMessagesDto savedMessage = messagesService.saveMessageWithFile(requestMessagesDto, file);

        return ResponseEntity.ok(savedMessage);
    }


}
