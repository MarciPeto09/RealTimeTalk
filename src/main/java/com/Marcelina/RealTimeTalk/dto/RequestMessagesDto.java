package com.Marcelina.RealTimeTalk.dto;

import com.Marcelina.RealTimeTalk.model.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestMessagesDto {
    private Long senderId;
    private Long receiverId;
    private String content;
    private String fileName;
    private String fileUrl;
}
