package com.Marcelina.RealTimeTalk.mapper;

    import com.Marcelina.RealTimeTalk.dto.RequestMessagesDto;
    import com.Marcelina.RealTimeTalk.dto.RespondMessagesDto;
    import com.Marcelina.RealTimeTalk.model.Messages;
    import com.Marcelina.RealTimeTalk.model.Users;
    import com.Marcelina.RealTimeTalk.repository.MessagesRepository;
    import com.Marcelina.RealTimeTalk.repository.UserRepository;
    import lombok.AllArgsConstructor;
    import org.springframework.stereotype.Component;

    import java.time.LocalDateTime;
@Component
@AllArgsConstructor
public class MessagesMappers {

      private MessagesRepository messagesRepository;
      private UserRepository userRepository;

        public Messages mapToEntity(RequestMessagesDto requestMessagesDto) {
            Messages message = new Messages();
            Users sender = userRepository.findById(requestMessagesDto.getSenderId())
                    .orElseThrow(() -> new RuntimeException("There is no user with this id"));
            message.setSender(sender);

            Users receiver = userRepository.findById(requestMessagesDto.getReceiverId())
                    .orElseThrow(() -> new RuntimeException("There is no user with this id"));

            message.setReceiver(receiver);
            message.setContent(requestMessagesDto.getContent());
            message.setTimestamp(LocalDateTime.now());
            message.setFileName(requestMessagesDto.getFileName());
            message.setFileUrl(requestMessagesDto.getFileUrl());
            return message;
        }

        public RespondMessagesDto mapToResponse(Messages messages){

            RespondMessagesDto respondMessagesDto = new RespondMessagesDto();
            respondMessagesDto.setId(messages.getId());
            respondMessagesDto.setContent(messages.getContent());
            respondMessagesDto.setTimestamp(messages.getTimestamp());
            respondMessagesDto.setSenderId(messages.getSender().getId());
            respondMessagesDto.setReceiverId(messages.getReceiver().getId());
            respondMessagesDto.setFileName(messages.getFileName());
            respondMessagesDto.setFileUrl(messages.getFileUrl());
            return respondMessagesDto;
        }
    }
