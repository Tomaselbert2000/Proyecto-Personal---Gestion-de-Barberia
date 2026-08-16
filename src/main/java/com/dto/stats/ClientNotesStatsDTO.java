package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class ClientNotesStatsDTO {

    public ClientNotesStatsDTO(Long clientsWithNotes, Long clientsWithoutNotes) {

        this.clientsWithNotes = clientsWithNotes;
        this.clientsWithoutNotes = clientsWithoutNotes;
    }

    @Builder.Default
    private Long clientsWithNotes = 0L;
    @Builder.Default
    private Long clientsWithoutNotes = 0L;
    @Builder.Default
    private Double clientsWithNotesPercentage = 0.0;
}
