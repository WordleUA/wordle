package ua.nure.wordle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.nure.wordle.entity.Attempt;

@Repository
public interface AttemptRepository extends JpaRepository<Attempt, Integer> {
}
