package ua.nure.wordle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.nure.wordle.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
