package com.artembredak.sporteventscalendar.infrastructure.persistence.mapper;

import com.artembredak.sporteventscalendar.domain.model.Sport;
import com.artembredak.sporteventscalendar.infrastructure.persistence.entity.SportEntity;
import org.springframework.stereotype.Component;

@Component
public class SportMapper {

    public Sport toDomain(SportEntity entity) {
        return new Sport(entity.getId(), entity.getName());
    }

    public SportEntity toEntity(Sport domain) {
        SportEntity entity = new SportEntity();
        entity.setId(domain.id());
        entity.setName(domain.name());
        return entity;
    }
}
