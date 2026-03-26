package com.artembredak.sporteventscalendar.service;

import com.artembredak.sporteventscalendar.domain.model.Sport;
import com.artembredak.sporteventscalendar.domain.repository.SportRepository;
import com.artembredak.sporteventscalendar.domain.usecase.SportUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SportService implements SportUseCase {

    private final SportRepository sportRepository;

    public SportService(SportRepository sportRepository) {
        this.sportRepository = sportRepository;
    }

    @Override
    public List<Sport> findAll() {
        return sportRepository.findAll();
    }

    @Override
    public Optional<Sport> findById(Long id) {
        return sportRepository.findById(id);
    }
}
