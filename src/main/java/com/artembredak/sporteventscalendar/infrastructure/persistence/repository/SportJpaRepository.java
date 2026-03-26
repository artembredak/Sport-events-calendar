package com.artembredak.sporteventscalendar.infrastructure.persistence.repository;

import com.artembredak.sporteventscalendar.infrastructure.persistence.entity.EventTeamId;
import com.artembredak.sporteventscalendar.infrastructure.persistence.entity.SportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SportJpaRepository extends JpaRepository<SportEntity, Long> {}
