package com.mboumda.paymybuddy.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequest(
        @NotNull Long receiverId,
        @NotNull @Positive BigDecimal amount,
        String description
) {
}
