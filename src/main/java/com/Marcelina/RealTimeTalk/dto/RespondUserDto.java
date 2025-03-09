package com.Marcelina.RealTimeTalk.dto;
import com.Marcelina.RealTimeTalk.model.Messages;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RespondUserDto {
    private Long id;
    private String username;
    private String email;
    private List<Long> sendMessages;
    private List<Long> receivedMessages;
    private String photoUrl;

    public RespondUserDto(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
}