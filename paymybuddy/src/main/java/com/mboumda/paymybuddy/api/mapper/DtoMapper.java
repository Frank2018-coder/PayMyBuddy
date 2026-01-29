package com.mboumda.paymybuddy.api.mapper;

import com.mboumda.paymybuddy.api.dto.TransactionDto;
import com.mboumda.paymybuddy.api.dto.UserDto;
import com.mboumda.paymybuddy.entity.Transaction;
import com.mboumda.paymybuddy.entity.User;

import java.time.Instant;

public class DtoMapper {

        private DtoMapper() {
        }

        public static UserDto toUserDto(User u) {
            return new UserDto(u.getId(), u.getUsername(), u.getEmail());
        }

        public static TransactionDto toTransactionDto(Transaction t, Long currentUserId) {
            String direction = t.getSender().getId().equals(currentUserId) ? "SENT" : "RECEIVED";
            Instant createdAt = t.getCreatedAt() == null ? null : t.getCreatedAt().toInstant();

            return new TransactionDto(
                    t.getId(),
                    createdAt,
                    t.getDescription(),
                    t.getAmount(),
                    t.getSender().getId(),
                    t.getSender().getUsername(),
                    t.getReceiver().getId(),
                    t.getReceiver().getUsername(),
                    direction
            );
        }

}
