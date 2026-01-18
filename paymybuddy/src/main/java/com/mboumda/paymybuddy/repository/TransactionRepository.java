package com.mboumda.paymybuddy.repository;

import com.mboumda.paymybuddy.model.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * methodes qui couvrent :
 * historique “Mes transactions” pour l’utilisateur connecté
 *
 * pratique si tu identifies l’utilisateur par email dans la couche sécurité
 */
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findBySenderIdOrderByCreatedAtDesc(Long senderId);

    List<TransactionEntity> findBySenderEmailOrderByCreatedAtDesc(String senderEmail);
}
