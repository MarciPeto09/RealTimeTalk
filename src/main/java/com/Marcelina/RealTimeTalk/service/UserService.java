package com.Marcelina.RealTimeTalk.service;

import com.Marcelina.RealTimeTalk.dto.ProfileUpdateDTO;
import com.Marcelina.RealTimeTalk.dto.RequestUserDto;
import com.Marcelina.RealTimeTalk.dto.RespondUserDto;
import com.Marcelina.RealTimeTalk.mapper.UserMapper;
import com.Marcelina.RealTimeTalk.model.Users;
import com.Marcelina.RealTimeTalk.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;


    public List<RespondUserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::mapToResponse)
                .collect(Collectors.toList());
    }



    public RespondUserDto getUserById(Long id) {
        return userMapper.mapToResponse(findUserById(id));
    }


    public Users findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID '" + id + "' does not exist"));
    }

    @Transactional
    public void deleteUser(Long id) {
        Users user = findUserById(id);
        userRepository.delete(user);
    }

    public RespondUserDto save(RequestUserDto requestUserDto) {
        if (userRepository.findByUsername(requestUserDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User with username '" + requestUserDto.getUsername() + "' already exists.");
        }
        Users savedUser = userMapper.mapToEntity(requestUserDto);
        userRepository.save(savedUser);
        return userMapper.mapToResponse(savedUser);
    }

    @Transactional
    public RespondUserDto updateProfile(Long userId, ProfileUpdateDTO profileUpdateDTO, MultipartFile photo) {
        Users user = findUserById(userId);

        user.setUsername(profileUpdateDTO.getUsername());
        user.setEmail(profileUpdateDTO.getEmail());

        if (photo != null && !photo.isEmpty()) {
            user.setPhotoUrl(fileStorageService.storeFile(photo));
        }

        Users savedUser = userRepository.save(user);
        return userMapper.mapToResponse(savedUser);
    }

    public Users authenticateUser(String username, String password) {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username '" + username + "' does not exist"));

        if (user.getPassword().equals(password)) {
            return user;
        } else {
            return null;
        }
    }

    public List<Users> getAllUsersById(List<Long> participantIds) {
        return userRepository.findAllById(participantIds);
    }
}
