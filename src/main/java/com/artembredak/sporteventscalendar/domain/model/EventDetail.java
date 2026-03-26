package com.artembredak.sporteventscalendar.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;

public record EventDetail(
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
        TeamInfo homeTeam,
        TeamInfo awayTeam,
        Integer homeGoals,
        Integer awayGoals,
        String winner
) {}
