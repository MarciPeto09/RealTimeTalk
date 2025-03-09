package com.Marcelina.RealTimeTalk.dto;

import com.Marcelina.RealTimeTalk.model.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RespondMessagesDto {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String fileName;
    private String fileUrl;
    private String content;
    private LocalDateTime timestamp;
}
