package com.artembredak.sporteventscalendar.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "event")
@Getter
@Setter
@NoArgsConstructor
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column(name = "event_time", nullable = false)
    private LocalTime eventTime;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "_competition_id", nullable = false)
    private Long competitionId;

    @Column(name = "_venue_id")
    private Long venueId;

    @Column(name = "_stage_id", nullable = false, length = 50)
    private String stageId;


}
