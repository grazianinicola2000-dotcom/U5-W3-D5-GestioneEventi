package nicolagraziani.U5_W3_D5_GestioneEventi.services;

import nicolagraziani.U5_W3_D5_GestioneEventi.entities.User;
import nicolagraziani.U5_W3_D5_GestioneEventi.exceptions.NotFoundException;
import nicolagraziani.U5_W3_D5_GestioneEventi.exceptions.UnauthorizedException;
import nicolagraziani.U5_W3_D5_GestioneEventi.payloads.LoginDTO;
import nicolagraziani.U5_W3_D5_GestioneEventi.security.TokenTools;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final TokenTools tokenTools;
    private final UserService userService;
    private final PasswordEncoder bcrypt;

    public AuthService(TokenTools tokenTools, UserService userService, PasswordEncoder bcrypt) {
        this.tokenTools = tokenTools;
        this.userService = userService;
        this.bcrypt = bcrypt;
    }

    public String checkCredentialAndGenerateToken(LoginDTO body) {

        try {
            User found = this.userService.findByEmail(body.email());

            if (this.bcrypt.matches(body.password(), found.getPassword())) {
                return this.tokenTools.generateToken(found);
            } else {
                throw new UnauthorizedException("Credenziali Errate, riprova");
            }
        } catch (NotFoundException ex) {
            throw new UnauthorizedException("Credenziali Errate, riprovare");
        }
    }
}
