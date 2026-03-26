package com.artembredak.sporteventscalendar.infrastructure.persistence.repository;

import com.artembredak.sporteventscalendar.infrastructure.persistence.entity.CompetitionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetitionJpaRepository extends JpaRepository<CompetitionEntity, Long> {}

