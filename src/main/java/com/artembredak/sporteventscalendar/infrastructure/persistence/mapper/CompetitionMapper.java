package com.artembredak.sporteventscalendar.infrastructure.persistence.mapper;

import com.artembredak.sporteventscalendar.domain.model.Competition;
import com.artembredak.sporteventscalendar.infrastructure.persistence.entity.CompetitionEntity;
import org.springframework.stereotype.Component;

@Component
public class CompetitionMapper {

    public Competition toDomain(CompetitionEntity entity) {
        return new Competition(
                entity.getId(),
                entity.getSlug(),
                entity.getName(),
                entity.getSeason(),
                entity.getSportId()
        );
    }
}