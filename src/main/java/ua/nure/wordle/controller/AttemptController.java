package ua.nure.wordle.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.nure.wordle.dto.AttemptDTO;
import ua.nure.wordle.entity.Attempt;
import ua.nure.wordle.exceptions.NotFoundException;
import ua.nure.wordle.service.interfaces.AttemptService;
import ua.nure.wordle.utils.Patcher;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/attempt")
public class AttemptController {
    private final AttemptService attemptService;
    private final ModelMapper modelMapper;
    private final Patcher<Attempt> patcher;

    @Autowired
    public AttemptController(AttemptService attemptService, ModelMapper modelMapper, Patcher<Attempt> patcher) {
        this.attemptService = attemptService;
        this.modelMapper = modelMapper;
        this.patcher = patcher;
    }

    @GetMapping()
    public List<AttemptDTO> findAll() {
        return attemptService.getAll().stream().map(this::convertToDTO).toList();
    }

    @PostMapping
    public List<AttemptDTO> save(@RequestBody AttemptDTO attemptDTO) {
        attemptService.create(convertToEntity(attemptDTO));
        return findAll();
    }

    @PatchMapping("/{id}")
    public List<AttemptDTO> update(@PathVariable("id") Long id,
                                @RequestBody AttemptDTO attemptDTO) {
        Attempt existingAttempt = attemptService.readById(id).
                orElseThrow(() -> new NotFoundException("Attempt not found with id: " + id));
        Attempt updatedAttempt = convertToEntity(attemptDTO);
        try {
            patcher.patch(existingAttempt, updatedAttempt);
            attemptService.update(id, existingAttempt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return findAll();
    }

    @DeleteMapping("/{id}")
    public List<AttemptDTO> delete(@PathVariable Long id) {
        attemptService.delete(id);
        return findAll();
    }

    private Attempt convertToEntity(AttemptDTO attemptDTO){
        return modelMapper.map(attemptDTO, Attempt.class);
    }

    private AttemptDTO convertToDTO(Attempt attempt){
        return modelMapper.map(attempt, AttemptDTO.class);
    }
}
