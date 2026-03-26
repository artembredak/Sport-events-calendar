package com.artembredak.sporteventscalendar.infrastructure.web.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record EventResponse(
        Long id,
        LocalDate eventDate,
        LocalTime eventTime,
        String status,
        Long competitionId,
        Long venueId,
        String stageId
) {}
