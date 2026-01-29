package com.mboumda.paymybuddy.service.impl;

import com.mboumda.paymybuddy.entity.User;
import com.mboumda.paymybuddy.entity.UserConnection;
import com.mboumda.paymybuddy.entity.UserConnectionId;
import com.mboumda.paymybuddy.repository.UserConnectionRepository;
import com.mboumda.paymybuddy.repository.UserRepository;
import com.mboumda.paymybuddy.service.BusinessException;
import com.mboumda.paymybuddy.service.ConnectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ConnectionServiceImpl implements ConnectionService {

    private final UserRepository userRepository;
    private final UserConnectionRepository connectionRepository;

    public ConnectionServiceImpl(UserRepository userRepository,
                             UserConnectionRepository connectionRepository) {
        this.userRepository = userRepository;
        this.connectionRepository = connectionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getConnections(Long userId) {
        return connectionRepository.findConnectionsOf(userId);
    }

    @Override
    public void addConnection(Long userId, String connectionEmail) {

        if (connectionEmail == null || connectionEmail.isBlank()) {
            throw new BusinessException("Email de connexion invalide.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Utilisateur introuvable."));

        User other = userRepository.findByEmail(connectionEmail.trim().toLowerCase())
                .orElseThrow(() -> new BusinessException("Aucun utilisateur avec cet email."));

        if (user.getId().equals(other.getId())) {
            throw new BusinessException("Vous ne pouvez pas vous ajouter vous-même.");
        }

        // Robustesse si la DB contient des connexions dans n'importe quel sens
        if (connectionRepository.existsAnyDirection(user.getId(), other.getId())) {
            throw new BusinessException("Cette connexion existe déjà.");
        }

        // Normalisation (min/max) -> on ne stocke qu'une direction
        long a = Math.min(user.getId(), other.getId());
        long b = Math.max(user.getId(), other.getId());

        User ua = userRepository.getReferenceById(a);
        User ub = userRepository.getReferenceById(b);

        UserConnection uc = new UserConnection();
        uc.setId(new UserConnectionId(a, b));
        uc.setUser(ua);
        uc.setConnection(ub);

        connectionRepository.save(uc);
    }


}
