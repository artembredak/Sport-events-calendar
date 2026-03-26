package com.artembredak.sporteventscalendar.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "team")
@Getter
@Setter
@NoArgsConstructor
public class TeamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "official_name", length = 255)
    private String officialName;

    @Column(name = "slug", unique = true, length = 255)
    private String slug;

    @Column(name = "abbreviation", length = 10)
    private String abbreviation;

    @Column(name = "country_code", length = 10)
    private String countryCode;


}