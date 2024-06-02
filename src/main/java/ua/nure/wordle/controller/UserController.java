package ua.nure.wordle.controller;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.nure.wordle.dto.UserDTO;
import ua.nure.wordle.dto.response.CabinetResponse;
import ua.nure.wordle.dto.response.GeneralStatisticResponse;
import ua.nure.wordle.entity.User;
import ua.nure.wordle.entity.enums.UserRole;
import ua.nure.wordle.exception.NotFoundException;
import ua.nure.wordle.service.interfaces.UserGameService;
import ua.nure.wordle.service.interfaces.UserService;
import ua.nure.wordle.utils.Patcher;

import java.util.List;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final UserGameService userGameService;
    private final ModelMapper modelMapper;
    private final Patcher<User> patcher;

    @Autowired
    public UserController(UserService userService, UserGameService userGameService, ModelMapper modelMapper, Patcher<User> patcher) {
        this.userService = userService;
        this.userGameService = userGameService;
        this.modelMapper = modelMapper;
        this.patcher = patcher;
    }

    @GetMapping()
    public List<UserDTO> findAll() {
        return userService.getAll().stream().map(this::convertToDTO).toList();
    }

    @GetMapping("/generalStatistic")
    public List<GeneralStatisticResponse> getGeneralStatistic() {
        return userService.getGeneralStatistic();
    }

    @GetMapping("/notSleep")
    public int notSleep() {
        return 1;
    }

    @PatchMapping("/{id}")
    public List<UserDTO> update(@Validated @PathVariable("id") Long id,
                                @RequestBody UserDTO userDTO) {
        User existingUser = userService.readById(id).
                orElseThrow(() -> new NotFoundException("User not found with id: " + id + ". Please, write valid user."));
        User updatedUser = convertToEntity(userDTO);

        try {
            patcher.patch(existingUser, updatedUser);
            userService.update(id, existingUser);
        } catch (IllegalAccessException e) {
            log.error("Error occurred while updating user with id: {}", id, e);
        }
        return findAll();
    }

    @DeleteMapping("/{id}")
    public List<UserDTO> delete(@PathVariable Long id) {
        userService.delete(id);
        return findAll();
    }

    @GetMapping("/cabinet/{id}")
    public CabinetResponse getCabinet(@PathVariable Long id) {
        return userService.getCabinet(id);
    }

    @PatchMapping("/role/{id}")
    public UserDTO changeRole(@PathVariable("id") Long id, @RequestBody UserDTO userDTO) {
        User existingUser = userService.readById(id).
                orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        existingUser.setRole(userDTO.getRole().getRole());
        return convertToDTO(userService.update(id, existingUser));
    }

    @PatchMapping("/block/{id}")
    public UserDTO blockUser(@PathVariable("id") Long id) {
        User existingUser = userService.readById(id).
                orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        return convertToDTO(userService.blockUser(existingUser));
    }


    private User convertToEntity(UserDTO userDTO) {
        return User.builder()
                .login(userDTO.getLogin())
                .email(userDTO.getEmail())
                .passwordHash(userDTO.getPasswordHash())
                .role(String.valueOf(UserRole.PLAYER))
                .isBanned(false)
                .gameWinCount(0L)
                .gameLoseCount(0L)
                .gameCount(0L)
                .coinsTotal(0L)
                .build();
    }

    private UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
