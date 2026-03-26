package com.artembredak.sporteventscalendar.infrastructure.web.dto;

public record TeamResponse(
        Long id,
        String name,
        String officialName,
        String slug,
        String abbreviation,
        String countryCode
) {}
