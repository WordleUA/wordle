package ua.nure.wordle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.nure.wordle.entity.UserGame;

@Repository
public interface UserGameRepository extends JpaRepository<UserGame, Long> {
}
