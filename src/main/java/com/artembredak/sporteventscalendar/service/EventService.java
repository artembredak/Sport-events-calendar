package com.artembredak.sporteventscalendar.service;

import com.artembredak.sporteventscalendar.domain.model.Event;
import com.artembredak.sporteventscalendar.domain.model.EventDetail;
import com.artembredak.sporteventscalendar.domain.repository.EventRepository;
import com.artembredak.sporteventscalendar.domain.usecase.EventUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EventService implements EventUseCase {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDetail> findAllEvents() {
        return eventRepository.findAllWithDetail();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDetail> findEventsBySportId(Long sportId) {
        return eventRepository.findAllWithDetailBySportId(sportId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDetail> findEventsByDate(LocalDate date) {
        return eventRepository.findAllWithDetailByDate(date);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventDetail> findEventById(Long id) {
        return eventRepository.findDetailById(id);
    }

    @Override
    public Event createEvent(
            LocalDate eventDate,
            String eventTime,
            String status,
            Long competitionId,
            Long venueId,
            String stageId,
            Long homeTeamId,
            Long awayTeamId) {

        if (!eventRepository.competitionExists(competitionId)) {
            throw new IllegalArgumentException("Competition not found: " + competitionId);
        }
        if (venueId != null && !eventRepository.venueExists(venueId)) {
            throw new IllegalArgumentException("Venue not found: " + venueId);
        }
        if (!eventRepository.stageExists(stageId)) {
            throw new IllegalArgumentException("Stage not found: " + stageId);
        }
        if (homeTeamId != null && !eventRepository.teamExists(homeTeamId)) {
            throw new IllegalArgumentException("Home team not found: " + homeTeamId);
        }
        if (awayTeamId != null && !eventRepository.teamExists(awayTeamId)) {
            throw new IllegalArgumentException("Away team not found: " + awayTeamId);
        }

        LocalTime parsedTime = LocalTime.parse(eventTime);

        Event event = new Event(null, eventDate, parsedTime, status, competitionId, venueId, stageId);
        Event saved = eventRepository.save(event);

        if (homeTeamId != null) {
            eventRepository.saveEventTeam(saved.id(), homeTeamId, "HOME");
        }
        if (awayTeamId != null) {
            eventRepository.saveEventTeam(saved.id(), awayTeamId, "AWAY");
        }

        return saved;
    }
}