package com.mboumda.paymybuddy.service;

import com.mboumda.paymybuddy.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class BalanceService {


    /**
     * on calcule un net (reçus - envoyés).
     * @param history
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public BigDecimal computeNetBalanceFromHistory(Page<Transaction> history, Long userId) {
        BigDecimal net = BigDecimal.ZERO;

        for (Transaction t : history.getContent()) {
            if (t.getReceiver().getId().equals(userId)) {
                net = net.add(t.getAmount());
            } else if (t.getSender().getId().equals(userId)) {
                net = net.subtract(t.getAmount());
            }
        }

        return net;
    }

}
