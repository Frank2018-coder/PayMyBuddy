package com.mboumda.paymybuddy.service.impl;

import com.mboumda.paymybuddy.entity.Transaction;
import com.mboumda.paymybuddy.entity.User;
import com.mboumda.paymybuddy.repository.TransactionRepository;
import com.mboumda.paymybuddy.repository.UserConnectionRepository;
import com.mboumda.paymybuddy.repository.UserRepository;
import com.mboumda.paymybuddy.service.BusinessException;
import com.mboumda.paymybuddy.service.TransferService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class TransferServiceImpl implements TransferService {

    private final UserRepository userRepository;
    private final UserConnectionRepository connectionRepository;
    private final TransactionRepository transactionRepository;

    public TransferServiceImpl(UserRepository userRepository,
                               UserConnectionRepository connectionRepository,
                               TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.connectionRepository = connectionRepository;
        this.transactionRepository = transactionRepository;
    }



    @Override
    public Transaction transfer(Long senderId, Long receiverId, String description, BigDecimal amount) {
        if (senderId == null || receiverId == null) {
            throw new BusinessException("Expéditeur/Destinataire invalide.");
        }
        if (senderId.equals(receiverId)) {
            throw new BusinessException("Vous ne pouvez pas vous transférer de l'argent à vous-même.");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Le montant doit être supérieur à 0.");
        }

        // Vérifie que les users existent (messages plus propres)
        if (!userRepository.existsById(senderId)) {
            throw new BusinessException("Expéditeur introuvable.");
        }
        if (!userRepository.existsById(receiverId)) {
            throw new BusinessException("Destinataire introuvable.");
        }

        // Règle métier: receiver doit être une connexion
        if (!connectionRepository.existsAnyDirection(senderId, receiverId)) {
            throw new BusinessException("Le destinataire doit faire partie de vos connexions.");
        }

        User senderRef = userRepository.getReferenceById(senderId);
        User receiverRef = userRepository.getReferenceById(receiverId);

        Transaction tx = new Transaction();
        tx.setSender(senderRef);
        tx.setReceiver(receiverRef);
        tx.setAmount(amount);
        tx.setDescription((description == null || description.isBlank()) ? null : description.trim());

        return transactionRepository.save(tx);
    }

    @Override
    public Page<Transaction> getHistory(Long userId, Pageable pageable) {
        return transactionRepository.findBySenderIdOrReceiverIdOrderByCreatedAtDescIdDesc(userId, userId, pageable);
    }
}
