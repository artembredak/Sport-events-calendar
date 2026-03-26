package com.artembredak.sporteventscalendar.domain.usecase;

import com.artembredak.sporteventscalendar.domain.model.Event;
import com.artembredak.sporteventscalendar.domain.model.EventDetail;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventUseCase {

    List<EventDetail> findAllEvents();

    List<EventDetail> findEventsBySportId(Long sportId);

    List<EventDetail> findEventsByDate(LocalDate date);

    Optional<EventDetail> findEventById(Long id);

    Event createEvent(
            LocalDate eventDate,
            String eventTime,
            String status,
            Long competitionId,
            Long venueId,
            String stageId,
            Long homeTeamId,
            Long awayTeamId
    );
}
