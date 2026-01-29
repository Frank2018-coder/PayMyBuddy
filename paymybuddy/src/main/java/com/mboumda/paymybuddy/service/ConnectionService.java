package com.mboumda.paymybuddy.service;

import com.mboumda.paymybuddy.entity.User;

import java.util.List;

public interface ConnectionService {


    /***
     * Fonctions :
     *
     * lister les connexions
     *
     * ajouter une connexion par email
     *
     * normaliser la paire (minId, maxId) pour éviter doublon A–B/B–A
     */


    List<User> getConnections(Long userId);

    void addConnection(Long userId, String connectionEmail);
}
