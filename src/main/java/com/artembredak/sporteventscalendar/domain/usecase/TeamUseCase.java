package com.artembredak.sporteventscalendar.domain.usecase;

import com.artembredak.sporteventscalendar.domain.model.Team;

import java.util.List;
import java.util.Optional;

public interface TeamUseCase {

    List<Team> findAll();

    Optional<Team> findById(Long id);
}
