package com.artembredak.sporteventscalendar.infrastructure.persistence.repository;

import com.artembredak.sporteventscalendar.domain.model.Team;
import com.artembredak.sporteventscalendar.domain.repository.TeamRepository;
import com.artembredak.sporteventscalendar.infrastructure.persistence.mapper.TeamMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TeamRepositoryAdapter implements TeamRepository {

    private final TeamJpaRepository jpa;
    private final TeamMapper mapper;

    public TeamRepositoryAdapter(TeamJpaRepository jpa, TeamMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public List<Team> findAll() {
        return jpa.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Team> findById(Long id) {
        return jpa.findById(id).map(mapper::toDomain);
    }
}