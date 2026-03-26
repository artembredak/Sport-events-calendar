package com.artembredak.sporteventscalendar.infrastructure.web.controller;

import com.artembredak.sporteventscalendar.domain.model.Sport;
import com.artembredak.sporteventscalendar.domain.usecase.SportUseCase;
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
class SportControllerTest {

    @Mock
    SportUseCase sportUseCase;

    @InjectMocks
    SportController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("list_returnsAllSports: returns 200 with all sports from use case")
    void list_returnsAllSports() throws Exception {
        given(sportUseCase.findAll()).willReturn(List.of(
                new Sport(1L, "Football"),
                new Sport(2L, "Basketball")
        ));

        mockMvc.perform(get("/api/v1/sports"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Football"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Basketball"));

        then(sportUseCase).should().findAll();
    }

    @Test
    @DisplayName("list_emptyRepository_returnsEmptyArray: returns 200 with empty JSON array when no sports exist")
    void list_emptyRepository_returnsEmptyArray() throws Exception {
        given(sportUseCase.findAll()).willReturn(List.of());

        mockMvc.perform(get("/api/v1/sports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
