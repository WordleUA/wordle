package ua.nure.wordle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.nure.wordle.entity.Game;
import ua.nure.wordle.entity.enums.GameStatus;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findAllByGameStatus(GameStatus gameStatus);
}
