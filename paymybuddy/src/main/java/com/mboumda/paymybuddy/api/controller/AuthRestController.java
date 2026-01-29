package com.mboumda.paymybuddy.api.controller;

import com.mboumda.paymybuddy.api.dto.RegisterRequest;
import com.mboumda.paymybuddy.api.dto.UserDto;
import com.mboumda.paymybuddy.api.mapper.DtoMapper;
import com.mboumda.paymybuddy.entity.User;
import com.mboumda.paymybuddy.repository.UserRepository;
import com.mboumda.paymybuddy.service.BusinessException;
import jakarta.validation.Valid;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthRestController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public UserDto register(@Valid @RequestBody RegisterRequest req) {
        String email = req.email().trim().toLowerCase();
        String username = req.username().trim();

        if (userRepository.existsByEmail(email)) throw new BusinessException("Email déjà utilisé.");
        if (userRepository.existsByUsername(username)) throw new BusinessException("Username déjà utilisé.");

        User u = new User();
        u.setEmail(email);
        u.setUsername(username);
        u.setPasswordHash(passwordEncoder.encode(req.password()));

        return DtoMapper.toUserDto(userRepository.save(u));
    }

//    @GetMapping("/me")
//    public UserDto me(Authentication authentication) {
//        String email = authentication.getName();
//        User u = userRepository.findByEmail(email)
//                .orElseThrow(() -> new BusinessException("Utilisateur introuvable."));
//        return DtoMapper.toUserDto(u);
//    }

    @GetMapping("/me")
    public UserDto me(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {

        String email = principal.getUsername(); // = email
        User u = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Utilisateur introuvable."));

        return DtoMapper.toUserDto(u);
    }

}
