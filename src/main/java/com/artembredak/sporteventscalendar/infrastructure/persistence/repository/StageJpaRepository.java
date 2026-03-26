package com.artembredak.sporteventscalendar.infrastructure.persistence.repository;

import com.artembredak.sporteventscalendar.infrastructure.persistence.entity.StageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StageJpaRepository extends JpaRepository<StageEntity, String> {}

