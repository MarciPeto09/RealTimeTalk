package com.Marcelina.RealTimeTalk.mapper;
import com.Marcelina.RealTimeTalk.dto.RequestConversationDto;
import com.Marcelina.RealTimeTalk.dto.RespondConversationDto;
import com.Marcelina.RealTimeTalk.dto.RespondUserDto;
import com.Marcelina.RealTimeTalk.model.Conversation;
import com.Marcelina.RealTimeTalk.model.Users;
import com.Marcelina.RealTimeTalk.repository.UserRepository;
import com.Marcelina.RealTimeTalk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class ConversationMapper {

    private final UserMapper userMapper;
    private final UserService userService;

    public RespondConversationDto mapToResponse(Conversation conversation) {
        if (conversation == null) {
            return null;
        }
        List<RespondUserDto> participants = conversation.getParticipants()
                .stream()
                .map(userMapper::mapToResponse)
                .collect(Collectors.toList());

        return new RespondConversationDto(
                conversation.getId(),
                participants,
                conversation.getIsGroup(),
                conversation.getGroupName(),
                conversation.getCreatedAt()
        );
    }

    public Conversation mapToEntity(RequestConversationDto requestDto) {

        List<Users> participants = userService.getAllUsersById(requestDto.getParticipantIds());

        if (participants.isEmpty()) {
            throw new IllegalArgumentException("At least one participant is required.");
        }

        boolean isGroup = requestDto.getParticipantIds().size() > 2;

        Conversation conversation = new Conversation();
        conversation.setParticipants(participants);
        conversation.setIsGroup(isGroup);
        conversation.setGroupName(requestDto.getGroupName());
        conversation.setCreatedAt(LocalDateTime.now());

        return conversation;
    }

    public RequestConversationDto mapToRequest(RespondConversationDto respondConversationDto) {
        if (respondConversationDto == null) {
            return null;
        }

        List<Long> participantIds = respondConversationDto.getParticipants().stream()
                .map(RespondUserDto::getId)
                .collect(Collectors.toList());

        RequestConversationDto requestConversationDto = new RequestConversationDto();
        requestConversationDto.setParticipantIds(participantIds);
        requestConversationDto.setIsGroup(respondConversationDto.getIsGroup());
        requestConversationDto.setGroupName(respondConversationDto.getGroupName());

        return requestConversationDto;
    }

}

