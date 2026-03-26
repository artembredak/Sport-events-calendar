package com.artembredak.sporteventscalendar.infrastructure.persistence.mapper;

import com.artembredak.sporteventscalendar.domain.model.EventDetail;
import com.artembredak.sporteventscalendar.domain.model.TeamInfo;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class EventDetailRowMapper {

    public EventDetail map(Object[] row) {
        Long id              = toLong(row[0]);
        LocalDate eventDate  = toLocalDate(row[1]);
        LocalTime eventTime  = toLocalTime(row[2]);
        String status        = (String) row[3];
        String compName      = (String) row[4];
        String compSlug      = (String) row[5];
        int season           = toInt(row[6]);
        String sportName     = (String) row[7];
        String stageName     = (String) row[8];
        int stageOrdering    = toInt(row[9]);
        String venueName     = (String) row[10];
        String venueCity     = (String) row[11];

        TeamInfo homeTeam = null;
        if (row[12] != null) {
            homeTeam = new TeamInfo(
                    toLong(row[12]),
                    (String) row[13],
                    (String) row[14],
                    (String) row[15]
            );
        }

        TeamInfo awayTeam = null;
        if (row[16] != null) {
            awayTeam = new TeamInfo(
                    toLong(row[16]),
                    (String) row[17],
                    (String) row[18],
                    (String) row[19]
            );
        }

        Integer homeGoals = (Integer) row[20];
        Integer awayGoals = (Integer) row[21];
        String winner     = (String) row[22];

        return new EventDetail(
                id, eventDate, eventTime, status,
                compName, compSlug, season,
                sportName,
                stageName, stageOrdering,
                venueName, venueCity,
                homeTeam, awayTeam,
                homeGoals, awayGoals, winner
        );
    }

    public List<EventDetail> mapAll(List<Object[]> rows) {
        return rows.stream().map(this::map).toList();
    }

    private Long toLong(Object o) {
        if (o == null) return null;
        if (o instanceof Long l) return l;
        if (o instanceof BigInteger bi) return bi.longValue();
        if (o instanceof Number n) return n.longValue();
        return Long.parseLong(o.toString());
    }

    private int toInt(Object o) {
        if (o instanceof Integer i) return i;
        if (o instanceof Number n) return n.intValue();
        return Integer.parseInt(o.toString());
    }

    private LocalDate toLocalDate(Object o) {
        if (o instanceof LocalDate ld) return ld;
        if (o instanceof Date d) return d.toLocalDate();
        return LocalDate.parse(o.toString());
    }

    private LocalTime toLocalTime(Object o) {
        if (o instanceof LocalTime lt) return lt;
        if (o instanceof Time t) return t.toLocalTime();
        return LocalTime.parse(o.toString());
    }
}