package com.artembredak.sporteventscalendar.domain.repository;

import com.artembredak.sporteventscalendar.domain.model.Sport;

import java.util.List;
import java.util.Optional;

public interface SportRepository {

    List<Sport> findAll();

    Optional<Sport> findById(Long id);
}
