package com.artembredak.sporteventscalendar.domain.repository;

import com.artembredak.sporteventscalendar.domain.model.Team;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository {

    List<Team> findAll();

    Optional<Team> findById(Long id);
}
