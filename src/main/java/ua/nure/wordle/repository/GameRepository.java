package ua.nure.wordle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.nure.wordle.entity.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
}
