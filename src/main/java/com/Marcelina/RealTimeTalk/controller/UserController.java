package com.Marcelina.RealTimeTalk.controller;

import com.Marcelina.RealTimeTalk.config.LoginRequest;
import com.Marcelina.RealTimeTalk.dto.ProfileUpdateDTO;
import com.Marcelina.RealTimeTalk.dto.RequestUserDto;
import com.Marcelina.RealTimeTalk.dto.RespondConversationDto;
import com.Marcelina.RealTimeTalk.dto.RespondUserDto;
import com.Marcelina.RealTimeTalk.exeption.UsernameAlreadyUsedException;
import com.Marcelina.RealTimeTalk.mapper.ConversationMapper;
import com.Marcelina.RealTimeTalk.mapper.UserMapper;
import com.Marcelina.RealTimeTalk.model.Users;
import com.Marcelina.RealTimeTalk.service.ConversationService;
import com.Marcelina.RealTimeTalk.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ConversationService conversationService;
    private final UserMapper userMapper;
    private final ConversationMapper conversationMapper;


    @GetMapping
    public ResponseEntity<List<RespondUserDto>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/login")
    public ResponseEntity<RespondUserDto> login(@RequestBody LoginRequest loginRequest) {
        Users user = userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());

        if (user != null) {
            RespondUserDto userDto = new RespondUserDto(user.getId(), user.getUsername(), user.getEmail(), user.getPhotoUrl());
            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/{userId}/conversations")
    public ResponseEntity<List<RespondConversationDto>> getUserConversations(@PathVariable Long userId) {
        return ResponseEntity.ok(conversationService.getUserConversations(userId));
    }


    @GetMapping("/{userId}")
    public ResponseEntity<RespondUserDto> findUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }


    @PostMapping("/{userId}/start-conversation/{recipientId}")
    public ResponseEntity<RespondConversationDto> startConversation(
            @PathVariable Long userId, @PathVariable Long recipientId) {

        RespondConversationDto respondConversationDto = new RespondConversationDto();
        RespondUserDto senderDto = userMapper.mapToResponse(userService.findUserById(userId));
        RespondUserDto receiverDto = userMapper.mapToResponse(userService.findUserById(recipientId));

        respondConversationDto.setParticipants(List.of(senderDto, receiverDto));
        respondConversationDto.setIsGroup(false);
        respondConversationDto.setGroupName(null);

        return ResponseEntity.status(HttpStatus.CREATED).body(conversationService.createConversation(conversationMapper.mapToRequest(respondConversationDto)));
    }


    @PostMapping("/register")
    public ResponseEntity<RespondUserDto> register(@Valid @RequestBody RequestUserDto requestUserDto) {
        return ResponseEntity.ok(userService.save(requestUserDto));
    }

    @PutMapping("/update-profile/{userId}")
    public ResponseEntity<RespondUserDto> updateProfile(
            @PathVariable Long userId,
            @RequestPart("username") String username,
            @RequestPart("email") String email,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {

        ProfileUpdateDTO profileUpdateDTO = new ProfileUpdateDTO();
        profileUpdateDTO.setUsername(username);
        profileUpdateDTO.setEmail(email);

        RespondUserDto updatedUser = userService.updateProfile(userId, profileUpdateDTO, photo);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/find/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
