package com.artembredak.sporteventscalendar.domain.repository;

import com.artembredak.sporteventscalendar.domain.model.Event;
import com.artembredak.sporteventscalendar.domain.model.EventDetail;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventRepository {

    List<EventDetail> findAllWithDetail();

    List<EventDetail> findAllWithDetailBySportId(Long sportId);

    List<EventDetail> findAllWithDetailByDate(LocalDate date);

    Optional<EventDetail> findDetailById(Long id);

    Event save(Event event);

    void saveEventTeam(Long eventId, Long teamId, String role);

    boolean competitionExists(Long competitionId);

    boolean venueExists(Long venueId);

    boolean stageExists(String stageId);

    boolean teamExists(Long teamId);
}
