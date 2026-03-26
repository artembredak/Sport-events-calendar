package com.artembredak.sporteventscalendar.domain.repository;

import com.artembredak.sporteventscalendar.domain.model.Competition;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface CompetitionRepository {

    List<Competition> findAll();

    Optional<Competition> findById(Long id);
}
