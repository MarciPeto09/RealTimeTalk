package com.Marcelina.RealTimeTalk.controller;

import com.Marcelina.RealTimeTalk.dto.RequestConversationDto;
import com.Marcelina.RealTimeTalk.dto.RespondConversationDto;
import com.Marcelina.RealTimeTalk.model.Users;
import com.Marcelina.RealTimeTalk.service.ConversationService;
import com.Marcelina.RealTimeTalk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<RespondConversationDto> createConversation(@RequestBody RequestConversationDto requestDto) {
        if (requestDto.getParticipantIds() == null || requestDto.getParticipantIds().isEmpty()) {
            throw new IllegalArgumentException("Participant IDs must not be null or empty");
        }
        List<Users> participants = userService.getAllUsersById(requestDto.getParticipantIds());

        if (participants.isEmpty()) {
            throw new IllegalArgumentException("No valid participants found for the provided IDs");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(conversationService.createConversation(requestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<RespondConversationDto>> getUserConversations(@PathVariable Long id) {
        return ResponseEntity.ok(conversationService.getUserConversations(id));
    }

    @GetMapping("/conversations/{id}")
    public ResponseEntity<RespondConversationDto> getConversationById(@PathVariable Long id) {
        return ResponseEntity.ok(conversationService.getConversationById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConversation(@PathVariable Long id) {
        conversationService.deleteConversation(id);
        return ResponseEntity.noContent().build();
    }
}
