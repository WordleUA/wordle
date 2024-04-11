-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema wordle
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `wordle` ;

-- -----------------------------------------------------
-- Schema wordle
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `wordle` DEFAULT CHARACTER SET utf8 ;
-- -----------------------------------------------------
-- Schema wordle
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `wordle` ;

-- -----------------------------------------------------
-- Schema wordle
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `wordle` DEFAULT CHARACTER SET utf8mb3 ;
USE `wordle` ;

-- -----------------------------------------------------
-- Table `wordle`.`attempt`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wordle`.`attempt` ;

CREATE TABLE IF NOT EXISTS `wordle`.`attempt` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `attempted_word` VARCHAR(6) NOT NULL,
  `attempt_time` TIME NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

USE `wordle` ;

-- -----------------------------------------------------
-- Table `wordle`.`game`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wordle`.`game` ;

CREATE TABLE IF NOT EXISTS `wordle`.`game` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `game_status` ENUM('complited', 'search', 'in_progress', 'canceled') NOT NULL DEFAULT 'search',
  `datetime` TIMESTAMP NOT NULL,
  `start_time` TIME NULL DEFAULT NULL,
  `end_time` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `wordle`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wordle`.`user` ;

CREATE TABLE IF NOT EXISTS `wordle`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `role` ENUM('admin', 'player') NOT NULL DEFAULT 'player',
  `is_banned` TINYINT NOT NULL DEFAULT '0',
  `game_win_count` INT NOT NULL DEFAULT '0',
  `game_count` INT NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `wordle`.`user_game`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wordle`.`user_game` ;

CREATE TABLE IF NOT EXISTS `wordle`.`user_game` (
  `user_id` INT NOT NULL,
  `game_id` INT NOT NULL,
  `attempt_id` INT NOT NULL,
  `player_status` ENUM('win', 'lose', 'draw') NULL DEFAULT NULL,
  `word` VARCHAR(6) NULL DEFAULT NULL,
  `is_game_over` TINYINT NOT NULL,
  PRIMARY KEY (`user_id`, `game_id`),
  INDEX `fk_user_has_game_game1_idx` (`game_id` ASC) VISIBLE,
  INDEX `fk_user_has_game_user_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_user_game_attempt1_idx` (`attempt_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_has_game_game1`
    FOREIGN KEY (`game_id`)
    REFERENCES `wordle`.`game` (`id`),
  CONSTRAINT `fk_user_has_game_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `wordle`.`user` (`id`),
  CONSTRAINT `fk_user_game_attempt1`
    FOREIGN KEY (`attempt_id`)
    REFERENCES `wordle`.`attempt` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
