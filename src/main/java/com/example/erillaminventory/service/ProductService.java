package com.example.erillaminventory.service;

import com.example.erillaminventory.dto.ProductDTO;
import com.example.erillaminventory.entity.Product;
import com.example.erillaminventory.repository.ProductRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class ProductService {

    @Inject
    private ProductRepository productRepository;


    public List<ProductDTO> findAll(int page, int size) {
        List<Product> products = productRepository.findAll(page, size);
        return products.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProductDTO> findById(Long id) {
        return productRepository.findById(id).map(this::toDTO);
    }

    @Transactional
    public void save(ProductDTO productDTO) {
        productRepository.save(toEntity(productDTO));
    }

    @Transactional
    public void update(ProductDTO productDTO) {
        Optional<Product> existingStudent = productRepository.findById(productDTO.getId());
        if(existingStudent.isPresent()) {
            Product product = existingStudent.get();
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setPrice(productDTO.getPrice());
            product.setCategory(productDTO.getCategory());
            productRepository.update(product);
        }else{
            throw new IllegalArgumentException("Producto no encontrado" + productDTO.getId());
        }
    }

    @Transactional
    public void updateStock(Long id, int stock) {
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setStock(stock);
            productRepository.update(product);
        }else{
            throw new IllegalArgumentException("Producto no encontrado" + id);
        }
    }

    public List<ProductDTO> getNameAndCategory(String name, String category) {
        List<Product> products = productRepository.findByNameAndCategory(name, category);
        return products.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public boolean deleteById(Long id){
        return productRepository.deleteById(id);
    }

    private ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setCategory(product.getCategory());
        return dto;
    }

    private Product toEntity(ProductDTO dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setCategory(dto.getCategory());
        return product;
    }
}
