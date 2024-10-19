package com.example.erillaminventory.repository;

import com.example.erillaminventory.entity.Product;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Stateless
public class ProductRepository {

    @PersistenceContext(unitName = "erillam_inventory")
    private EntityManager entityManager;

    public List<Product> findAll(int page, int size) {
        return entityManager.createQuery("SELECT p FROM Product p", Product.class)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    public List<Product> findByNameAndCategory(String name, String category) {
        return entityManager.createQuery("SELECT p FROM Product p WHERE p.name LIKE :name AND p.category = :category", Product.class)
                .setParameter("name", "%" + name + "%") // Para buscar por nombre (contiene)
                .setParameter("category", category)
                .getResultList();
    }



    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Product.class, id));
    }


    public void save(Product product) {
        entityManager.persist(product);
    }

    public void update(Product product) {
        entityManager.merge(product);
    }

    @Transactional
    public boolean deleteById(Long id) {
        Product product = entityManager.find(Product.class, id);
        if(product != null) {
            entityManager.remove(product);
            return true;
        }else{
            return false;
        }
    }
}
