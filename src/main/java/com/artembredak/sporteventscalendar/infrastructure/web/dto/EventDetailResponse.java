package com.artembredak.sporteventscalendar.infrastructure.web.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record EventDetailResponse(
        Long id,
        LocalDate eventDate,
        LocalTime eventTime,
        String status,
        String competitionName,
        String competitionSlug,
        int season,
        String sportName,
        String stageName,
        int stageOrdering,
        String venueName,
        String venueCity,
        TeamInfoResponse homeTeam,
        TeamInfoResponse awayTeam,
        Integer homeGoals,
        Integer awayGoals,
        String winner
) {}