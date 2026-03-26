package com.artembredak.sporteventscalendar.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EventTeamId implements Serializable {

    @Column(name = "_event_id", nullable = false)
    private Long eventId;

    @Column(name = "_team_id", nullable = false)
    private Long teamId;

}
