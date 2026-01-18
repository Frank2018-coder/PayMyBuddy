package com.mboumda.paymybuddy.repository;

import com.mboumda.paymybuddy.model.ConnectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *cest methodes couvrent
 * afficher les relations dans le select (Transfer)
 *
 * empêcher doublons (Add connection)
 *
 * option future : supprimer relation
 */
public interface ConnectionRepository extends JpaRepository<ConnectionEntity, Integer> {

    List<ConnectionEntity> findByUserId(Long userId);

    boolean existsByUserIdAndBuddyId(Long userId, Long buddyId);

    long deleteByUserIdAndBuddyId(Long userId, Long buddyId);
}
