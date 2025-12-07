package com.javatpoint;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}

/*
Spring Boot automatically gives you full CRUD database operations for the Product entity.

You get these methods for free:

✔ Save a product
productRepository.save(product);

✔ Save all products
productRepository.saveAll(products);

✔ Fetch all
productRepository.findAll();

✔ Fetch by ID
productRepository.findById(10);

✔ Delete
productRepository.deleteById(5);

✔ Count
productRepository.count();

✔ Pagination & Sorting
productRepository.findAll(PageRequest.of(0, 10));


And more — without writing even one line of SQL.
 */