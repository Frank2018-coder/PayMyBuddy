package com.mboumda.paymybuddy.api.dto;

import java.math.BigDecimal;
import java.util.List;

public record DashboardDto(
        BigDecimal netBalance,
        List<TransactionDto> history
) {
}
