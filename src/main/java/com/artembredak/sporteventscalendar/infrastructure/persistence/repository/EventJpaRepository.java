package com.artembredak.sporteventscalendar.infrastructure.persistence.repository;

import com.artembredak.sporteventscalendar.infrastructure.persistence.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventJpaRepository extends JpaRepository<EventEntity, Long> {

    String EVENT_DETAIL_SELECT = """
            SELECT
                e.id               AS event_id,
                e.event_date       AS event_date,
                e.event_time       AS event_time,
                e.status           AS status,
                c.name             AS competition_name,
                c.slug             AS competition_slug,
                c.season           AS season,
                sp.name            AS sport_name,
                st.name            AS stage_name,
                st.ordering        AS stage_ordering,
                v.name             AS venue_name,
                v.city             AS venue_city,
                ht.id              AS home_team_id,
                ht.name            AS home_team_name,
                ht.abbreviation    AS home_team_abbreviation,
                ht.country_code    AS home_team_country_code,
                at.id              AS away_team_id,
                at.name            AS away_team_name,
                at.abbreviation    AS away_team_abbreviation,
                at.country_code    AS away_team_country_code,
                er.home_goals      AS home_goals,
                er.away_goals      AS away_goals,
                er.winner          AS winner
            FROM event e
            JOIN competition c  ON c.id = e._competition_id
            JOIN sport sp       ON sp.id = c._sport_id
            JOIN stage st       ON st.id = e._stage_id
            LEFT JOIN venue v                ON v.id  = e._venue_id
            LEFT JOIN event_team et_home     ON et_home._event_id = e.id  AND et_home.role = 'HOME'
            LEFT JOIN team ht                ON ht.id = et_home._team_id
            LEFT JOIN event_team et_away     ON et_away._event_id = e.id  AND et_away.role = 'AWAY'
            LEFT JOIN team at                ON at.id = et_away._team_id
            LEFT JOIN event_result er        ON er._event_id = e.id
            """;

    @Query(value = EVENT_DETAIL_SELECT + " ORDER BY e.event_date, e.event_time", nativeQuery = true)
    List<Object[]> findAllEventDetails();

    @Query(value = EVENT_DETAIL_SELECT
            + " WHERE sp.id = :sportId ORDER BY e.event_date, e.event_time",
            nativeQuery = true)
    List<Object[]> findEventDetailsBySportId(@Param("sportId") Long sportId);

    @Query(value = EVENT_DETAIL_SELECT
            + " WHERE e.event_date = :date ORDER BY e.event_time",
            nativeQuery = true)
    List<Object[]> findEventDetailsByDate(@Param("date") LocalDate date);

    @Query(value = EVENT_DETAIL_SELECT + " WHERE e.id = :id", nativeQuery = true)
    List<Object[]> findEventDetailById(@Param("id") Long id);
}