package com.mboumda.paymybuddy.service;

import com.mboumda.paymybuddy.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

/***
 * Fonctions :
 *
 * effectuer un transfert (transactionnel)
 *
 * historique paginé
 *
 * solde net (reçus - envoyés) calculé via requêtes repo (ici simple via itération sur historique paginé ou requête dédiée)
 *
 * 👉 Pour le prototype, on fait :
 *
 * validation amount > 0
 *
 * interdiction sender == receiver
 *
 * exigence : receiver ∈ connexions
 *
 * @Transactional : si une exception est levée, rollback auto
 *
 */


public interface TransferService {

     Transaction transfer(Long senderId, Long receiverId,  String description, BigDecimal amount);

     Page<Transaction> getHistory(Long userId, Pageable pageable);
}
