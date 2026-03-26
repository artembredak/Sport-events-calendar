package com.artembredak.sporteventscalendar.infrastructure.persistence.repository;

import com.artembredak.sporteventscalendar.domain.model.Competition;
import com.artembredak.sporteventscalendar.domain.repository.CompetitionRepository;
import com.artembredak.sporteventscalendar.infrastructure.persistence.mapper.CompetitionMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CompetitionRepositoryAdapter implements CompetitionRepository {

    private final CompetitionJpaRepository jpa;
    private final CompetitionMapper mapper;

    public CompetitionRepositoryAdapter(CompetitionJpaRepository jpa, CompetitionMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public List<Competition> findAll() {
        return jpa.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Competition> findById(Long id) {
        return jpa.findById(id).map(mapper::toDomain);
    }
}
