package com.mboumda.paymybuddy.api.controller;

import com.mboumda.paymybuddy.api.dto.DashboardDto;
import com.mboumda.paymybuddy.api.dto.TransactionDto;
import com.mboumda.paymybuddy.api.mapper.DtoMapper;
import com.mboumda.paymybuddy.entity.Transaction;
import com.mboumda.paymybuddy.entity.User;
import com.mboumda.paymybuddy.repository.UserRepository;
import com.mboumda.paymybuddy.service.BalanceService;
import com.mboumda.paymybuddy.service.BusinessException;
import com.mboumda.paymybuddy.service.TransferService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardRestController {

    private final TransferService transferService;
    private final BalanceService balanceService;
    private final UserRepository userRepository;

    public DashboardRestController(TransferService transferService,
                                   BalanceService balanceService,
                                   UserRepository userRepository) {
        this.transferService = transferService;
        this.balanceService = balanceService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public DashboardDto dashboard(Authentication auth,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size) {

        Long userId = currentUserId(auth);

        Page<Transaction> historyPage = transferService.getHistory(userId, PageRequest.of(page, size));
        List<TransactionDto> history = historyPage.getContent()
                .stream().map(t -> DtoMapper.toTransactionDto(t, userId)).toList();

        BigDecimal net = balanceService.computeNetBalanceFromHistory(historyPage, userId);

        return new DashboardDto(net, history);
    }

    private Long currentUserId(Authentication auth) {
        return userRepository.findByEmail(auth.getName())
                .map(User::getId)
                .orElseThrow(() -> new BusinessException("Utilisateur introuvable."));
    }
}
