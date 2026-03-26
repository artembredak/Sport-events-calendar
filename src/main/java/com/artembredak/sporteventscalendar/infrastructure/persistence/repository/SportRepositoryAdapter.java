package com.artembredak.sporteventscalendar.infrastructure.persistence.repository;

import com.artembredak.sporteventscalendar.domain.model.Sport;
import com.artembredak.sporteventscalendar.domain.repository.SportRepository;
import com.artembredak.sporteventscalendar.infrastructure.persistence.mapper.SportMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SportRepositoryAdapter implements SportRepository {

    private final SportJpaRepository jpa;
    private final SportMapper mapper;

    public SportRepositoryAdapter(SportJpaRepository jpa, SportMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public List<Sport> findAll() {
        return jpa.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Sport> findById(Long id) {
        return jpa.findById(id).map(mapper::toDomain);
    }
}
