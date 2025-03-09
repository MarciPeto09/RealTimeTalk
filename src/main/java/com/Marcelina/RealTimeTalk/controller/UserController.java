package com.Marcelina.RealTimeTalk.controller;

import com.Marcelina.RealTimeTalk.config.LoginRequest;
import com.Marcelina.RealTimeTalk.dto.ProfileUpdateDTO;
import com.Marcelina.RealTimeTalk.dto.RequestUserDto;
import com.Marcelina.RealTimeTalk.dto.RespondUserDto;
import com.Marcelina.RealTimeTalk.exeption.UsernameAlreadyUsedException;
import com.Marcelina.RealTimeTalk.mapper.UserMapper;
import com.Marcelina.RealTimeTalk.model.Users;
import com.Marcelina.RealTimeTalk.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

    @RestController
    @RequestMapping("/api/user")
    @AllArgsConstructor
    public class UserController {

        private final UserService userService;
        private final UserMapper userMapper;

        @GetMapping
        public ResponseEntity<List<RespondUserDto>> getAllUsers(){
            return ResponseEntity.ok(userService.getAllUsers());
        }

        @PostMapping("/register")
        public ResponseEntity<RespondUserDto> register(@Valid @RequestBody RequestUserDto requestUserDto) {
            return ResponseEntity.ok(userService.save(requestUserDto));
        }

        @DeleteMapping("/find/{username}")
        public ResponseEntity<Void> deleteUser(@PathVariable String username) {
            userService.deleteUser(username);
            return ResponseEntity.noContent().build();
        }

        @GetMapping("/{username}")
        public ResponseEntity<RespondUserDto> getUserByUsername(@PathVariable String username) {
            RespondUserDto respondUserDto = userService.getUserByUsername(username);
            return ResponseEntity.ok(respondUserDto);
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


    }
