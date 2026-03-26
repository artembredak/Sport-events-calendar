package com.artembredak.sporteventscalendar.infrastructure.persistence.repository;

import com.artembredak.sporteventscalendar.domain.model.Event;
import com.artembredak.sporteventscalendar.domain.model.EventDetail;
import com.artembredak.sporteventscalendar.domain.repository.EventRepository;
import com.artembredak.sporteventscalendar.infrastructure.persistence.entity.EventEntity;
import com.artembredak.sporteventscalendar.infrastructure.persistence.entity.EventTeamEntity;
import com.artembredak.sporteventscalendar.infrastructure.persistence.mapper.EventDetailRowMapper;
import com.artembredak.sporteventscalendar.infrastructure.persistence.mapper.EventMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class EventRepositoryAdapter implements EventRepository {

    private final EventJpaRepository eventJpa;
    private final EventTeamJpaRepository eventTeamJpa;
    private final CompetitionJpaRepository competitionJpa;
    private final VenueJpaRepository venueJpa;
    private final StageJpaRepository stageJpa;
    private final TeamJpaRepository teamJpa;
    private final EventMapper eventMapper;
    private final EventDetailRowMapper rowMapper;

    public EventRepositoryAdapter(
            EventJpaRepository eventJpa,
            EventTeamJpaRepository eventTeamJpa,
            CompetitionJpaRepository competitionJpa,
            VenueJpaRepository venueJpa,
            StageJpaRepository stageJpa,
            TeamJpaRepository teamJpa,
            EventMapper eventMapper,
            EventDetailRowMapper rowMapper) {
        this.eventJpa = eventJpa;
        this.eventTeamJpa = eventTeamJpa;
        this.competitionJpa = competitionJpa;
        this.venueJpa = venueJpa;
        this.stageJpa = stageJpa;
        this.teamJpa = teamJpa;
        this.eventMapper = eventMapper;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<EventDetail> findAllWithDetail() {
        return rowMapper.mapAll(eventJpa.findAllEventDetails());
    }

    @Override
    public List<EventDetail> findAllWithDetailBySportId(Long sportId) {
        return rowMapper.mapAll(eventJpa.findEventDetailsBySportId(sportId));
    }

    @Override
    public List<EventDetail> findAllWithDetailByDate(LocalDate date) {
        return rowMapper.mapAll(eventJpa.findEventDetailsByDate(date));
    }

    @Override
    public Optional<EventDetail> findDetailById(Long id) {
        List<Object[]> rows = eventJpa.findEventDetailById(id);
        if (rows.isEmpty()) return Optional.empty();
        return Optional.of(rowMapper.map(rows.get(0)));
    }

    @Override
    public Event save(Event event) {
        EventEntity entity = eventMapper.toEntity(event);
        EventEntity saved = eventJpa.save(entity);
        return eventMapper.toDomain(saved);
    }

    @Override
    public void saveEventTeam(Long eventId, Long teamId, String role) {
        eventTeamJpa.save(new EventTeamEntity(eventId, teamId, role));
    }

    @Override
    public boolean competitionExists(Long competitionId) {
        return competitionJpa.existsById(competitionId);
    }

    @Override
    public boolean venueExists(Long venueId) {
        return venueJpa.existsById(venueId);
    }

    @Override
    public boolean stageExists(String stageId) {
        return stageJpa.existsById(stageId);
    }

    @Override
    public boolean teamExists(Long teamId) {
        return teamJpa.existsById(teamId);
    }
}
