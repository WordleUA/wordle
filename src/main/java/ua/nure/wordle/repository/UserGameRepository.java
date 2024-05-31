package ua.nure.wordle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.nure.wordle.entity.UserGame;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserGameRepository extends JpaRepository<UserGame, Long> {
    Optional<UserGame> findByUserIdAndGameId(Long userId, Long gameId);
    Optional<UserGame> findByGameIdAndUserIdNot(Long gameId, Long userId);
    List<UserGame> findByUserId(Long userId);
}