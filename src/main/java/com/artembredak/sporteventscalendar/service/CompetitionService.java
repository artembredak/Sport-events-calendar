package com.artembredak.sporteventscalendar.service;

import com.artembredak.sporteventscalendar.domain.model.Competition;
import com.artembredak.sporteventscalendar.domain.repository.CompetitionRepository;
import com.artembredak.sporteventscalendar.domain.usecase.CompetitionUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CompetitionService implements CompetitionUseCase {

    private final CompetitionRepository competitionRepository;

    public CompetitionService(CompetitionRepository competitionRepository) {
        this.competitionRepository = competitionRepository;
    }

    @Override
    public List<Competition> findAll() {
        return competitionRepository.findAll();
    }

    @Override
    public Optional<Competition> findById(Long id) {
        return competitionRepository.findById(id);
    }
}