package com.Marcelina.RealTimeTalk.service;

import com.Marcelina.RealTimeTalk.dto.RequestConversationDto;
import com.Marcelina.RealTimeTalk.dto.RespondConversationDto;
import com.Marcelina.RealTimeTalk.mapper.ConversationMapper;
import com.Marcelina.RealTimeTalk.model.Conversation;
import com.Marcelina.RealTimeTalk.model.Users;
import com.Marcelina.RealTimeTalk.repository.ConversationRepository;
import com.Marcelina.RealTimeTalk.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationMapper conversationMapper;

    public RespondConversationDto createConversation(RequestConversationDto requestDto) {
        Conversation savedConversation = conversationRepository.save(conversationMapper.mapToEntity(requestDto));
        return conversationMapper.mapToResponse(savedConversation);
    }


    public RespondConversationDto getConversationById(Long id) {
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conversation not found"));
        return conversationMapper.mapToResponse(conversation);
    }

    public void deleteConversation(Long id) {
        if (!conversationRepository.existsById(id)) {
            throw new EntityNotFoundException("Conversation not found");
        }
        conversationRepository.deleteById(id);
    }

    public List<RespondConversationDto> getUserConversations(Long userId) {
        List<Conversation> list = conversationRepository.findByUserId(userId);
        return list.stream()
                .map(conversationMapper::mapToResponse)
                .collect(Collectors.toList());
    }
}
