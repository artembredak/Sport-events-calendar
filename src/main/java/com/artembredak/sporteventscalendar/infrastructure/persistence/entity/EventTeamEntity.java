package com.artembredak.sporteventscalendar.infrastructure.persistence.entity;


import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "event_team")
public class EventTeamEntity {

    @EmbeddedId
    private EventTeamId id;

    @Column(name = "role", nullable = false, length = 10)
    private String role;

    public EventTeamEntity() {}

    public EventTeamEntity(Long eventId, Long teamId, String role) {
        this.id = new EventTeamId(eventId, teamId);
        this.role = role;
    }

    public EventTeamId getId() { return id; }
    public void setId(EventTeamId id) { this.id = id; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
