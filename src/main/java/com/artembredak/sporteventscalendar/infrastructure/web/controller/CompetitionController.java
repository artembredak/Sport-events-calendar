package com.artembredak.sporteventscalendar.infrastructure.web.controller;

import com.artembredak.sporteventscalendar.domain.usecase.CompetitionUseCase;
import com.artembredak.sporteventscalendar.infrastructure.web.dto.CompetitionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Competitions", description = "Sports competitions")
@RestController
@RequestMapping("/api/v1/competitions")
public class CompetitionController {

    private final CompetitionUseCase competitionUseCase;

    public CompetitionController(CompetitionUseCase competitionUseCase) {
        this.competitionUseCase = competitionUseCase;
    }

    @Operation(summary = "List all competitions")
    @ApiResponse(responseCode = "200", description = "List of competitions")
    @GetMapping
    public ResponseEntity<List<CompetitionResponse>> list() {
        List<CompetitionResponse> competitions = competitionUseCase.findAll().stream()
                .map(c -> new CompetitionResponse(c.id(), c.slug(), c.name(), c.season(), c.sportId()))
                .toList();
        return ResponseEntity.ok(competitions);
    }
}
