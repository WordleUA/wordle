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
import ua.nure.wordle.dto.UserDTO;
import ua.nure.wordle.dto.response.AdministrationResponse;
import ua.nure.wordle.dto.response.CabinetResponse;
import ua.nure.wordle.dto.response.GeneralRatingResponse;
import ua.nure.wordle.entity.User;
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

    @GetMapping("/getGeneralRating")
    public List<GeneralRatingResponse> getRating() {
        return userService.getGeneralRating();
    }


    @PatchMapping("/update")
    public ResponseEntity<?> update(@AuthenticationPrincipal User currentUser,
                                    @RequestBody @Valid UserDTO userDTO) {

        User updatedUser = convertToEntity(userDTO);
        try {
            User existingUser = userService.readById(userDTO.getId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userDTO.getId()));

            if (!existingUser.getLogin().equals(updatedUser.getLogin())) {
                if (userService.getByEmail(updatedUser.getLogin()) != null) {
                    return ResponseEntity.badRequest().body("Login '" + updatedUser.getLogin() + "' is already in use.");
                }
            }

            patcher.patch(currentUser, updatedUser);
            userService.update(currentUser.getId(), updatedUser);
            return ResponseEntity.ok(convertToDTO(updatedUser));

        } catch (IllegalAccessException e) {
            log.error("Error occurred while updating user with id: {}", currentUser.getId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (EntityNotFoundException e) {
            log.error("User not found with id: {}", currentUser.getId(), e);
            return ResponseEntity.notFound().build();
        }
    }



    @GetMapping("/cabinet")
    public CabinetResponse getCabinet(@AuthenticationPrincipal User user) {
        return userService.getCabinet(user);
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
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    @GetMapping("/notSleep")
    public int notSleep() {
        return 1;
    }
}