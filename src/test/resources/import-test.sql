-- Test data for leagues
INSERT INTO league (id, name, country) VALUES (1, 'Test League', 'Test Country');

-- Test data for teams
INSERT INTO team (id, name, league_id) VALUES (1, 'Test Team', 1);

-- Test data for venues
INSERT INTO venue (id, name, capacity) VALUES (1, 'Test Venue', 1000);

-- Add more test data as needed
