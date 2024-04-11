package ua.nure.wordle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.nure.wordle.entity.User;
import ua.nure.wordle.exceptions.NotFoundException;
import ua.nure.wordle.service.interfaces.UserService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<User> findAll() {
        return userService.getAll();
    }

    @PostMapping
    public List<User> save(@RequestBody User user) {
        userService.create(user);
        return findAll();
    }


    //не працює, на скору руку робив, там шось в сервісі
    @PatchMapping("/{id}")
    public List<User> update(@PathVariable("id") Long id,
                                             @RequestBody User user) {
        User existingDepartment = userService.readById(id).
                orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        userService.update(id, user);
        return findAll();
    }

    @DeleteMapping("/{id}")
    public List<User> delete(@PathVariable Long id) {
        userService.delete(id);
        return findAll();
    }
}
