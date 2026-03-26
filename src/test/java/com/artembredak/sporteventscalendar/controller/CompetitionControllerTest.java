package com.artembredak.sporteventscalendar.controller;

import com.artembredak.sporteventscalendar.domain.model.Competition;
import com.artembredak.sporteventscalendar.domain.usecase.CompetitionUseCase;
import com.artembredak.sporteventscalendar.infrastructure.web.controller.CompetitionController;
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
class CompetitionControllerTest {

    @Mock
    CompetitionUseCase competitionUseCase;

    @InjectMocks
    CompetitionController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("list_returnsAllCompetitions: returns 200 with all competitions from use case")
    void list_returnsAllCompetitions() throws Exception {
        given(competitionUseCase.findAll()).willReturn(List.of(
                new Competition(1L, "champions-league", "Champions League", 2024, 1L),
                new Competition(2L, "la-liga", "La Liga", 2024, 1L)
        ));

        mockMvc.perform(get("/api/v1/competitions"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Champions League"))
                .andExpect(jsonPath("$[0].slug").value("champions-league"))
                .andExpect(jsonPath("$[0].season").value(2024))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("La Liga"));

        then(competitionUseCase).should().findAll();
    }

    @Test
    @DisplayName("list_emptyRepository_returnsEmptyArray: returns 200 with empty JSON array when no competitions exist")
    void list_emptyRepository_returnsEmptyArray() throws Exception {
        given(competitionUseCase.findAll()).willReturn(List.of());

        mockMvc.perform(get("/api/v1/competitions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
