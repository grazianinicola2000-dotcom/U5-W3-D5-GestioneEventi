package nicolagraziani.U5_W3_D5_GestioneEventi.services;

import lombok.extern.slf4j.Slf4j;
import nicolagraziani.U5_W3_D5_GestioneEventi.entities.User;
import nicolagraziani.U5_W3_D5_GestioneEventi.enums.Role;
import nicolagraziani.U5_W3_D5_GestioneEventi.exceptions.BadRequestException;
import nicolagraziani.U5_W3_D5_GestioneEventi.exceptions.NotFoundException;
import nicolagraziani.U5_W3_D5_GestioneEventi.payloads.UserDTO;
import nicolagraziani.U5_W3_D5_GestioneEventi.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UserService {

    private final PasswordEncoder bcrypt;
    private final UserRepository userRepository;


    public UserService(PasswordEncoder bcrypt, UserRepository userRepository) {
        this.bcrypt = bcrypt;
        this.userRepository = userRepository;
    }

    public User saveUser(UserDTO body) {
        if (this.userRepository.existsByEmail(body.email())) {
            throw new BadRequestException("L'indirizzo mail " + body.email() + " è già in uso!");
        }
        if (this.userRepository.existsByUsername(body.username())) {
            throw new BadRequestException("L'username " + body.username() + " è già in uso!");
        }
        Role role = Role.USER;
        if (body.role().equalsIgnoreCase("organizzatore")) {
            role = Role.EVENT_CREATOR;
        }
        if (body.role().equalsIgnoreCase("admin")) {
            role = Role.ADMIN;
        }
        User newUser = new User(body.username(), body.name(), body.surname(), body.email(), this.bcrypt.encode(body.password()), role);
        this.userRepository.save(newUser);
        log.info("Il dipendente {} {} è stato registrato correttamente", body.surname(), body.name());
        return newUser;
    }

    public Page<User> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.userRepository.findAll(pageable);
    }

    public User findUserById(UUID userId) {
        return this.userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
    }

    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(email));
    }

    public void findUserByIdAndDelete(UUID userId) {
        User found = this.findUserById(userId);
        this.userRepository.delete(found);
        log.info("L'utente {} {} è stato eliminato correttamente", found.getSurname(), found.getName());
    }
}
