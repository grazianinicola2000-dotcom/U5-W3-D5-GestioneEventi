package nicolagraziani.U5_W3_D5_GestioneEventi.controllers;

import nicolagraziani.U5_W3_D5_GestioneEventi.entities.User;
import nicolagraziani.U5_W3_D5_GestioneEventi.exceptions.ValidationException;
import nicolagraziani.U5_W3_D5_GestioneEventi.payloads.LoginDTO;
import nicolagraziani.U5_W3_D5_GestioneEventi.payloads.LoginResponseDTO;
import nicolagraziani.U5_W3_D5_GestioneEventi.payloads.UserDTO;
import nicolagraziani.U5_W3_D5_GestioneEventi.services.AuthService;
import nicolagraziani.U5_W3_D5_GestioneEventi.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO body) {
        return new LoginResponseDTO(this.authService.checkCredentialAndGenerateToken(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User saveUser(@RequestBody @Validated UserDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            List<String> errors = validation.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }

        return this.userService.saveUser(body);
    }
}
