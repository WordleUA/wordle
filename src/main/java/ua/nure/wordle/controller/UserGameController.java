package ua.nure.wordle.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.nure.wordle.entity.User;
import ua.nure.wordle.service.interfaces.UserGameService;
import ua.nure.wordle.utils.Patcher;

@CrossOrigin
@RestController
@RequestMapping("/user-game")
public class UserGameController {
    private final UserGameService userGameService;
    private final ModelMapper modelMapper;
    private final Patcher<User> patcher;
    public UserGameController(UserGameService userGameService, ModelMapper modelMapper, Patcher<User> patcher) {
        this.userGameService = userGameService;
        this.modelMapper = modelMapper;
        this.patcher = patcher;
    }
}
