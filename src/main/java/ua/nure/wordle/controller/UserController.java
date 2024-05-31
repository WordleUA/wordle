package ua.nure.wordle.controller;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.nure.wordle.dto.UserDTO;
import ua.nure.wordle.entity.User;
import ua.nure.wordle.entity.enums.UserRole;
import ua.nure.wordle.exceptions.NotFoundException;
import ua.nure.wordle.service.interfaces.UserService;
import ua.nure.wordle.utils.Patcher;

import java.util.List;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final Patcher<User> patcher;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper, Patcher<User> patcher) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.patcher = patcher;
    }

    @GetMapping()
    public List<UserDTO> findAll() {
        return userService.getAll().stream().map(this::convertToDTO).toList();
    }

    @PostMapping
    public List<UserDTO> save(@Validated @RequestBody UserDTO userDTO) {
        userService.create(convertToEntity(userDTO));
        return findAll();
    }

    @PatchMapping("/{id}")
    public List<UserDTO> update(@Validated @PathVariable("id") Long id,
                                @RequestBody UserDTO userDTO) {
        User existingUser = userService.readById(id).
                orElseThrow(() -> new NotFoundException("User not found with id: " + id));
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

    private User convertToEntity(UserDTO userDTO) {
        return User.builder()
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .passwordHash(userDTO.getPasswordHash())
                .role(String.valueOf(UserRole.PLAYER))
                .isBanned(false)
                .gameWinCount(0)
                .gameLoseCount(0)
                .gameCount(0)
                .coinsTotal(0)
                .build();

    }

    private UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
