package ua.nure.wordle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.nure.wordle.dto.UserGameDTO;
import ua.nure.wordle.entity.UserGame;

import java.util.List;

@Repository
public interface UserGameRepository extends JpaRepository<UserGame, Long> {
    UserGame findByUserIdAndGameId(Long userId, Long gameId);
    UserGame findByGameIdAndUserIdNot(Long gameId, Long userId);
    List<UserGame> findByUserId(Long userId);

    @Query("SELECT new ua.nure.wordle.dto.UserGameDTO(ugOpp.word, g.endedAt, ug.attempts, ug.playerStatus) " +
            "FROM UserGame ug " +
            "JOIN ug.game g " +
            "JOIN UserGame ugOpp ON ug.game.id = ugOpp.game.id AND ug.user.id <> ugOpp.user.id " +
            "WHERE ug.user.id = :userId " +
            "AND g.gameStatus = ua.nure.wordle.entity.enums.GameStatus.COMPLETE " +
            "ORDER BY g.endedAt DESC")
    List<UserGameDTO> findByUserIdWithOpponentWord(Long userId);
}