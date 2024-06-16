package ua.nure.wordle.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.nure.wordle.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    List<User> findAllByOrderByCoinsTotalDesc();
    Optional<User> findByConfirmationCode(String code);
    Boolean existsByLogin(String login);
    Optional<User> findByPasswordResetCode(String passwordResetCode);
    User findByLogin(String login);
}
