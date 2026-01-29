package com.mboumda.paymybuddy.repository;

import com.mboumda.paymybuddy.entity.User;
import com.mboumda.paymybuddy.entity.UserConnection;
import com.mboumda.paymybuddy.entity.UserConnectionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserConnectionRepository extends JpaRepository<UserConnection, Long> {


    /**
     * Objectifs :
     *
     * vérifier si une paire existe (A,B) (normalisée min/max côté service)
     *
     * lister les connexions d’un user
     *
     * Comme on stockera toujours en (minId, maxId), la vérification devient simple.
     * @param id
     * @return
     */


    boolean existsById(UserConnectionId id);

    @Query("""
    select case when count(uc) > 0 then true else false end
    from UserConnection uc
    where (uc.user.id = :userId and uc.connection.id = :connectionId)
       or (uc.user.id = :connectionId and uc.connection.id = :userId)
  """)
    boolean existsAnyDirection(@Param("userId") Long userId,
                               @Param("connectionId") Long connectionId);

    /**
     * Retourne la liste des User "amis" d'un utilisateur.
     * On exploite la table de jointure self many-to-many.
     */
    @Query("""
    select case
      when uc.user.id = :userId then uc.connection
      else uc.user
    end
    from UserConnection uc
    where :userId in (uc.user.id, uc.connection.id)
    order by
      case when uc.user.id = :userId then uc.connection.username else uc.user.username end
  """)
    List<User> findConnectionsOf(@Param("userId") Long userId);
}
