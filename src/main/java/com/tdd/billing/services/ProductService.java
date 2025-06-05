package com.tdd.billing.services;

import com.tdd.billing.dto.ProductRequestDTO;
import com.tdd.billing.dto.ProductResponseDTO;
import com.tdd.billing.entities.Product;
import com.tdd.billing.entities.Store;
import com.tdd.billing.entities.Category;
import com.tdd.billing.entities.User;
import com.tdd.billing.repositories.CategoryRepository;
import com.tdd.billing.repositories.ProductRepository;
import com.tdd.billing.mappers.ProductMapper;
import com.tdd.billing.repositories.StoreRepository;
import com.tdd.billing.repositories.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, StoreRepository storeRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }
    
    

    // Registrar un nuevo producto
    public ProductResponseDTO registerProduct(ProductRequestDTO productRequestDTO, String photoUrl) {
        Product product = new Product();
        product.setName(productRequestDTO.getName());
        product.setDescription(productRequestDTO.getDescription());
        product.setPrice(productRequestDTO.getPrice());
        product.setStock(productRequestDTO.getStock());
        product.setUrl(photoUrl);
        product.setRatingRate(productRequestDTO.getRatingRate());
        product.setRatingCount(productRequestDTO.getRatingCount());
        product.setStatus(productRequestDTO.getStatus());

        // Traer las entidades desde la base de datos para que estén persistidas
        Store store = storeRepository.findById(productRequestDTO.getStoreId())
                .orElseThrow(() -> new RuntimeException("Store not found"));
        product.setStore(store);

        Category category = categoryRepository.findById(productRequestDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);

        User user = userRepository.findById(productRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        product.setUser(user);

        Product savedProduct = productRepository.save(product);
        return ProductMapper.toResponseDTO(savedProduct);
    }

    public List<ProductResponseDTO> getRandomProducts(Long quantity) {
        List<Product> products = productRepository.findByStatusTrue();
        if (products.size() <= quantity) {
            return products.stream()
                    .map(ProductMapper::toResponseDTO)
                    .toList();
        }
        Collections.shuffle(products);

        return products.stream()
                .limit(quantity)
                .map(ProductMapper::toResponseDTO)
                .toList();
    }





    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }



    public List<Product> listActiveProducts() {
        return productRepository.findByStatusTrue();
    }


    public Page<ProductResponseDTO> listProductsByStore(Long storeId, Long categoryId, int page, int size) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        Page<Product> productsPage;

        if (categoryId == 0) {
            // Obtener productos de todas las categorías
            productsPage = productRepository.findByStoreAndStatusTrue(store, PageRequest.of(page, size));
        } else {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            productsPage = productRepository.findByStoreAndCategoryAndStatusTrue(store, category, PageRequest.of(page, size));
        }

        return productsPage.map(ProductMapper::toResponseDTO);
    }





//    // Listar productos activos por tienda
//    public List<Product> listActiveProductsByStore(Store store) {
//        return productRepository.findByStoreAndStatusTrue(store);
//    }

    // Listar productos por categoría
    public List<Product> listProductsByCategory(Category category) {
        return productRepository.findByCategory(category);
    }



    // Actualizar un producto existente
    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setStock(productDetails.getStock());
        product.setStatus(productDetails.getStatus());
        product.setStore(productDetails.getStore());
        product.setCategory(productDetails.getCategory());

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setStatus(false);
        productRepository.save(product);
    }

    public ProductResponseDTO convertToProductResponseDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setUrl(product.getUrl());
        dto.setRatingRate(product.getRatingRate());
        dto.setRatingCount(product.getRatingCount());
        dto.setStatus(product.getStatus());
        return dto;
    }
}
