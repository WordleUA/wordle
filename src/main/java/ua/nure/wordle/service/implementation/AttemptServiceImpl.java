package ua.nure.wordle.service.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.nure.wordle.entity.Attempt;
import ua.nure.wordle.repository.AttemptRepository;
import ua.nure.wordle.service.interfaces.AttemptService;

import java.util.List;
import java.util.Optional;

@Service
public class AttemptServiceImpl implements AttemptService {
    private AttemptRepository attemptRepository;

    public AttemptServiceImpl(AttemptRepository attemptRepository) {
        this.attemptRepository = attemptRepository;
    }

    @Override
    public Attempt create(Attempt attempt) {
        return null;
    }

    @Override
    public Optional<Attempt> readById(long id) {
        return Optional.empty();
    }

    @Override
    public Attempt update(long id, Attempt attempt) {
        return null;
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public List<Attempt> getAll() {
        return List.of();
    }

    @Override
    public Page<Attempt> getAll(Pageable pageable) {
        return null;
    }
}
