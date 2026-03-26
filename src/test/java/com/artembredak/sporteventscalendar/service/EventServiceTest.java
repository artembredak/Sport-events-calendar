package com.artembredak.sporteventscalendar.service;

import com.artembredak.sporteventscalendar.domain.model.Event;
import com.artembredak.sporteventscalendar.domain.model.EventDetail;
import com.artembredak.sporteventscalendar.domain.model.TeamInfo;
import com.artembredak.sporteventscalendar.domain.repository.EventRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class EventServiceTest {

    @Mock
    EventRepository eventRepository;

    @InjectMocks
    EventService eventService;


    private static EventDetail makeDetail(long id) {
        return new EventDetail(
                id,
                LocalDate.of(2024, 1, 3),
                LocalTime.of(16, 0),
                "scheduled",
                "AFC Champions League",
                "afc-champions-league",
                2024,
                "Football",
                "ROUND OF 16",
                4,
                null,
                null,
                new TeamInfo(1L, "Al Hilal", "HIL", "KSA"),
                new TeamInfo(2L, "Shabab Al Ahli", "SAH", "UAE"),
                null,
                null,
                null
        );
    }

    private static Event makeSavedEvent(long id) {
        return new Event(id, LocalDate.of(2024, 1, 3), LocalTime.of(16, 0),
                "scheduled", 1L, null, "ROUND_OF_16");
    }

    @Test
    @DisplayName("getAllEvents_returnsListFromRepository: delegates to findAllWithDetail and returns same list")
    void getAllEvents_returnsListFromRepository() {
        List<EventDetail> expected = List.of(makeDetail(1L), makeDetail(2L));
        given(eventRepository.findAllWithDetail()).willReturn(expected);

        List<EventDetail> result = eventService.findAllEvents();

        assertThat(result).isEqualTo(expected);
        then(eventRepository).should().findAllWithDetail();
    }

    @Test
    @DisplayName("getEventById_existingId_returnsEventDetail: returns the Optional value when present")
    void getEventById_existingId_returnsEventDetail() {
        EventDetail detail = makeDetail(5L);
        given(eventRepository.findDetailById(5L)).willReturn(Optional.of(detail));

        Optional<EventDetail> result = eventService.findEventById(5L);

        assertThat(result).isPresent().contains(detail);
        then(eventRepository).should().findDetailById(5L);
    }

    @Test
    @DisplayName("getEventById_unknownId_throwsException: service returns empty Optional for unknown id")
    void getEventById_unknownId_returnsEmpty() {
        given(eventRepository.findDetailById(999L)).willReturn(Optional.empty());

        Optional<EventDetail> result = eventService.findEventById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("createEvent_validData_persistsAndReturnsId: saves event and registers teams, returns saved event id")
    void createEvent_validData_persistsAndReturnsId() {
        given(eventRepository.competitionExists(1L)).willReturn(true);
        given(eventRepository.stageExists("ROUND_OF_16")).willReturn(true);
        given(eventRepository.teamExists(10L)).willReturn(true);
        given(eventRepository.teamExists(20L)).willReturn(true);
        Event saved = makeSavedEvent(42L);
        given(eventRepository.save(any(Event.class))).willReturn(saved);

        Event result = eventService.createEvent(
                LocalDate.of(2024, 1, 3),
                "16:00",
                "scheduled",
                1L,
                null,
                "ROUND_OF_16",
                10L,
                20L
        );

        assertThat(result.id()).isEqualTo(42L);
        then(eventRepository).should().save(any(Event.class));
        then(eventRepository).should().saveEventTeam(42L, 10L, "HOME");
        then(eventRepository).should().saveEventTeam(42L, 20L, "AWAY");
    }

    @Test
    @DisplayName("createEvent_unknownCompetitionId_throwsException: throws IllegalArgumentException containing competition id")
    void createEvent_unknownCompetitionId_throwsException() {
        given(eventRepository.competitionExists(999L)).willReturn(false);

        assertThatThrownBy(() -> eventService.createEvent(
                LocalDate.of(2024, 1, 3), "16:00", "scheduled",
                999L, null, "ROUND_OF_16", null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("999");

        then(eventRepository).should(never()).save(any());
    }

    @Test
    @DisplayName("createEvent_unknownStageId_throwsException: throws IllegalArgumentException containing stage id")
    void createEvent_unknownStageId_throwsException() {
        given(eventRepository.competitionExists(1L)).willReturn(true);
        given(eventRepository.stageExists("UNKNOWN_STAGE")).willReturn(false);

        assertThatThrownBy(() -> eventService.createEvent(
                LocalDate.of(2024, 1, 3), "16:00", "scheduled",
                1L, null, "UNKNOWN_STAGE", null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("UNKNOWN_STAGE");

        then(eventRepository).should(never()).save(any());
    }

    @Test
    @DisplayName("createEvent_unknownHomeTeamId_throwsException: throws IllegalArgumentException when homeTeamId not found")
    void createEvent_unknownHomeTeamId_throwsException() {
        given(eventRepository.competitionExists(1L)).willReturn(true);
        given(eventRepository.stageExists("ROUND_OF_16")).willReturn(true);
        given(eventRepository.teamExists(999L)).willReturn(false);

        assertThatThrownBy(() -> eventService.createEvent(
                LocalDate.of(2024, 1, 3), "16:00", "scheduled",
                1L, null, "ROUND_OF_16", 999L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("999");

        then(eventRepository).should(never()).save(any());
    }

    @Test
    @DisplayName("getAllEvents_emptyRepository_returnsEmptyList: returns empty list gracefully when no events exist")
    void getAllEvents_emptyRepository_returnsEmptyList() {
        given(eventRepository.findAllWithDetail()).willReturn(List.of());

        List<EventDetail> result = eventService.findAllEvents();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("getEventsByDate_returnsFilteredList: delegates to findAllWithDetailByDate and returns filtered list")
    void getEventsByDate_returnsFilteredList() {
        LocalDate date = LocalDate.of(2024, 1, 3);
        List<EventDetail> expected = List.of(makeDetail(1L));
        given(eventRepository.findAllWithDetailByDate(date)).willReturn(expected);

        List<EventDetail> result = eventService.findEventsByDate(date);

        assertThat(result).isEqualTo(expected);
        then(eventRepository).should().findAllWithDetailByDate(date);
    }

    @Test
    @DisplayName("getEventsBySportId_returnsFilteredList: delegates to findAllWithDetailBySportId and returns filtered list")
    void getEventsBySportId_returnsFilteredList() {
        List<EventDetail> expected = List.of(makeDetail(3L));
        given(eventRepository.findAllWithDetailBySportId(7L)).willReturn(expected);

        List<EventDetail> result = eventService.findEventsBySportId(7L);

        assertThat(result).isEqualTo(expected);
        then(eventRepository).should().findAllWithDetailBySportId(7L);
    }
}