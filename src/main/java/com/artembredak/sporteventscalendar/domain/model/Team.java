package com.artembredak.sporteventscalendar.domain.model;

public record Team(
        Long id,
        String name,
        String officialName,
        String slug,
        String abbreviation,
        String countryCode
) {}
