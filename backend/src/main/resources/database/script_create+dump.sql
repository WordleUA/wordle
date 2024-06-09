-- Create the schema if it does not exist
CREATE SCHEMA IF NOT EXISTS wordle_uzmi;

-- Set search path to the schema wordle_uzmi
SET search_path TO wordle_uzmi;

-- Table wordle_uzmi.game
CREATE TABLE IF NOT EXISTS game
(
    id          SERIAL PRIMARY KEY,
    game_status VARCHAR(12) NOT NULL DEFAULT 'SEARCH' CHECK (game_status IN ('COMPLETE', 'SEARCH', 'IN_PROGRESS', 'CANCELED')),
    created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    started_at  TIMESTAMP   NULL     DEFAULT NULL,
    ended_at    TIMESTAMP   NULL     DEFAULT NULL
);

-- Table wordle_uzmi.user
CREATE TABLE IF NOT EXISTS "user"
(
    id              SERIAL PRIMARY KEY,
    login           VARCHAR(45)  NOT NULL,
    email           VARCHAR(255) NOT NULL,
    password_hash   VARCHAR(255) NOT NULL,
    role            VARCHAR(6)   NOT NULL DEFAULT 'PLAYER' CHECK (role IN ('ADMIN', 'PLAYER')),
    is_banned       BOOLEAN      NOT NULL DEFAULT FALSE,
    game_win_count  INT          NOT NULL DEFAULT 0,
    game_lose_count INT          NOT NULL DEFAULT 0,
    game_count      INT          NOT NULL DEFAULT 0,
    coins_total     INT          NOT NULL DEFAULT 0,
    is_enabled      BOOLEAN      NOT NULL DEFAULT FALSE,
    confirmation_code VARCHAR(64) NULL DEFAULT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS users_confirmation_code_uindex ON "user" (confirmation_code);

-- Table wordle_uzmi.user_game
CREATE TABLE IF NOT EXISTS user_game
(
    user_id       INT        NOT NULL,
    game_id       INT        NOT NULL,
    player_status VARCHAR(4) NULL DEFAULT NULL CHECK (player_status IN ('WIN', 'LOSE', 'DRAW')),
    word          VARCHAR(6) NULL DEFAULT NULL,
    attempts      INT        NULL,
    PRIMARY KEY (user_id, game_id),
    FOREIGN KEY (game_id) REFERENCES game (id),
    FOREIGN KEY (user_id) REFERENCES "user" (id)
);

-- Create indexes for foreign keys
CREATE INDEX IF NOT EXISTS idx_user_game_game_id ON user_game (game_id);
CREATE INDEX IF NOT EXISTS idx_user_game_user_id ON user_game (user_id);


-- Insert data into user table
INSERT INTO "user" (login, email, password_hash, role, is_banned, game_win_count, game_lose_count, game_count,
                    coins_total)
VALUES ('user1', 'user1@gmail.com', 'password1', 'PLAYER', FALSE, 0, 0, 0, 0),
       ('user2', 'user2@gmail.com', 'password2', 'PLAYER', FALSE, 0, 0, 0, 0),
       ('user3', 'user3@gmail.com', 'password3', 'PLAYER', FALSE, 0, 0, 0, 0),
       ('user4', 'user4@gmail.com', 'password4', 'PLAYER', FALSE, 0, 0, 0, 0),
       ('user5', 'user5@gmail.com', 'password5', 'PLAYER', FALSE, 0, 0, 0, 0),
       ('user6', 'user6@gmail.com', 'password6', 'ADMIN', FALSE, 0, 0, 0, 0),
       ('user7', 'user7@gmail.com', 'password7', 'ADMIN', FALSE, 0, 0, 0, 0),
       ('user8', 'user8@gmail.com', 'password8', 'ADMIN', FALSE, 0, 0, 0, 0);

-- Insert data into game table
INSERT INTO game (game_status, started_at, ended_at)
VALUES ('COMPLETE', '2021-01-01 00:00:00', '2021-01-01 00:00:00'),
       ('COMPLETE', '2021-01-01 00:00:00', '2021-01-01 00:00:00'),
       ('IN_PROGRESS', '2021-01-01 00:00:00', '2021-01-01 00:00:00'),
       ('CANCELED', '2021-01-01 00:00:00', '2021-01-01 00:00:00'),
       ('COMPLETE', '2021-01-01 00:00:00', '2021-01-01 00:00:00'),
       ('COMPLETE', '2021-01-01 00:00:00', '2021-01-01 00:00:00'),
       ('IN_PROGRESS', '2021-01-01 00:00:00', '2021-01-01 00:00:00'),
       ('IN_PROGRESS', '2021-01-01 00:00:00', '2021-01-01 00:00:00'),
       ('IN_PROGRESS', '2021-01-01 00:00:00', '2021-01-01 00:00:00'),
       ('IN_PROGRESS', '2021-01-01 00:00:00', '2021-01-01 00:00:00'),
       ('CANCELED', '2021-01-01 00:00:00', '2021-01-01 00:00:00');

-- Select all from game to verify insertions
SELECT *
FROM game;

-- Insert data into user_game table
INSERT INTO user_game (user_id, game_id, player_status, word, attempts)
VALUES (1, 1, 'WIN', 'word1', 1),
       (2, 2, 'LOSE', 'word2', 3),
       (3, 3, 'DRAW', 'word3', 6),
       (4, 4, 'WIN', 'word4', 1),
       (5, 5, 'LOSE', 'word5', 3),
       (6, 6, 'DRAW', 'word6', 5),
       (7, 7, 'WIN', 'word7', 1),
       (8, 8, 'LOSE', 'word8', 3);

INSERT INTO user_game (user_id, game_id, word)
VALUES (1, 9, 'word1'),
       (2, 9, 'word1');

-- Select data to verify insertions
SELECT *
FROM game;
SELECT *
FROM user_game;
SELECT *
FROM "user";
