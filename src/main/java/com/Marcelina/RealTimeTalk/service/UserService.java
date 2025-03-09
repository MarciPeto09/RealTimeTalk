package com.Marcelina.RealTimeTalk.service;

import com.Marcelina.RealTimeTalk.dto.ProfileUpdateDTO;
import com.Marcelina.RealTimeTalk.dto.RequestUserDto;
import com.Marcelina.RealTimeTalk.dto.RespondUserDto;
import com.Marcelina.RealTimeTalk.exeption.UsernameAlreadyUsedException;
import com.Marcelina.RealTimeTalk.mapper.UserMapper;
import com.Marcelina.RealTimeTalk.model.Users;
import com.Marcelina.RealTimeTalk.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final  UserMapper userMapper;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public List<RespondUserDto> getAllUsers(){
       List<Users> listOfUseres = userRepository.findAll();
        return listOfUseres.stream()
                .map(userMapper::mapToResponse)
                .collect(Collectors.toList());
    }



    public RespondUserDto getUserById(Long id){
        return userMapper.mapToResponse(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("There is no user with this id!")));
    }


    public RespondUserDto getUserByUsername(String username) {
        logger.info("Fetching user with username: {}", username);
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username '" + username + "' does not exist."));
        return userMapper.mapToResponse(user);
    }


    @Transactional
    public void deleteUser(String username) {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username '" + username + "' does not exist."));
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


    public Users connect(Users user) throws UsernameAlreadyUsedException {
        Users dbUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User with username '" + user.getUsername() + "' does not exist"));

        if (dbUser.getConnected()) {
            throw new UsernameAlreadyUsedException("User '" + dbUser.getUsername() + "' is already connected.");
        }

        dbUser.setConnected(true);
        userRepository.save(dbUser);
        logger.info("User with username '{}' connected.", dbUser.getUsername());
        return dbUser;
    }


    public Users disconnect(Users user) {
        if (user == null || user.getUsername() == null) {
            return null;
        }

        Users dbUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User with username '" + user.getUsername() + "' does not exist"));

        dbUser.setConnected(false);
        userRepository.save(dbUser);
        logger.info("User with username '{}' disconnected.", dbUser.getUsername());
        return dbUser;
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


    @Transactional
    public RespondUserDto updateProfile(Long userId, ProfileUpdateDTO profileUpdateDTO, MultipartFile photo) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update username and email
        user.setUsername(profileUpdateDTO.getUsername());
        user.setEmail(profileUpdateDTO.getEmail());

        // Handle profile photo upload
        if (photo != null && !photo.isEmpty()) {
            String photoUrl = fileStorageService.storeFile( photo); // Save the file and get its URL
            user.setPhotoUrl(photoUrl);
        }

         Users savedUser = userRepository.save(user);
         RespondUserDto respondUserDto = userMapper.mapToResponse(savedUser);
         return respondUserDto;
    }

}
