package com.artembredak.sporteventscalendar.infrastructure.persistence.mapper;

import com.artembredak.sporteventscalendar.domain.model.Event;
import com.artembredak.sporteventscalendar.infrastructure.persistence.entity.EventEntity;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public Event toDomain(EventEntity entity) {
        return new Event(
                entity.getId(),
                entity.getEventDate(),
                entity.getEventTime(),
                entity.getStatus(),
                entity.getCompetitionId(),
                entity.getVenueId(),
                entity.getStageId()
        );
    }

    public EventEntity toEntity(Event domain) {
        EventEntity entity = new EventEntity();
        entity.setId(domain.id());
        entity.setEventDate(domain.eventDate());
        entity.setEventTime(domain.eventTime());
        entity.setStatus(domain.status());
        entity.setCompetitionId(domain.competitionId());
        entity.setVenueId(domain.venueId());
        entity.setStageId(domain.stageId());
        return entity;
    }
}
