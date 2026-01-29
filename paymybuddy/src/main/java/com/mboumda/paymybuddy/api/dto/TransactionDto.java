package com.mboumda.paymybuddy.api.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionDto(
        Long id,
        Instant createdAt,
        String description,
        BigDecimal amount,
        Long senderId,
        String senderUsername,
        Long receiverId,
        String receiverUsername,
        String direction // SENT/RECEIVED
) {
}
