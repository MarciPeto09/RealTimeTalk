package com.Marcelina.RealTimeTalk.service;

import com.Marcelina.RealTimeTalk.dto.RequestMessagesDto;
import com.Marcelina.RealTimeTalk.dto.RespondMessagesDto;
import com.Marcelina.RealTimeTalk.mapper.MessagesMappers;
import com.Marcelina.RealTimeTalk.model.Messages;
import com.Marcelina.RealTimeTalk.model.Users;
import com.Marcelina.RealTimeTalk.repository.MessagesRepository;
import com.Marcelina.RealTimeTalk.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessagesService {

    private final MessagesMappers messagesMappers;
    private final MessagesRepository messagesRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;


    public List<RespondMessagesDto> getAllMessages(){
        List<Messages> messagesList = messagesRepository.findAll();
        return messagesList.stream()
                .map(messagesMappers::mapToResponse)
                .toList();
    }


    @Transactional
    public void deleteMessage(String username) {
        Users user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        List<Messages> sentMessages = messagesRepository.findBySender(user);
        List<Messages> receivedMessages = messagesRepository.findByReceiver(user);
        sentMessages.addAll(receivedMessages);
        messagesRepository.deleteAll(sentMessages);
    }



    public void save(RequestMessagesDto requestMessagesDto){
        // Validate that senderId and receiverId are not null
        if(requestMessagesDto.getSenderId() == null || requestMessagesDto.getReceiverId() == null) {
            throw new IllegalArgumentException("SenderId and ReceiverId must not be null");
        }

        // Fetch sender and receiver using the provided IDs
        Users sender = userRepository.findById(requestMessagesDto.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        Users receiver = userRepository.findById(requestMessagesDto.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // Map DTO to entity and set additional fields
        Messages messages = messagesMappers.mapToEntity(requestMessagesDto);
        messages.setSender(sender);
        messages.setReceiver(receiver);
        messages.setTimestamp(LocalDateTime.now());
        messages.setFileName(requestMessagesDto.getFileName());
        messages.setFileUrl(requestMessagesDto.getFileUrl());

        messagesRepository.save(messages);
    }


    public List<RespondMessagesDto> getMessagesBetweenUsers(Long senderId, Long receiverId) {
        List<Messages> listOfMessages = messagesRepository.findMessagesBetweenUsers(senderId, receiverId);
        return listOfMessages.stream()
                .map(messagesMappers::mapToResponse)
                .collect(Collectors.toList());
    }


    public RespondMessagesDto saveMessageWithFile(RequestMessagesDto requestMessagesDto, MultipartFile file) {
        Users sender = userRepository.findById(requestMessagesDto.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        Users receiver = userRepository.findById(requestMessagesDto.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Messages message = new Messages();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(requestMessagesDto.getContent());
        message.setTimestamp(LocalDateTime.now());
        message.setFileUrl(requestMessagesDto.getFileUrl());

        if (file != null && !file.isEmpty()) {
            String fileName = fileStorageService.storeFile(file);
            message.setFileName(fileName);
        } else {
            message.setFileName(null);
        }

        messagesRepository.save(message);
        return messagesMappers.mapToResponse(message);
    }


}
