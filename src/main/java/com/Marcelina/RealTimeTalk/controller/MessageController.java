package com.Marcelina.RealTimeTalk.controller;

import com.Marcelina.RealTimeTalk.dto.RequestMessagesDto;
import com.Marcelina.RealTimeTalk.dto.RespondMessagesDto;
import com.Marcelina.RealTimeTalk.service.FileStorageService;
import com.Marcelina.RealTimeTalk.service.MessagesService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final MessagesService messagesService;
    private final FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<List<RespondMessagesDto>> getAllMessages(){
        return ResponseEntity.ok(messagesService.getAllMessages());
    }


    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<RespondMessagesDto>> getMessagesByConversation(@PathVariable Long conversationId) {
        return ResponseEntity.ok(messagesService.getMessagesByConversation(conversationId));
    }

    @PostMapping("/save")
    public ResponseEntity<RespondMessagesDto> save(@RequestBody RequestMessagesDto requestMessagesDto) {
        if (requestMessagesDto.getContent() == null || requestMessagesDto.getConversationId() == null) {
            throw new IllegalArgumentException("Content and conversationId must not be null");
        }
        return ResponseEntity.ok(messagesService.save(requestMessagesDto));
    }

    @PostMapping(value = "/saveWithFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RespondMessagesDto> saveMessage(
            @RequestParam("senderId") Long senderId,
            @RequestParam("conversationId") Long conversationId, // <-- Link to conversation
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        String fileName = fileStorageService.saveFile(file);

        RequestMessagesDto requestMessagesDto = new RequestMessagesDto();
        requestMessagesDto.setSenderId(senderId);
        requestMessagesDto.setConversationId(conversationId);
        requestMessagesDto.setContent(content);
        requestMessagesDto.setFileName(fileName);
        requestMessagesDto.setFileUrl("/uploads/" + fileName);

        return ResponseEntity.ok(messagesService.saveMessageWithFile(requestMessagesDto, file));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable("id") Long id) {
        messagesService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }

}
