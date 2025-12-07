package com.javatpoint;

import java.util.List;

public interface IProductService {

    // ========================= DB PRODUCTS =============================

    // Fetch all products from DB
    List<ProductDTO> getProductsFromDb();

    // Fetch product by ID from DB
    ProductDTO getProductById(int id);

    // Create a new product in DB
    ProductDTO createProduct(ProductDTO dto);

    // Update existing product in DB
    ProductDTO updateProduct(int id, ProductDTO dto);

    // ========================= HARDCODED PRODUCTS =====================

    // Fetch hardcoded product list
    List<ProductDTO> getHardcodedProducts();
}
