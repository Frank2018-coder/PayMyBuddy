package com.mboumda.paymybuddy.api.controller;

import com.mboumda.paymybuddy.api.dto.AddConnectionRequest;
import com.mboumda.paymybuddy.api.dto.UserDto;
import com.mboumda.paymybuddy.api.mapper.DtoMapper;
import com.mboumda.paymybuddy.entity.User;
import com.mboumda.paymybuddy.repository.UserRepository;
import com.mboumda.paymybuddy.service.BusinessException;
import com.mboumda.paymybuddy.service.ConnectionService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/connections")
public class ConnectionRestController {


    private final ConnectionService connectionService;
    private final UserRepository userRepository;

    public ConnectionRestController(ConnectionService connectionService, UserRepository userRepository) {
        this.connectionService = connectionService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<UserDto> list(Authentication auth) {
        Long userId = currentUserId(auth);
        List<User> connections = connectionService.getConnections(userId);
        return connections.stream().map(DtoMapper::toUserDto).toList();
    }

    @PostMapping
    public void add(Authentication auth, @Valid @RequestBody AddConnectionRequest req) {
        Long userId = currentUserId(auth);
        connectionService.addConnection(userId, req.email());
    }

    private Long currentUserId(Authentication auth) {
        return userRepository.findByEmail(auth.getName())
                .map(User::getId)
                .orElseThrow(() -> new BusinessException("Utilisateur introuvable."));
    }
}
