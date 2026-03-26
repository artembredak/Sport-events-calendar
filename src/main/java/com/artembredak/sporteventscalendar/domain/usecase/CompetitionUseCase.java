package com.artembredak.sporteventscalendar.domain.usecase;

import com.artembredak.sporteventscalendar.domain.model.Competition;

import java.util.List;
import java.util.Optional;

public interface CompetitionUseCase {

    List<Competition> findAll();

    Optional<Competition> findById(Long id);
}
