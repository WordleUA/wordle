package ua.nure.wordle.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ua.nure.wordle.dto.UserDTO;
import ua.nure.wordle.dto.response.AdministrationResponse;
import ua.nure.wordle.dto.response.CabinetResponse;
import ua.nure.wordle.dto.response.GeneralStatisticResponse;
import ua.nure.wordle.dto.response.LoginResponse;
import ua.nure.wordle.entity.User;
import ua.nure.wordle.entity.enums.UserRole;
import ua.nure.wordle.service.interfaces.UserService;
import ua.nure.wordle.utils.Patcher;

import java.util.List;

@CrossOrigin
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final Patcher<User> patcher;

    @GetMapping()
    public List<UserDTO> findAll() {
        return userService.getAll().stream().map(this::convertToDTO).toList();
    }

    @GetMapping("/generalStatistic")
    public List<GeneralStatisticResponse> getGeneralStatistic() {
        return userService.getGeneralStatistic();
    }


    @PatchMapping("/update")
    public ResponseEntity<UserDTO> update(@AuthenticationPrincipal User user,
                                                @RequestBody UserDTO userDTO) {
        User updatedUser = convertToEntity(userDTO);
        try {
            patcher.patch(user, updatedUser);
            userService.update(user.getId(), user);
            return ResponseEntity.ok(convertToDTO(user));
        } catch (IllegalAccessException e) {
            log.error("Error occurred while updating user with id: {}", user.getId(), e);
        }
        return ResponseEntity.ok(convertToDTO(user));
    }

    @GetMapping("/cabinet")
    public CabinetResponse getCabinet(@AuthenticationPrincipal User user) {
        return userService.getCabinet(user.getId());
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

    @GetMapping("/notSleep")
    public int notSleep() {
        return 1;
    }
}