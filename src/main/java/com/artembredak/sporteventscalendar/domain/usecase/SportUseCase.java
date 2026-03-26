package com.artembredak.sporteventscalendar.domain.usecase;

import com.artembredak.sporteventscalendar.domain.model.Sport;

import java.util.List;
import java.util.Optional;

public interface SportUseCase {

    List<Sport> findAll();

    Optional<Sport> findById(Long id);
}
