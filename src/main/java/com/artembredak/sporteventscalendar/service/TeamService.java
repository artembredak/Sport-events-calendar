package com.artembredak.sporteventscalendar.service;

import com.artembredak.sporteventscalendar.domain.model.Team;
import com.artembredak.sporteventscalendar.domain.repository.TeamRepository;
import com.artembredak.sporteventscalendar.domain.usecase.TeamUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class TeamService implements TeamUseCase {

    private final TeamRepository teamRepository;

    @Override
    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    @Override
    public Optional<Team> findById(Long id) {
        return teamRepository.findById(id);
    }
}