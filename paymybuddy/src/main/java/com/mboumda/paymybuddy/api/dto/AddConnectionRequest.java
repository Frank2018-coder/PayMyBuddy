package com.mboumda.paymybuddy.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;


public record AddConnectionRequest(
        @NotNull Long receiverId,
        @NotNull @Positive BigDecimal amount,
        String description,
        @NotBlank @Email String email
) {
}
