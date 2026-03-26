package com.artembredak.sporteventscalendar.controller;

import com.artembredak.sporteventscalendar.domain.model.Team;
import com.artembredak.sporteventscalendar.domain.usecase.TeamUseCase;
import com.artembredak.sporteventscalendar.infrastructure.web.controller.TeamController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class TeamControllerTest {

    @Mock
    TeamUseCase teamUseCase;

    @InjectMocks
    TeamController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private static Team makeTeam(long id, String name) {
        return new Team(id, name, name + " FC", name.toLowerCase().replace(" ", "-"), "ABV", "ESP");
    }

    @Test
    @DisplayName("list_returnsAllTeams: returns 200 with all teams from use case")
    void list_returnsAllTeams() throws Exception {
        given(teamUseCase.findAll()).willReturn(List.of(
                makeTeam(1L, "Real Madrid"),
                makeTeam(2L, "Barcelona")
        ));

        mockMvc.perform(get("/api/v1/teams"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Real Madrid"))
                .andExpect(jsonPath("$[0].abbreviation").value("ABV"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Barcelona"));

        then(teamUseCase).should().findAll();
    }

    @Test
    @DisplayName("list_emptyRepository_returnsEmptyArray: returns 200 with empty JSON array when no teams exist")
    void list_emptyRepository_returnsEmptyArray() throws Exception {
        given(teamUseCase.findAll()).willReturn(List.of());

        mockMvc.perform(get("/api/v1/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
