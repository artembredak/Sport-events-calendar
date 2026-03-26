package com.artembredak.sporteventscalendar.service;

import com.artembredak.sporteventscalendar.domain.model.Team;
import com.artembredak.sporteventscalendar.domain.repository.TeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class TeamServiceTest {

    @Mock
    TeamRepository teamRepository;

    @InjectMocks
    TeamService teamService;

    private static Team makeTeam(long id, String name) {
        return new Team(id, name, name + " Official", name.toLowerCase().replace(" ", "-"), "ABV", "KSA");
    }

    @Test
    @DisplayName("getAllTeams_returnsAll: delegates to repository and returns all teams")
    void getAllTeams_returnsAll() {
        List<Team> expected = List.of(makeTeam(1L, "Al Hilal"), makeTeam(2L, "Al Shabab"));
        given(teamRepository.findAll()).willReturn(expected);

        List<Team> result = teamService.findAll();

        assertThat(result).hasSize(2).isEqualTo(expected);
        then(teamRepository).should().findAll();
    }

    @Test
    @DisplayName("getTeamById_found: returns Optional containing the team when it exists")
    void getTeamById_found() {
        Team team = makeTeam(1L, "Al Hilal");
        given(teamRepository.findById(1L)).willReturn(Optional.of(team));

        Optional<Team> result = teamService.findById(1L);

        assertThat(result).isPresent().contains(team);
        then(teamRepository).should().findById(1L);
    }
}