package com.artembredak.sporteventscalendar.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;

public record Event(
        Long id,
        LocalDate eventDate,
        LocalTime eventTime,
        String status,
        Long competitionId,
        Long venueId,
        String stageId
) {}
