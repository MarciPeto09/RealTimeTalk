package com.Marcelina.RealTimeTalk.mapper;


import com.Marcelina.RealTimeTalk.dto.RequestMessagesDto;
import com.Marcelina.RealTimeTalk.dto.RespondMessagesDto;
import com.Marcelina.RealTimeTalk.model.Conversation;
import com.Marcelina.RealTimeTalk.model.Messages;
import com.Marcelina.RealTimeTalk.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MessagesMapper {

    private final UserMapper userMapper;

    @Autowired
    public MessagesMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public RespondMessagesDto mapToResponse(Messages message) {
        if (message == null) {
            return null;
        }
        return new RespondMessagesDto(
                message.getId(),
                message.getConversation() != null ? message.getConversation().getId() : null,
                userMapper.mapToResponse(message.getSender()),
                message.getContent(),
                message.getFileName(),
                message.getFileUrl(),
                message.getTimestamp()
        );
    }

    public Messages mapToEntity(RequestMessagesDto dto, Users sender, Conversation conversation) {
        if (dto == null || sender == null) {
            return null;
        }
        Messages message = new Messages();
        message.setSender(sender);
        message.setContent(dto.getContent());
        message.setFileName(dto.getFileName());
        message.setFileUrl(dto.getFileUrl());
        message.setTimestamp(LocalDateTime.now());
        message.setConversation(conversation);
        return message;
    }
}