package com.dto.activity;

import com.enums.EventType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class RecentActivityDTO {

    private EventType eventType;
    private String text;
    private LocalDateTime timestamp;
}
