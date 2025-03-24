package com.Marcelina.RealTimeTalk.service;

import com.Marcelina.RealTimeTalk.dto.RequestMessagesDto;
import com.Marcelina.RealTimeTalk.dto.RespondMessagesDto;
import com.Marcelina.RealTimeTalk.mapper.MessagesMapper;
import com.Marcelina.RealTimeTalk.model.Conversation;
import com.Marcelina.RealTimeTalk.model.Messages;
import com.Marcelina.RealTimeTalk.model.Users;
import com.Marcelina.RealTimeTalk.repository.ConversationRepository;
import com.Marcelina.RealTimeTalk.repository.MessagesRepository;
import com.Marcelina.RealTimeTalk.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;


@Service
@AllArgsConstructor
@Transactional
public class MessagesService {

    private final MessagesMapper messagesMappers;
    private final MessagesRepository messagesRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    public  final ConversationRepository conversationRepository;

    public List<RespondMessagesDto> getAllMessages() {
        List<Messages> messagesList = messagesRepository.findAll();
        return messagesList.stream()
                .map(messagesMappers::mapToResponse)
                .toList();
    }


    public RespondMessagesDto save(RequestMessagesDto requestMessagesDto) {
        if (requestMessagesDto.getSenderId() == null) {
            throw new IllegalArgumentException("SenderId must not be null");
        }

        if (requestMessagesDto.getConversationId() == null) {
            throw new IllegalArgumentException("ConversationId must not be null");
        }

        Conversation conversation = conversationRepository.findById(requestMessagesDto.getConversationId())
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        Users sender = userRepository.findById(requestMessagesDto.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        Messages message = messagesMappers.mapToEntity(requestMessagesDto, sender,conversation);
        message.setTimestamp(LocalDateTime.now());
        Messages savedMessage = messagesRepository.save(message );
        return messagesMappers.mapToResponse(savedMessage);
    }


    public  void deleteMessage(Long id){
        messagesRepository.deleteById(id);
    }

    public RespondMessagesDto saveMessageWithFile(RequestMessagesDto requestMessagesDto, MultipartFile file) {
        Users sender = userRepository.findById(requestMessagesDto.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Messages message = new Messages();
        message.setSender(sender);
        message.setContent(requestMessagesDto.getContent());
        message.setTimestamp(LocalDateTime.now());

        if (file != null && !file.isEmpty()) {
            String fileName = fileStorageService.storeFile(file);
            message.setFileName(fileName);
        } else {
            message.setFileName(null);
        }

        messagesRepository.save(message);
        return messagesMappers.mapToResponse(message);
    }

    public List<RespondMessagesDto> getMessagesByConversation(Long conversationId) {
        List<Messages> list = messagesRepository.getMessagesByConversation_Id(conversationId);
        return list.stream()
                .map(messagesMappers::mapToResponse)
                .toList();
    }
}