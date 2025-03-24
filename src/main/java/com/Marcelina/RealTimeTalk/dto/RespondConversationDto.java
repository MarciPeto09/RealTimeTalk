package com.Marcelina.RealTimeTalk.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RespondConversationDto {
    private Long id;
    private List<RespondUserDto> participants;
    private Boolean isGroup;
    private String groupName;
    private LocalDateTime createdAt;
}
