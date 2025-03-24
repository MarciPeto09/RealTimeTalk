package com.Marcelina.RealTimeTalk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestConversationDto {
    private List<Long> participantIds;
    private Boolean isGroup;
    private String groupName;
}
