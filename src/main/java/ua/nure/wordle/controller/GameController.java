package ua.nure.wordle.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.nure.wordle.dto.GameDTO;
import ua.nure.wordle.entity.Game;
import ua.nure.wordle.exceptions.NotFoundException;
import ua.nure.wordle.service.interfaces.GameService;
import ua.nure.wordle.utils.Patcher;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;
    private final ModelMapper modelMapper;
    private final Patcher<Game> patcher;

    @Autowired
    public GameController(GameService gameService, ModelMapper modelMapper, Patcher<Game> patcher) {
        this.gameService = gameService;
        this.modelMapper = modelMapper;
        this.patcher = patcher;
    }

    @GetMapping()
    public List<GameDTO> findAll() {
        return gameService.getAll().stream().map(this::convertToDTO).toList();
    }

    @PostMapping
    public List<GameDTO> save(@RequestBody GameDTO gameDTO) {
        gameService.create(convertToEntity(gameDTO));
        return findAll();
    }

    @PatchMapping("/{id}")
    public List<GameDTO> update(@PathVariable("id") Long id,
                                   @RequestBody GameDTO gameDTO) {
        Game existingGame = gameService.readById(id).
                orElseThrow(() -> new NotFoundException("Game not found with id: " + id));
        Game updatedGame = convertToEntity(gameDTO);
        try {
            patcher.patch(existingGame, updatedGame);
            gameService.update(id, existingGame);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return findAll();
    }

    @DeleteMapping("/{id}")
    public List<GameDTO> delete(@PathVariable Long id) {
        gameService.delete(id);
        return findAll();
    }

    private Game convertToEntity(GameDTO gameDTO){
        return modelMapper.map(gameDTO, Game.class);
    }

    private GameDTO convertToDTO(Game game){
        return modelMapper.map(game, GameDTO.class);
    }
}
