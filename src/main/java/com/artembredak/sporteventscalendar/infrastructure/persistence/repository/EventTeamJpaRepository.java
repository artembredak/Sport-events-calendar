package com.artembredak.sporteventscalendar.infrastructure.persistence.repository;

import com.artembredak.sporteventscalendar.infrastructure.persistence.entity.EventTeamEntity;
import com.artembredak.sporteventscalendar.infrastructure.persistence.entity.EventTeamId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventTeamJpaRepository extends JpaRepository<EventTeamEntity, EventTeamId> {}

