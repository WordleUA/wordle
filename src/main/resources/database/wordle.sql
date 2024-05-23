-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema wordle
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `wordle`;

-- -----------------------------------------------------
-- Schema wordle
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `wordle` DEFAULT CHARACTER SET utf8mb3;
USE `wordle`;

-- -----------------------------------------------------
-- Table `wordle`.`game`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wordle`.`game`;

CREATE TABLE IF NOT EXISTS `wordle`.`game`
(
    `id`          INT                                                    NOT NULL AUTO_INCREMENT,
    `game_status` ENUM ('COMPLETE', 'SEARCH', 'IN_PROGRESS', 'CANCELED') NOT NULL DEFAULT 'SEARCH',
    `created_at`  TIMESTAMP                                              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `started_at`  TIMESTAMP                                              NULL     DEFAULT NULL,
    `ended_at`    TIMESTAMP                                              NULL     DEFAULT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `wordle`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wordle`.`user`;

CREATE TABLE IF NOT EXISTS `wordle`.`user`
(
    `id`              INT                      NOT NULL AUTO_INCREMENT,
    `username`        VARCHAR(45)              NOT NULL,
    `email`           VARCHAR(45)              NOT NULL,
    `password_hash`   VARCHAR(45)              NOT NULL,
    `role`            ENUM ('ADMIN', 'PLAYER') NOT NULL DEFAULT 'PLAYER',
    `is_banned`       TINYINT                  NOT NULL DEFAULT '0',
    `game_win_count`  INT                      NOT NULL DEFAULT '0',
    `game_lose_count` INT                      NOT NULL DEFAULT '0',
    `game_count`      INT                      NOT NULL DEFAULT '0',
    `coins_total`     INT                      NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `wordle`.`user_game`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wordle`.`user_game`;

CREATE TABLE IF NOT EXISTS `wordle`.`user_game`
(
    `user_id`       INT                          NOT NULL,
    `game_id`       INT                          NOT NULL,
    `player_status` ENUM ('WIN', 'LOSE', 'DRAW') NULL DEFAULT NULL,
    `word`          VARCHAR(6)                   NULL DEFAULT NULL,
    `is_game_over`  TINYINT                      NOT NULL,
    `attempts`      INT                          NULL,
    PRIMARY KEY (`user_id`, `game_id`),
    INDEX `fk_user_has_game_game1_idx` (`game_id` ASC) VISIBLE,
    INDEX `fk_user_has_game_user_idx` (`user_id` ASC) VISIBLE,
    CONSTRAINT `fk_user_has_game_game1`
        FOREIGN KEY (`game_id`)
            REFERENCES `wordle`.`game` (`id`),
    CONSTRAINT `fk_user_has_game_user`
        FOREIGN KEY (`user_id`)
            REFERENCES `wordle`.`user` (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;
