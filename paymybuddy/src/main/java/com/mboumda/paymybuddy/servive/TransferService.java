package com.mboumda.paymybuddy.servive;

import com.mboumda.paymybuddy.model.TransactionEntity;
import com.mboumda.paymybuddy.model.UserEntity;
import com.mboumda.paymybuddy.repository.TransactionRepository;
import com.mboumda.paymybuddy.repository.UserRepository;
import com.mboumda.paymybuddy.servive.exception.NotFoundException;
import com.mboumda.paymybuddy.servive.exception.ValidationException;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class TransferService {
    private static final Logger log = LogManager.getLogger(TransferService.class);

    // 0,5% = 0.005
    private static final BigDecimal FEE_RATE = new BigDecimal("0.005");

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public TransferService(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Transfert d'argent entre deux utilisateurs.
     * - Calcul des frais: fee = amount * 0.005
     * - totalDebit = amount + fee
     * - Débit sender / Crédit receiver
     * - Enregistrement transaction
     * Tout se fait dans une transaction : rollback automatique si erreur.
     */
    @Transactional
    public void transferer(Long senderId, Long receiverId, BigDecimal amount, String description) {
        log.info("Transfert demandé: senderId={} receiverId={} amount={} description={}",
                senderId, receiverId, amount, description);

        if (senderId == null || receiverId == null) {
            log.error("Ids invalides: senderId={} receiverId={}", senderId, receiverId);
            throw new ValidationException("Identifiants invalides.");
        }

        if (senderId.equals(receiverId)) {
            log.error("Transfert vers soi-même interdit: senderId={}", senderId);
            throw new ValidationException("Impossible de transférer vers soi-même.");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Montant invalide: {}", amount);
            throw new ValidationException("Le montant doit être supérieur à 0.");
        }

        // On force 2 décimales pour éviter les surprises
        BigDecimal normalizedAmount = amount.setScale(2, RoundingMode.HALF_UP);

        UserEntity sender = userRepository.findById(senderId)
                .orElseThrow(() -> new NotFoundException("Émetteur introuvable."));

        UserEntity receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new NotFoundException("Destinataire introuvable."));

        BigDecimal fee = normalizedAmount.multiply(FEE_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalDebit = normalizedAmount.add(fee);

        log.debug("Calcul frais: amount={} feeRate={} fee={} totalDebit={}",
                normalizedAmount, FEE_RATE, fee, totalDebit);

        BigDecimal senderBalance = sender.getBalance() == null ? BigDecimal.ZERO : sender.getBalance();
        BigDecimal receiverBalance = receiver.getBalance() == null ? BigDecimal.ZERO : receiver.getBalance();

        log.debug("Soldes avant: senderBalance={} receiverBalance={}", senderBalance, receiverBalance);

        if (senderBalance.compareTo(totalDebit) < 0) {
            log.error("Solde insuffisant: senderId={} balance={} totalDebit={}",
                    senderId, senderBalance, totalDebit);
            throw new ValidationException("Solde insuffisant.");
        }

        // Mise à jour soldes
        sender.setBalance(senderBalance.subtract(totalDebit));
        receiver.setBalance(receiverBalance.add(normalizedAmount));

        // Persist
        userRepository.save(sender);
        userRepository.save(receiver);

        // Transaction record
        TransactionEntity tx = new TransactionEntity();
        tx.setSender(sender);
        tx.setReceiver(receiver);
        tx.setAmount(normalizedAmount);
        tx.setFee(fee);
        tx.setDescription((description == null) ? null : description.trim());

        transactionRepository.save(tx);

        log.info("Transfert effectué: senderId={} receiverId={} amount={} fee={}",
                senderId, receiverId, normalizedAmount, fee);
        log.debug("Soldes après: senderBalance={} receiverBalance={}",
                sender.getBalance(), receiver.getBalance());
    }
}
