package com.mboumda.paymybuddy.repository;

import com.mboumda.paymybuddy.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Objectifs :
     *
     * historique d’un user (envoyées + reçues)
     *pagination possible
     *
     * @param senderId
     * @param receiverId
     * @param pageable
     * @return
     */
    Page<Transaction> findBySenderIdOrReceiverIdOrderByCreatedAtDescIdDesc(
            Long senderId, Long receiverId, Pageable pageable
    );
}
