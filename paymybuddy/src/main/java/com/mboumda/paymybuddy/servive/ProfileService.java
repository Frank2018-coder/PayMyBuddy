package com.mboumda.paymybuddy.servive;

import com.mboumda.paymybuddy.model.UserEntity;
import com.mboumda.paymybuddy.repository.UserRepository;
import com.mboumda.paymybuddy.servive.exception.NotFoundException;
import com.mboumda.paymybuddy.servive.exception.ValidationException;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    private static final Logger log = LogManager.getLogger(ProfileService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void changerNomUtilisateur(Long userId, String newUsername) {
        log.info("Changement username demandé: userId={}", userId);

        if (newUsername == null || newUsername.isBlank()) {
            log.error("Username vide: userId={}", userId);
            throw new ValidationException("Le nom d'utilisateur est obligatoire.");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable."));

        user.setUsername(newUsername.trim());
        userRepository.save(user);

        log.info("Username modifié: userId={} username={}", userId, user.getUsername());
    }

    @Transactional
    public void changerMotDePasse(Long userId, String newPassword) {
        log.info("Changement mot de passe demandé: userId={}", userId);

        if (newPassword == null || newPassword.isBlank() || newPassword.length() < 8) {
            log.error("Mot de passe invalide: userId={}", userId);
            throw new ValidationException("Le mot de passe doit contenir au moins 8 caractères.");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable."));

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        log.info("Mot de passe modifié: userId={}", userId);
    }
}
