package com.mboumda.paymybuddy.api.controller;

import com.mboumda.paymybuddy.api.dto.TransactionDto;
import com.mboumda.paymybuddy.api.dto.TransferRequest;
import com.mboumda.paymybuddy.api.mapper.DtoMapper;
import com.mboumda.paymybuddy.entity.Transaction;
import com.mboumda.paymybuddy.entity.User;
import com.mboumda.paymybuddy.repository.UserRepository;
import com.mboumda.paymybuddy.service.BusinessException;
import com.mboumda.paymybuddy.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/transactions")
public class TransactionRestController {

    private final TransferService transferService;
    private final UserRepository userRepository;

    public TransactionRestController(TransferService transferService, UserRepository userRepository) {
        this.transferService = transferService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public Page<TransactionDto> history(Authentication auth,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        Long userId = currentUserId(auth);
        Page<Transaction> history = transferService.getHistory(userId, PageRequest.of(page, size));
        return history.map(t -> DtoMapper.toTransactionDto(t, userId));
    }

//    @PostMapping
//    public TransactionDto transfer(Authentication auth, @Valid @RequestBody TransferRequest req) {
//        Long senderId = currentUserId(auth);
//        Transaction tx = transferService.transfer(senderId, req.receiverId(), req.amount(), req.description());
//        return DtoMapper.toTransactionDto(tx, senderId);
//    }
//






    private Long currentUserId(Authentication auth) {
        return userRepository.findByEmail(auth.getName())
                .map(User::getId)
                .orElseThrow(() -> new BusinessException("Utilisateur introuvable."));
    }
}
