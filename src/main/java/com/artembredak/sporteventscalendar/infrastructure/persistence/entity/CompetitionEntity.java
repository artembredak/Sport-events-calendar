package com.artembredak.sporteventscalendar.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "competition")
public class CompetitionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "slug", nullable = false, unique = true, length = 255)
    private String slug;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "season", nullable = false)
    private int season;

    @Column(name = "_sport_id", nullable = false)
    private Long sportId;

    public CompetitionEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getSeason() { return season; }
    public void setSeason(int season) { this.season = season; }

    public Long getSportId() { return sportId; }
    public void setSportId(Long sportId) { this.sportId = sportId; }
}
