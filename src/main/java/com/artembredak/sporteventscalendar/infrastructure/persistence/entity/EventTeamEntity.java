package com.artembredak.sporteventscalendar.infrastructure.persistence.entity;


import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "event_team")
@Getter
@Setter
@NoArgsConstructor
public class EventTeamEntity {

    @EmbeddedId
    private EventTeamId id;

    @Column(name = "role", nullable = false, length = 10)
    private String role;

}
