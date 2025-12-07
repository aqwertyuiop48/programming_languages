package com.javatpoint;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService implements IProductService {

    private final ProductRepository repo;
    private final ProductMapper mapper;

    public ProductService(ProductRepository repo, ProductMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    // ========================= DB PRODUCTS =============================

    @Override
    public List<ProductDTO> getProductsFromDb() {
        // Auto-load DB only if empty
        if (repo.count() == 0) {
            repo.saveAll(getHardcodedEntityProducts());
        }

        List<Product> products = repo.findAll();
        return mapper.toDTOList(products);
    }

    @Override
    public ProductDTO getProductById(int id) {
        Product product = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return mapper.toDTO(product);
    }

    // ===================== HARDCODED PRODUCTS ==========================
    @Override
    public List<ProductDTO> getHardcodedProducts() {
        List<Product> products = getHardcodedEntityProducts();
        return mapper.toDTOList(products);
    }

    // ===================== POST / CREATE PRODUCT ======================
    @Override
    public ProductDTO createProduct(ProductDTO dto) {
        Product entity = mapper.toEntity(dto);
        Product saved = repo.save(entity);
        return mapper.toDTO(saved);
    }

    // ===================== PUT / UPDATE PRODUCT ======================
    @Override
    public ProductDTO updateProduct(int id, ProductDTO dto) {
        Product existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        // Update fields (ID is auto-generated, do not update)
        existing.setPname(dto.getName());
        existing.setBatchno(dto.getBatch());
        existing.setPrice(dto.getPrice());
        existing.setNoofproduct(dto.getQuantity());

        Product updated = repo.save(existing);
        return mapper.toDTO(updated);
    }

    // ===================== PRIVATE HARDCODED ENTITY LIST =================
    private List<Product> getHardcodedEntityProducts() {
        ArrayList<Product> products = new ArrayList<>();

        products.add(new Product("Mobiles", "CLK98123", 9000.00, 6));
        products.add(new Product("Smart TV", "LGST09167", 60000.00, 3));
        products.add(new Product("Washing Machine", "38753BK9", 9000.00, 7));
        products.add(new Product("Laptop", "LHP29OCP", 24000.00, 1));
        products.add(new Product("Air Conditioner", "ACLG66721", 30000.00, 5));
        products.add(new Product("Refrigerator", "12WP9087", 10000.00, 4));

        return products;
    }
}
