package com.artembredak.sporteventscalendar.infrastructure.persistence.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "event_result")
@Getter
@Setter
@NoArgsConstructor
public class EventResultEntity {

    @Id
    @Column(name = "_event_id", nullable = false)
    private Long eventId;

    @Column(name = "home_goals")
    private Integer homeGoals;

    @Column(name = "away_goals")
    private Integer awayGoals;

    @Column(name = "winner", length = 255)
    private String winner;


}
