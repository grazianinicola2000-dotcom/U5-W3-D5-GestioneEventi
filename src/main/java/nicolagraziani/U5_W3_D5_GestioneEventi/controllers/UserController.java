package nicolagraziani.U5_W3_D5_GestioneEventi.controllers;

import nicolagraziani.U5_W3_D5_GestioneEventi.entities.User;
import nicolagraziani.U5_W3_D5_GestioneEventi.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<User> findAll(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "20") int size,
                              @RequestParam(defaultValue = "surname") String sortBy) {
        return this.userService.findAll(page, size, sortBy);
    }

    @GetMapping("/me")
    public User getOwnProfile(@AuthenticationPrincipal User currentAuthenticatedUser) {
        return currentAuthenticatedUser;
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOwnProfile(@AuthenticationPrincipal User currentAuthenticatedUser) {
        this.userService.findUserByIdAndDelete(currentAuthenticatedUser.getUserId());
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public User findById(@PathVariable UUID userId) {
        return this.userService.findUserById(userId);
    }
}
//eyJhbGciOiJIUzM4NCJ9.eyJpYXQiOjE3NzcwMjgwNzUsImV4cCI6MTc3NzYzMjg3NSwic3ViIjoiMzY0NWExZmItODUxZi00OTc2LTkyZTItOTdiMWQ0OWYzYmZlIn0.aqK9eK5VeZDlAB-s8XCzFrMJhottuw4GvGOSzI4FyEaT074mFN3qqAEqoXiqhnnd