package com.artembredak.sporteventscalendar.infrastructure.web.dto;

public record CompetitionResponse(Long id, String slug, String name, int season, Long sportId) {}

