INSERT INTO user (username, email, password_hash, role, is_banned, game_win_count, game_lose_count, game_count,
                  coins_total)
VALUES ('user1', 'user1@gmail.com', 'password1', 'PLAYER', 0, 0, 0, 0, 0),
       ('user2', 'user2@gmail.com', 'password2', 'PLAYER', 0, 0, 0, 0, 0),
       ('user3', 'user3@gmail.com', 'password3', 'PLAYER', 0, 0, 0, 0, 0),
       ('user4', 'user4@gmail.com', 'password4', 'PLAYER', 0, 0, 0, 0, 0),
       ('user5', 'user5@gmail.com', 'password5', 'PLAYER', 0, 0, 0, 0, 0),
       ('user6', 'user6@gmail.com', 'password6', 'ADMIN', 0, 0, 0, 0, 0),
       ('user7', 'user7@gmail.com', 'password7', 'ADMIN', 0, 0, 0, 0, 0),
       ('user8', 'user8@gmail.com', 'password8', 'ADMIN', 0, 0, 0, 0, 0);

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

select * from game;
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
VALUES (1, 9, 'word1');
INSERT INTO user_game (user_id, game_id, word)
VALUES (2, 9, 'word1');

select * from game;
select * from wordle.user_game;