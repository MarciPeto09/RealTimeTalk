package com.Marcelina.RealTimeTalk.dto;

import com.Marcelina.RealTimeTalk.model.Messages;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestUserDto {

    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @NotEmpty(message = "Email cannot be empty")
    private String email;

    private String photoUrl;

    public RequestUserDto(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
