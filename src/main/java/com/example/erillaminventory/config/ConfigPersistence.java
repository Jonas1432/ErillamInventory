package com.example.erillaminventory.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class ConfigPersistence {
    @PersistenceContext(unitName = "erillam_inventory")
    private EntityManager entityManager;

    public EntityManager getEntityManager(){
        return entityManager;
    }
}
