package ua.nure.wordle.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.nure.wordle.dto.UserDTO;
import ua.nure.wordle.dto.response.AdministrationResponse;
import ua.nure.wordle.dto.response.CabinetResponse;
import ua.nure.wordle.dto.response.GeneralRatingResponse;
import ua.nure.wordle.dto.response.MessageResponse;
import ua.nure.wordle.entity.User;
import ua.nure.wordle.entity.enums.UserRole;
import ua.nure.wordle.exception.ConflictException;
import ua.nure.wordle.service.implementation.UserServiceImpl;
import ua.nure.wordle.utils.Patcher;

import java.util.List;

@CrossOrigin
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserServiceImpl userService;
    private final ModelMapper modelMapper;
    private final Patcher<User> patcher;

    @GetMapping()
    public List<UserDTO> findAll() {
        return userService.getAll().stream().map(this::convertToDTO).toList();
    }

    @GetMapping("/getGeneralRating")
    public List<GeneralRatingResponse> getRating() {
        return userService.getGeneralRating();
    }

    @PatchMapping("/update")
    public ResponseEntity<UserDTO> update(@AuthenticationPrincipal User user,
                                          @RequestBody @Valid UserDTO userDTO) {

        User updatedUser = convertToEntity(userDTO);
        try {
            if (!user.getLogin().equals(updatedUser.getLogin())) {
                if (userService.getByLogin(updatedUser.getLogin()) != null) {
                    throw new ConflictException("Login '" + updatedUser.getLogin() + "' is already in use.");
                }
            }
            patcher.patch(user, updatedUser);
            userService.update(user.getId(), user);

            return ResponseEntity.ok(convertToDTO(user));

        } catch (IllegalAccessException e) {
            log.error("Error occurred while updating user with id: {}", user.getId(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error occurred while updating user. Please try again later.");
        }
    }


    @GetMapping("/cabinet")
    public CabinetResponse getCabinet(@AuthenticationPrincipal User user) {
        return userService.getCabinetInfo(user);
    }

    @GetMapping("/usersByAdmin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<AdministrationResponse> getUsersByAdmin() {
        return userService.getUsersByAdmin();
    }

    @PatchMapping("/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserDTO changeRole(@RequestBody UserDTO userDTO) {
        User existingUser = userService.readById(userDTO.getId()).
                orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userDTO.getId()));
        existingUser.setRole(userDTO.getRole().getRole());
        return convertToDTO(userService.update(userDTO.getId(), existingUser));
    }

    @PatchMapping("/block")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserDTO blockUser(@RequestBody UserDTO userDTO) {
        User existingUser = userService.readById(userDTO.getId()).
                orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userDTO.getId()));
        return convertToDTO(userService.blockUser(existingUser));
    }


    private User convertToEntity(UserDTO userDTO) {
        UserRole role = userDTO.getRole();
        return User.builder()
                .login(userDTO.getLogin())
                .email(userDTO.getEmail())
                .passwordHash(userDTO.getPasswordHash())
                .role(role != null ? role.toString() : null)
                .isBanned(userDTO.getIsBanned())
                .gameWinCount(userDTO.getGameWinCount())
                .gameLoseCount(userDTO.getGameLoseCount())
                .gameCount(userDTO.getGameCount())
                .coinsTotal(userDTO.getCoinsTotal())
                .build();
    }

    private UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    @GetMapping("/notSleep")
    public int notSleep() {
        return 1;
    }
}