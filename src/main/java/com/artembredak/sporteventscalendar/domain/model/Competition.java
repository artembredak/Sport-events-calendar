package com.artembredak.sporteventscalendar.domain.model;

public record Competition(Long id, String slug, String name, int season, Long sportId) {}

