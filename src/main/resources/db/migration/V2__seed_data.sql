INSERT INTO sport (id, name) VALUES (1, 'Football');
SELECT setval(pg_get_serial_sequence('sport', 'id'), 1, true);


INSERT INTO competition (id, slug, name, season, _sport_id)
VALUES (1, 'afc-champions-league', 'AFC Champions League', 2024, 1);
SELECT setval(pg_get_serial_sequence('competition', 'id'), 1, true);


INSERT INTO stage (id, name, ordering) VALUES
                                           ('ROUND_OF_16', 'Round of 16', 4),
                                           ('FINAL',       'Final',       7);


INSERT INTO team (id, name, slug, abbreviation, country_code) VALUES
                                                                  (1, 'Al Shabab',      'al-shabab-fc',        'SHA', 'KSA'),
                                                                  (2, 'Nasaf',          'fc-nasaf-qarshi',      'NAS', 'UZB'),
                                                                  (3, 'Al Hilal',       'al-hilal-saudi-fc',    'HIL', 'KSA'),
                                                                  (4, 'Shabab Al Ahli', 'shabab-al-ahli-club',  'SAH', 'UAE'),
                                                                  (5, 'Al Duhail',      'al-duhail-sc',         'DUH', 'QAT'),
                                                                  (6, 'Al Rayyan',      'al-rayyan-sc',         'RYN', 'QAT'),
                                                                  (7, 'Al Faisaly',     'al-faisaly-fc',        'FAI', 'KSA'),
                                                                  (8, 'Foolad',         'foolad-khuzestan-fc',  'FLD', 'IRN'),
                                                                  (9, 'Urawa Reds',     'urawa-red-diamonds',   'RED', 'JPN');
SELECT setval(pg_get_serial_sequence('team', 'id'), 9, true);


INSERT INTO event (id, event_date, event_time, status, _competition_id, _venue_id, _stage_id) VALUES
                                                                                                  (1, '2024-01-03', '00:00:00', 'PLAYED',    1, NULL, 'ROUND_OF_16'),
                                                                                                  (2, '2024-01-03', '16:00:00', 'SCHEDULED', 1, NULL, 'ROUND_OF_16'),
                                                                                                  (3, '2024-01-04', '15:25:00', 'SCHEDULED', 1, NULL, 'ROUND_OF_16'),
                                                                                                  (4, '2024-01-04', '08:00:00', 'SCHEDULED', 1, NULL, 'ROUND_OF_16'),
                                                                                                  (5, '2024-01-19', '00:00:00', 'SCHEDULED', 1, NULL, 'FINAL');
SELECT setval(pg_get_serial_sequence('event', 'id'), 5, true);


INSERT INTO event_team (_event_id, _team_id, role) VALUES
                                                       -- Event 1: Al Shabab (HOME) vs Nasaf (AWAY)
                                                       (1, 1, 'HOME'),
                                                       (1, 2, 'AWAY'),
                                                       -- Event 2: Al Hilal (HOME) vs Shabab Al Ahli (AWAY)
                                                       (2, 3, 'HOME'),
                                                       (2, 4, 'AWAY'),
                                                       -- Event 3: Al Duhail (HOME) vs Al Rayyan (AWAY)
                                                       (3, 5, 'HOME'),
                                                       (3, 6, 'AWAY'),
                                                       -- Event 4: Al Faisaly (HOME) vs Foolad (AWAY)
                                                       (4, 7, 'HOME'),
                                                       (4, 8, 'AWAY'),
                                                       -- Event 5: FINAL — only Urawa Reds as AWAY; HOME team not yet determined
                                                       (5, 9, 'AWAY');


INSERT INTO event_result (_event_id, home_goals, away_goals, winner) VALUES
                                                                         (1, 1, 2, 'Nasaf'),
                                                                         (2, 0, 0, NULL),
                                                                         (3, 0, 0, NULL),
                                                                         (4, 0, 0, NULL);


