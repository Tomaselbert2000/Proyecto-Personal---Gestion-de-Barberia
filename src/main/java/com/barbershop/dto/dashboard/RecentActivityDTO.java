package com.barbershop.dto.dashboard;

import com.barbershop.enums.EventType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecentActivityDTO {

    private EventType eventType;
    private String text;
    private LocalDateTime timestamp;
}
