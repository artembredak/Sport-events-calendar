package com.artembredak.sporteventscalendar.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record CreateEventRequest(

        @NotNull(message = "eventDate is required")
        LocalDate eventDate,

        @NotBlank(message = "eventTime is required")
        @Pattern(regexp = "\\d{2}:\\d{2}", message = "eventTime must be in HH:mm format")
        String eventTime,

        @NotBlank(message = "status is required")
        @Pattern(regexp = "SCHEDULED|PLAYED|CANCELLED|POSTPONED",
                message = "status must be SCHEDULED, PLAYED, CANCELLED, or POSTPONED")
        String status,

        @NotNull(message = "competitionId is required")
        Long competitionId,

        Long venueId,

        @NotBlank(message = "stageId is required")
        String stageId,

        Long homeTeamId,

        Long awayTeamId
) {}
