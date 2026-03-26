package com.artembredak.sporteventscalendar.infrastructure.persistence.mapper;

import com.artembredak.sporteventscalendar.domain.model.Team;
import com.artembredak.sporteventscalendar.infrastructure.persistence.entity.TeamEntity;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {

    public Team toDomain(TeamEntity entity) {
        return new Team(
                entity.getId(),
                entity.getName(),
                entity.getOfficialName(),
                entity.getSlug(),
                entity.getAbbreviation(),
                entity.getCountryCode()
        );
    }
}
