package com.Marcelina.RealTimeTalk.mapper;

import com.Marcelina.RealTimeTalk.dto.RequestUserDto;
import com.Marcelina.RealTimeTalk.dto.RespondUserDto;
import com.Marcelina.RealTimeTalk.model.Users;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public Users mapToEntity(RequestUserDto requestUserDto){
        Users user = new Users();
        user.setUsername(requestUserDto.getUsername());
        user.setPassword(requestUserDto.getPassword());
        user.setEmail(requestUserDto.getEmail());
        user.setPhotoUrl(requestUserDto.getPhotoUrl());
        return user;
    }

    public RespondUserDto mapToResponse(Users user){
        RespondUserDto response = new RespondUserDto();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setPhotoUrl(user.getPhotoUrl());

        return response;
    }

}
