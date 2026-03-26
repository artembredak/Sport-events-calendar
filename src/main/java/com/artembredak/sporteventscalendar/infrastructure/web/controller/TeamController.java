package com.artembredak.sporteventscalendar.infrastructure.web.controller;


import com.artembredak.sporteventscalendar.domain.usecase.TeamUseCase;
import com.artembredak.sporteventscalendar.infrastructure.web.dto.TeamResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@Tag(name = "Teams", description = "Sports teams")
@RestController
@RequestMapping("/api/v1/teams")
public class TeamController {

    private final TeamUseCase teamUseCase;

    @Operation(summary = "List all teams")
    @ApiResponse(responseCode = "200", description = "List of teams")
    @GetMapping
    public ResponseEntity<List<TeamResponse>> list() {
        List<TeamResponse> teams = teamUseCase.findAll().stream()
                .map(t -> new TeamResponse(
                        t.id(), t.name(), t.officialName(),
                        t.slug(), t.abbreviation(), t.countryCode()))
                .toList();
        return ResponseEntity.ok(teams);
    }
}
