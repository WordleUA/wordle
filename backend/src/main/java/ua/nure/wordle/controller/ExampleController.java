package ua.nure.wordle.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.nure.wordle.entity.User;

@CrossOrigin
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/example")
public class ExampleController {

    @GetMapping
    public Object example() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return "Ви авторизовані: \n" +
                "ID: " + user.getId() + "\n" +
                "Логін: " + user.getLogin() + "\n" +
                "Email: " + user.getEmail() + "\n" +
                "Роль: " + user.getRole();
    }

    @GetMapping("/currentUser")
    public User fetchUser(@AuthenticationPrincipal User user) {
        return user;
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String admin() {
        return "You are admin";
    }

    @GetMapping("/player")
    @PreAuthorize("hasAuthority('PLAYER')")
    public String user() {
        return "You are player";
    }
}
