package com.artembredak.sporteventscalendar.infrastructure.web.controller;

import com.artembredak.sporteventscalendar.domain.usecase.SportUseCase;
import com.artembredak.sporteventscalendar.infrastructure.web.dto.SportResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Sports", description = "Sport types")
@RestController
@RequestMapping("/api/v1/sports")
public class SportController {

    private final SportUseCase sportUseCase;

    public SportController(SportUseCase sportUseCase) {
        this.sportUseCase = sportUseCase;
    }

    @Operation(summary = "List all sports")
    @ApiResponse(responseCode = "200", description = "List of sports")
    @GetMapping
    public ResponseEntity<List<SportResponse>> list() {
        List<SportResponse> sports = sportUseCase.findAll().stream()
                .map(s -> new SportResponse(s.id(), s.name()))
                .toList();
        return ResponseEntity.ok(sports);
    }
}