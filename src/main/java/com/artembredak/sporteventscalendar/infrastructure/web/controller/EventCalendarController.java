package com.artembredak.sporteventscalendar.infrastructure.web.controller;

import com.artembredak.sporteventscalendar.domain.model.EventDetail;
import com.artembredak.sporteventscalendar.domain.model.Stage;
import com.artembredak.sporteventscalendar.domain.usecase.CompetitionUseCase;
import com.artembredak.sporteventscalendar.domain.usecase.EventUseCase;
import com.artembredak.sporteventscalendar.domain.usecase.SportUseCase;
import com.artembredak.sporteventscalendar.domain.usecase.TeamUseCase;
import com.artembredak.sporteventscalendar.infrastructure.persistence.repository.StageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class EventCalendarController {

    private final EventUseCase eventUseCase;
    private final SportUseCase sportUseCase;
    private final TeamUseCase teamUseCase;
    private final CompetitionUseCase competitionUseCase;
    private final StageJpaRepository stageJpaRepository;


    // -------------------------------------------------------------------------
    // Root redirect
    // -------------------------------------------------------------------------

    @GetMapping("/")
    public String root() {
        return "redirect:/events";
    }

    // -------------------------------------------------------------------------
    // Events list
    // -------------------------------------------------------------------------

    @GetMapping("/events")
    public String listEvents(
            @RequestParam(required = false) Long sportId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

        List<EventDetail> events;
        if (sportId != null) {
            events = eventUseCase.findEventsBySportId(sportId);
        } else if (date != null) {
            events = eventUseCase.findEventsByDate(date);
        } else {
            events = eventUseCase.findAllEvents();
        }

        model.addAttribute("events", events);
        model.addAttribute("sports", sportUseCase.findAll());
        model.addAttribute("selectedSportId", sportId);
        model.addAttribute("selectedDate", date);

        return "events/list";
    }

    // -------------------------------------------------------------------------
    // Event detail
    // -------------------------------------------------------------------------

    @GetMapping("/events/{id}")
    public String eventDetail(@PathVariable Long id, Model model) {
        EventDetail event = eventUseCase.findEventById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + id));
        model.addAttribute("event", event);
        return "events/detail";
    }

    // -------------------------------------------------------------------------
    // Add event form
    // -------------------------------------------------------------------------

    @GetMapping("/events/new")
    public String newEventForm(Model model) {
        model.addAttribute("competitions", competitionUseCase.findAll());
        model.addAttribute("teams", teamUseCase.findAll());

        List<Stage> stages = stageJpaRepository.findAll().stream()
                .map(e -> new Stage(e.getId(), e.getName(), e.getOrdering()))
                .sorted(Comparator.comparingInt(Stage::ordering))
                .toList();
        model.addAttribute("stages", stages);

        return "events/form";
    }

    // -------------------------------------------------------------------------
    // Create event (form POST)
    // -------------------------------------------------------------------------

    @PostMapping("/events")
    public String createEvent(
            @RequestParam String eventDate,
            @RequestParam String eventTime,
            @RequestParam String status,
            @RequestParam Long competitionId,
            @RequestParam String stageId,
            @RequestParam(required = false) Long homeTeamId,
            @RequestParam(required = false) Long awayTeamId,
            RedirectAttributes redirectAttributes) {

        try {
            eventUseCase.createEvent(
                    LocalDate.parse(eventDate),
                    eventTime,
                    status,
                    competitionId,
                    null,
                    stageId,
                    homeTeamId,
                    awayTeamId
            );
            redirectAttributes.addFlashAttribute("successMessage", "Event created successfully.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create event: " + ex.getMessage());
        }

        return "redirect:/events";
    }
}
