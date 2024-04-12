package ua.nure.wordle.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.nure.wordle.dto.UserDTO;
import ua.nure.wordle.entity.User;
import ua.nure.wordle.exceptions.NotFoundException;
import ua.nure.wordle.service.interfaces.UserService;
import ua.nure.wordle.utils.Patcher;

import java.util.List;

@CrossOrigin
@RestController
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
    public List<UserDTO> save(@RequestBody UserDTO userDTO) {
        userService.create(convertToEntity(userDTO));
        return findAll();
    }

    @PatchMapping("/{id}")
    public List<UserDTO> update(@PathVariable("id") Long id,
                                             @RequestBody UserDTO userDTO) {
        User existingUser = userService.readById(id).
                orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        User updatedUser = convertToEntity(userDTO);
        try {
            patcher.patch(existingUser, updatedUser);
            userService.update(id, existingUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return findAll();
    }

    @DeleteMapping("/{id}")
    public List<UserDTO> delete(@PathVariable Long id) {
        userService.delete(id);
        return findAll();
    }

    private User convertToEntity(UserDTO userDTO){
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO convertToDTO(User user){
        return modelMapper.map(user, UserDTO.class);
    }
}
