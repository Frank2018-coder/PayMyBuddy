package com.mboumda.paymybuddy.servive;

import com.mboumda.paymybuddy.model.ConnectionEntity;
import com.mboumda.paymybuddy.model.UserEntity;
import com.mboumda.paymybuddy.repository.ConnectionRepository;
import com.mboumda.paymybuddy.repository.UserRepository;
import com.mboumda.paymybuddy.servive.exception.NotFoundException;
import com.mboumda.paymybuddy.servive.exception.ValidationException;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ConnectionService {
    private static final Logger log = LogManager.getLogger(ConnectionService.class);

    private final UserRepository userRepository;
    private final ConnectionRepository connectionRepository;

    public ConnectionService(UserRepository userRepository, ConnectionRepository connectionRepository) {
        this.userRepository = userRepository;
        this.connectionRepository = connectionRepository;
    }

    @Transactional
    public void ajouterRelationParEmail(Long userId, String buddyEmail) {
        log.info("Ajout relation demandé: userId={} buddyEmail={}", userId, buddyEmail);

        if (buddyEmail == null || buddyEmail.isBlank()) {
            log.error("Email relation vide: userId={}", userId);
            throw new ValidationException("L'email de la relation est obligatoire.");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable."));

        UserEntity buddy = userRepository.findByEmail(buddyEmail.trim().toLowerCase())
                .orElseThrow(() -> new NotFoundException("Aucun utilisateur avec cet email."));

        log.debug("Utilisateur trouvé: id={} email={}", user.getId(), user.getEmail());
        log.debug("Buddy trouvé: id={} email={}", buddy.getId(), buddy.getEmail());

        if (user.getId().equals(buddy.getId())) {
            log.error("Auto-connexion interdite: userId={}", userId);
            throw new ValidationException("Impossible de s'ajouter soi-même.");
        }

        if (connectionRepository.existsByUserIdAndBuddyId(user.getId(), buddy.getId())) {
            log.error("Relation déjà existante: userId={} buddyId={}", user.getId(), buddy.getId());
            throw new ValidationException("Cette relation existe déjà.");
        }

        ConnectionEntity c = new ConnectionEntity();
        c.setUserId(user.getId());
        c.setBuddyId(buddy.getId());

        connectionRepository.save(c);

        log.info("Relation ajoutée avec succès: userId={} buddyId={}", user.getId(), buddy.getId());
    }
}
