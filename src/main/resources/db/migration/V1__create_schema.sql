-- Creates all 8 tables for the sport events calendar schema (3NF normalised).
-- Table order respects FK dependencies:
-- sport -> competition -> venue -> team -> stage -> event -> event_team -> event_result

-- ------------------------------------------------------------
-- 1. sport
-- ------------------------------------------------------------
CREATE TABLE sport (
                       id   BIGSERIAL    PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,

                       CONSTRAINT uq_sport_name UNIQUE (name)
);

COMMENT ON TABLE  sport      IS 'Sports categories (e.g. Football, Basketball)';
COMMENT ON COLUMN sport.name IS 'Human-readable sport name, unique across the system';

-- ------------------------------------------------------------
-- 2. competition
-- ------------------------------------------------------------
CREATE TABLE competition (
                             id        BIGSERIAL    PRIMARY KEY,
                             slug      VARCHAR(255) NOT NULL,
                             name      VARCHAR(255) NOT NULL,
                             season    INTEGER      NOT NULL,
                             _sport_id BIGINT       NOT NULL,

                             CONSTRAINT uq_competition_slug  UNIQUE (slug),
                             CONSTRAINT fk_competition_sport FOREIGN KEY (_sport_id) REFERENCES sport (id)
);

CREATE INDEX idx_competition_sport_id ON competition (_sport_id);

COMMENT ON TABLE  competition           IS 'A competition season (e.g. AFC Champions League 2024)';
COMMENT ON COLUMN competition._sport_id IS 'FK to sport.id — underscore prefix per ADR-005';

-- ------------------------------------------------------------
-- 3. venue
-- ------------------------------------------------------------
CREATE TABLE venue (
                       id   BIGSERIAL    PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       city VARCHAR(100)
);

COMMENT ON TABLE venue IS 'Physical venues / stadiums where events take place';

-- ------------------------------------------------------------
-- 4. team
-- ------------------------------------------------------------
CREATE TABLE team (
                      id            BIGSERIAL    PRIMARY KEY,
                      name          VARCHAR(255) NOT NULL,
                      official_name VARCHAR(255),
                      slug          VARCHAR(255),
                      abbreviation  VARCHAR(10),
                      country_code  VARCHAR(10),

                      CONSTRAINT uq_team_slug UNIQUE (slug)
);

COMMENT ON TABLE  team              IS 'Competing teams / clubs';
COMMENT ON COLUMN team.slug         IS 'URL-safe unique identifier for the team';
COMMENT ON COLUMN team.abbreviation IS 'Short 2-3 letter code used in display (e.g. SHA, HIL)';
COMMENT ON COLUMN team.country_code IS 'ISO country code (e.g. KSA, UAE, JPN)';

-- ------------------------------------------------------------
-- 5. stage
-- ------------------------------------------------------------
CREATE TABLE stage (
                       id       VARCHAR(50)  PRIMARY KEY,
                       name     VARCHAR(100) NOT NULL,
                       ordering INTEGER      NOT NULL
);

COMMENT ON TABLE  stage          IS 'Competition stages (e.g. ROUND_OF_16, FINAL)';
COMMENT ON COLUMN stage.id       IS 'String PK used as a semantic identifier (e.g. ROUND_OF_16)';
COMMENT ON COLUMN stage.ordering IS 'Ascending sort order across stages within a competition';

-- ------------------------------------------------------------
-- 6. event
-- ------------------------------------------------------------
CREATE TABLE event (
                       id              BIGSERIAL   PRIMARY KEY,
                       event_date      DATE        NOT NULL,
                       event_time      TIME        NOT NULL,
                       status          VARCHAR(50) NOT NULL,
                       _competition_id BIGINT      NOT NULL,
                       _venue_id       BIGINT,
                       _stage_id       VARCHAR(50) NOT NULL,

                       CONSTRAINT fk_event_competition FOREIGN KEY (_competition_id) REFERENCES competition (id),
                       CONSTRAINT fk_event_venue       FOREIGN KEY (_venue_id)       REFERENCES venue       (id),
                       CONSTRAINT fk_event_stage       FOREIGN KEY (_stage_id)       REFERENCES stage       (id),
                       CONSTRAINT chk_event_status     CHECK (status IN ('PLAYED', 'SCHEDULED', 'CANCELLED', 'POSTPONED'))
);

CREATE INDEX idx_event_competition_id ON event (_competition_id);
CREATE INDEX idx_event_venue_id       ON event (_venue_id) WHERE _venue_id IS NOT NULL;
CREATE INDEX idx_event_stage_id       ON event (_stage_id);
CREATE INDEX idx_event_date           ON event (event_date);

COMMENT ON TABLE  event               IS 'A single match or fixture';
COMMENT ON COLUMN event._venue_id     IS 'Nullable FK to venue.id — stadium may be unknown at scheduling time';
COMMENT ON COLUMN event.status        IS 'Lifecycle status: PLAYED | SCHEDULED | CANCELLED | POSTPONED';

-- ------------------------------------------------------------
-- 7. event_team
-- ------------------------------------------------------------
CREATE TABLE event_team (
                            _event_id BIGINT      NOT NULL,
                            _team_id  BIGINT      NOT NULL,
                            role      VARCHAR(10) NOT NULL,

                            CONSTRAINT pk_event_team       PRIMARY KEY (_event_id, _team_id),
                            CONSTRAINT fk_event_team_event FOREIGN KEY (_event_id) REFERENCES event (id),
                            CONSTRAINT fk_event_team_team  FOREIGN KEY (_team_id)  REFERENCES team  (id),
                            CONSTRAINT chk_event_team_role CHECK (role IN ('HOME', 'AWAY'))
);

CREATE INDEX idx_event_team_event_id ON event_team (_event_id);
CREATE INDEX idx_event_team_team_id  ON event_team (_team_id);

COMMENT ON TABLE  event_team      IS 'Junction table linking teams to events with HOME/AWAY role';
COMMENT ON COLUMN event_team.role IS 'HOME or AWAY — one event may have no HOME team (e.g. FINAL venue TBD)';

-- ------------------------------------------------------------
-- 8. event_result
-- ------------------------------------------------------------
CREATE TABLE event_result (
                              _event_id  BIGINT       NOT NULL,
                              home_goals INTEGER,
                              away_goals INTEGER,
                              winner     VARCHAR(255),

                              CONSTRAINT pk_event_result       PRIMARY KEY (_event_id),
                              CONSTRAINT fk_event_result_event FOREIGN KEY (_event_id) REFERENCES event (id)
);

COMMENT ON TABLE  event_result            IS 'Final result for a played event — one row per event, optional';
COMMENT ON COLUMN event_result._event_id  IS 'PK and FK to event.id — 1:1 relationship';
COMMENT ON COLUMN event_result.home_goals IS 'Goals scored by the HOME team; NULL for non-played events';
COMMENT ON COLUMN event_result.away_goals IS 'Goals scored by the AWAY team; NULL for non-played events';
COMMENT ON COLUMN event_result.winner     IS 'Name of winning team or NULL for draw / unplayed events';
