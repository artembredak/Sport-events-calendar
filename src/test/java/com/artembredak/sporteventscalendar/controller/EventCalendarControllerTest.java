package com.artembredak.sporteventscalendar.controller;

import com.artembredak.sporteventscalendar.domain.model.*;
import com.artembredak.sporteventscalendar.domain.usecase.CompetitionUseCase;
import com.artembredak.sporteventscalendar.domain.usecase.EventUseCase;
import com.artembredak.sporteventscalendar.domain.usecase.SportUseCase;
import com.artembredak.sporteventscalendar.domain.usecase.TeamUseCase;
import com.artembredak.sporteventscalendar.infrastructure.persistence.entity.StageEntity;
import com.artembredak.sporteventscalendar.infrastructure.persistence.repository.StageJpaRepository;
import com.artembredak.sporteventscalendar.infrastructure.web.controller.EventCalendarController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class EventCalendarControllerTest {

    @Mock private EventUseCase eventUseCase;
    @Mock private SportUseCase sportUseCase;
    @Mock private TeamUseCase teamUseCase;
    @Mock private CompetitionUseCase competitionUseCase;
    @Mock private StageJpaRepository stageJpaRepository;

    @InjectMocks
    private EventCalendarController controller;

    private MockMvc mockMvc;

    @ControllerAdvice
    static class GlobalExceptionHandler {
        @ExceptionHandler(IllegalArgumentException.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        void handleIllegalArgument() {}
    }

    @BeforeEach
    void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".html");
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setViewResolvers(viewResolver)
                .build();
    }

    private static EventDetail makeDetail(long id) {
        return new EventDetail(
                id, LocalDate.of(2024, 1, 3), LocalTime.of(16, 0), "SCHEDULED",
                "Champions League", "champions-league", 2024, "Football",
                "Group Stage", 1, "Bernabeu", "Madrid",
                new TeamInfo(1L, "Real Madrid", "RMA", "ESP"),
                new TeamInfo(2L, "Barcelona", "BAR", "ESP"),
                null, null, null
        );
    }

    private static StageEntity makeStageEntity(String id, String name, int ordering) {
        StageEntity e = new StageEntity();
        e.setId(id);
        e.setName(name);
        e.setOrdering(ordering);
        return e;
    }


    @Test
    @DisplayName("root_redirectsToEvents: GET / redirects to /events")
    void root_redirectsToEvents() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/events"));
    }


    @Test
    @DisplayName("listEvents_noParams_addsEventsAndSportsToModel: populates model with all events and sports")
    void listEvents_noParams_addsEventsAndSportsToModel() throws Exception {
        given(eventUseCase.findAllEvents()).willReturn(List.of(makeDetail(1L)));
        given(sportUseCase.findAll()).willReturn(List.of(new Sport(1L, "Football")));

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/list"))
                .andExpect(model().attributeExists("events", "sports"))
                .andExpect(model().attribute("selectedSportId", (Object) null))
                .andExpect(model().attribute("selectedDate", (Object) null));

        then(eventUseCase).should().findAllEvents();
    }

    @Test
    @DisplayName("listEvents_withSportId_filtersBySport: delegates to findEventsBySportId and sets selectedSportId")
    void listEvents_withSportId_filtersBySport() throws Exception {
        given(eventUseCase.findEventsBySportId(2L)).willReturn(List.of(makeDetail(1L)));
        given(sportUseCase.findAll()).willReturn(List.of(new Sport(2L, "Football")));

        mockMvc.perform(get("/events").param("sportId", "2"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/list"))
                .andExpect(model().attribute("selectedSportId", 2L));

        then(eventUseCase).should().findEventsBySportId(2L);
    }

    @Test
    @DisplayName("listEvents_withDate_filtersByDate: delegates to findEventsByDate and sets selectedDate")
    void listEvents_withDate_filtersByDate() throws Exception {
        LocalDate date = LocalDate.of(2024, 1, 3);
        given(eventUseCase.findEventsByDate(date)).willReturn(List.of(makeDetail(1L)));
        given(sportUseCase.findAll()).willReturn(List.of());

        mockMvc.perform(get("/events").param("date", "2024-01-03"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/list"))
                .andExpect(model().attribute("selectedDate", date));

        then(eventUseCase).should().findEventsByDate(date);
    }


    @Test
    @DisplayName("eventDetail_found_addsEventToModel: returns detail view with event in model")
    void eventDetail_found_addsEventToModel() throws Exception {
        given(eventUseCase.findEventById(5L)).willReturn(Optional.of(makeDetail(5L)));

        mockMvc.perform(get("/events/5"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/detail"))
                .andExpect(model().attributeExists("event"));
    }

    @Test
    @DisplayName("eventDetail_notFound_throwsException: throws IllegalArgumentException for unknown id")
    void eventDetail_notFound_throwsException() throws Exception {
        given(eventUseCase.findEventById(999L)).willReturn(Optional.empty());

        mockMvc.perform(get("/events/999"))
                .andExpect(status().is5xxServerError());
    }


    @Test
    @DisplayName("newEventForm_populatesModel: adds competitions, teams, and stages to model")
    void newEventForm_populatesModel() throws Exception {
        given(competitionUseCase.findAll()).willReturn(List.of(
                new Competition(1L, "champions-league", "Champions League", 2024, 1L)
        ));
        given(teamUseCase.findAll()).willReturn(List.of(
                new Team(1L, "Real Madrid", "Real Madrid FC", "real-madrid", "RMA", "ESP")
        ));
        given(stageJpaRepository.findAll()).willReturn(List.of(
                makeStageEntity("GROUP_STAGE", "Group Stage", 1),
                makeStageEntity("ROUND_OF_16", "Round of 16", 2)
        ));

        mockMvc.perform(get("/events/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/form"))
                .andExpect(model().attributeExists("competitions", "teams", "stages"));
    }


    @Test
    @DisplayName("createEvent_success_redirectsToEvents: redirects to /events with success message")
    void createEvent_success_redirectsToEvents() throws Exception {
        given(eventUseCase.createEvent(
                LocalDate.of(2024, 1, 3), "16:00", "SCHEDULED",
                1L, null, "GROUP_STAGE", null, null))
                .willReturn(new Event(42L, LocalDate.of(2024, 1, 3), LocalTime.of(16, 0),
                        "SCHEDULED", 1L, null, "GROUP_STAGE"));

        mockMvc.perform(post("/events")
                        .param("eventDate", "2024-01-03")
                        .param("eventTime", "16:00")
                        .param("status", "SCHEDULED")
                        .param("competitionId", "1")
                        .param("stageId", "GROUP_STAGE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/events"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    @DisplayName("createEvent_useCaseThrows_redirectsWithError: redirects to /events with error message on failure")
    void createEvent_useCaseThrows_redirectsWithError() throws Exception {
        given(eventUseCase.createEvent(
                LocalDate.of(2024, 1, 3), "16:00", "SCHEDULED",
                99L, null, "GROUP_STAGE", null, null))
                .willThrow(new IllegalArgumentException("Competition not found: 99"));

        mockMvc.perform(post("/events")
                        .param("eventDate", "2024-01-03")
                        .param("eventTime", "16:00")
                        .param("status", "SCHEDULED")
                        .param("competitionId", "99")
                        .param("stageId", "GROUP_STAGE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/events"))
                .andExpect(flash().attributeExists("errorMessage"));
    }
}
