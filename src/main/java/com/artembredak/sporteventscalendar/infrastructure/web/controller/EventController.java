package com.artembredak.sporteventscalendar.infrastructure.web.controller;

import com.artembredak.sporteventscalendar.domain.model.Event;
import com.artembredak.sporteventscalendar.domain.model.EventDetail;
import com.artembredak.sporteventscalendar.domain.model.TeamInfo;
import com.artembredak.sporteventscalendar.domain.usecase.EventUseCase;
import com.artembredak.sporteventscalendar.infrastructure.web.dto.CreateEventRequest;
import com.artembredak.sporteventscalendar.infrastructure.web.dto.EventDetailResponse;
import com.artembredak.sporteventscalendar.infrastructure.web.dto.EventResponse;
import com.artembredak.sporteventscalendar.infrastructure.web.dto.TeamInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Tag(name = "Events", description = "Sports event management")
@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventUseCase eventUseCase;

    @Operation(summary = "List events", description = "Returns all events. Optionally filter by sport or date. Uses a single JOIN query (no N+1).")
    @ApiResponse(responseCode = "200", description = "List of events")
    @GetMapping
    public ResponseEntity<List<EventDetailResponse>> list(
            @Parameter(description = "Filter by sport ID") @RequestParam(required = false) Long sportId,
            @Parameter(description = "Filter by date (yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<EventDetail> events;
        if (sportId != null) {
            events = eventUseCase.findEventsBySportId(sportId);
        } else if (date != null) {
            events = eventUseCase.findEventsByDate(date);
        } else {
            events = eventUseCase.findAllEvents();
        }

        return ResponseEntity.ok(events.stream().map(this::toDetailResponse).toList());
    }

    @Operation(summary = "Get event by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Event found"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EventDetailResponse> getById(@Parameter(description = "Event ID") @PathVariable Long id) {
        return eventUseCase.findEventById(id)
                .map(this::toDetailResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create event")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Event created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<EventResponse> create(@Valid @RequestBody CreateEventRequest request) {
        Event event = eventUseCase.createEvent(
                request.eventDate(),
                request.eventTime(),
                request.status(),
                request.competitionId(),
                request.venueId(),
                request.stageId(),
                request.homeTeamId(),
                request.awayTeamId()
        );

        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(event.id())
                .toUri();

        return ResponseEntity.created(location).body(toEventResponse(event));
    }

    // private mapping helpers

    private EventDetailResponse toDetailResponse(EventDetail d) {
        return new EventDetailResponse(
                d.id(),
                d.eventDate(),
                d.eventTime(),
                d.status(),
                d.competitionName(),
                d.competitionSlug(),
                d.season(),
                d.sportName(),
                d.stageName(),
                d.stageOrdering(),
                d.venueName(),
                d.venueCity(),
                toTeamInfoResponse(d.homeTeam()),
                toTeamInfoResponse(d.awayTeam()),
                d.homeGoals(),
                d.awayGoals(),
                d.winner()
        );
    }

    private TeamInfoResponse toTeamInfoResponse(TeamInfo teamInfo) {
        if (teamInfo == null) return null;
        return new TeamInfoResponse(
                teamInfo.id(),
                teamInfo.name(),
                teamInfo.abbreviation(),
                teamInfo.countryCode()
        );
    }

    private EventResponse toEventResponse(Event event) {
        return new EventResponse(
                event.id(),
                event.eventDate(),
                event.eventTime(),
                event.status(),
                event.competitionId(),
                event.venueId(),
                event.stageId()
        );
    }
}
