package com.artembredak.sporteventscalendar.controller;

import com.artembredak.sporteventscalendar.domain.model.Event;
import com.artembredak.sporteventscalendar.domain.model.EventDetail;
import com.artembredak.sporteventscalendar.domain.model.TeamInfo;
import com.artembredak.sporteventscalendar.domain.usecase.EventUseCase;
import com.artembredak.sporteventscalendar.infrastructure.web.controller.EventController;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class EventControllerTest {

    @Mock
    EventUseCase eventUseCase;

    @InjectMocks
    EventController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private static EventDetail makeDetail(long id) {
        return new EventDetail(
                id,
                LocalDate.of(2024, 1, 3),
                LocalTime.of(16, 0),
                "SCHEDULED",
                "Champions League",
                "champions-league",
                2024,
                "Football",
                "Group Stage",
                1,
                "Bernabeu",
                "Madrid",
                new TeamInfo(1L, "Real Madrid", "RMA", "ESP"),
                new TeamInfo(2L, "Barcelona", "BAR", "ESP"),
                null, null, null
        );
    }

    private static Event makeSavedEvent(long id) {
        return new Event(id, LocalDate.of(2024, 1, 3), LocalTime.of(16, 0),
                "SCHEDULED", 1L, null, "GROUP_STAGE");
    }


    @Test
    @DisplayName("list_noParams_returnsAllEvents: calls findAllEvents and returns 200 with full list")
    void list_noParams_returnsAllEvents() throws Exception {
        given(eventUseCase.findAllEvents()).willReturn(List.of(makeDetail(1L), makeDetail(2L)));

        mockMvc.perform(get("/api/v1/events"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("SCHEDULED"))
                .andExpect(jsonPath("$[0].competitionName").value("Champions League"));

        then(eventUseCase).should().findAllEvents();
    }

    @Test
    @DisplayName("list_withSportId_filtersBySport: calls findEventsBySportId and returns matching events")
    void list_withSportId_filtersBySport() throws Exception {
        given(eventUseCase.findEventsBySportId(7L)).willReturn(List.of(makeDetail(3L)));

        mockMvc.perform(get("/api/v1/events").param("sportId", "7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(3));

        then(eventUseCase).should().findEventsBySportId(7L);
    }

    @Test
    @DisplayName("list_withDate_filtersByDate: calls findEventsByDate and returns matching events")
    void list_withDate_filtersByDate() throws Exception {
        LocalDate date = LocalDate.of(2024, 1, 3);
        given(eventUseCase.findEventsByDate(date)).willReturn(List.of(makeDetail(4L)));

        mockMvc.perform(get("/api/v1/events").param("date", "2024-01-03"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(4));

        then(eventUseCase).should().findEventsByDate(date);
    }


    @Test
    @DisplayName("getById_found_returns200: returns event detail when id exists")
    void getById_found_returns200() throws Exception {
        given(eventUseCase.findEventById(5L)).willReturn(Optional.of(makeDetail(5L)));

        mockMvc.perform(get("/api/v1/events/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.sportName").value("Football"))
                .andExpect(jsonPath("$.homeTeam.name").value("Real Madrid"))
                .andExpect(jsonPath("$.awayTeam.abbreviation").value("BAR"));
    }

    @Test
    @DisplayName("getById_notFound_returns404: returns 404 when event does not exist")
    void getById_notFound_returns404() throws Exception {
        given(eventUseCase.findEventById(999L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/events/999"))
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("create_validRequest_returns201WithLocation: saves event and returns 201 with Location header")
    void create_validRequest_returns201WithLocation() throws Exception {
        given(eventUseCase.createEvent(any(), any(), any(), any(), any(), any(), any(), any()))
                .willReturn(makeSavedEvent(42L));

        String body = """
                {
                  "eventDate": "2024-01-03",
                  "eventTime": "16:00",
                  "status": "SCHEDULED",
                  "competitionId": 1,
                  "stageId": "GROUP_STAGE"
                }
                """;

        mockMvc.perform(post("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/42")))
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.status").value("SCHEDULED"));
    }

    @Test
    @DisplayName("create_missingCompetitionId_returns400: returns 400 when required field is absent")
    void create_missingCompetitionId_returns400() throws Exception {
        String body = """
                {
                  "eventDate": "2024-01-03",
                  "eventTime": "16:00",
                  "status": "SCHEDULED",
                  "stageId": "GROUP_STAGE"
                }
                """;

        mockMvc.perform(post("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("create_invalidStatus_returns400: returns 400 when status value is not allowed")
    void create_invalidStatus_returns400() throws Exception {
        String body = """
                {
                  "eventDate": "2024-01-03",
                  "eventTime": "16:00",
                  "status": "INVALID_STATUS",
                  "competitionId": 1,
                  "stageId": "GROUP_STAGE"
                }
                """;

        mockMvc.perform(post("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}
