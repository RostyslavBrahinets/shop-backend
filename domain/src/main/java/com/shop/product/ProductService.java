package com.shop.product;

import com.shop.category.Category;
import com.shop.productcategory.ProductCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductValidator productValidator;
    private final ProductCategoryRepository productCategoryRepository;

    public ProductService(
        ProductRepository productRepository,
        ProductValidator productValidator,
        ProductCategoryRepository productCategoryRepository
    ) {
        this.productRepository = productRepository;
        this.productValidator = productValidator;
        this.productCategoryRepository = productCategoryRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(long id) {
        productValidator.validate(id, productRepository.findAll());
        Optional<Product> product = productRepository.findById(id);
        return product.orElseGet(Product::new);
    }

    public Product findByBarcode(String barcode) {
        productValidator.validate(barcode, productRepository.findAll());
        Optional<Product> product = productRepository.findByBarcode(barcode);
        return product.orElseGet(Product::new);
    }

    public Product save(Product product) {
        productValidator.validate(
            product.getName(),
            product.getDescribe(),
            product.getPrice(),
            product.getBarcode(),
            productRepository.findAll());

        productRepository.save(
            product.getName(),
            product.getDescribe(),
            product.getPrice(),
            product.getBarcode(),
            product.isInStock(),
            product.getImage()
        );

        product.setId(productRepository.findAll().size() + 1);

        return product;
    }

    public Product update(Product product) {
        return new Product();
    }

    public void delete(Product product) {
        productValidator.validate(product.getBarcode(), productRepository.findAll());

        Optional<Product> productOptional = productRepository.findByBarcode(product.getBarcode());

        if (productOptional.isPresent()) {
            Optional<Category> category = productCategoryRepository
                .findCategoryForProduct(productOptional.get().getId());
            category.ifPresent(
                value -> productCategoryRepository
                    .deleteProductFromCategory(
                        productOptional.get().getId(),
                        value.getId()
                    )
            );
        }

        productRepository.delete(product.getBarcode());
    }

    public void saveImageForProduct(byte[] image, long id) {
        productValidator.validate(id, productRepository.findAll());
        productRepository.saveImageForProduct(image, id);
    }

    public List<Product> findRandomProducts(int count) {
        List<Product> products = new ArrayList<>();
        int size = findAll().size();

        if (count > size) {
            count = size;
        }

        int i = 0;
        while (i < count) {
            Optional<Product> product = productRepository.findById(getRandomIndex());
            if (product.isPresent()) {
                if (!products.contains(product.get())) {
                    products.add(product.get());
                    i++;
                }
            }
        }

        return products;
    }

    private long getRandomIndex() {
        Random random = new Random();
        List<Product> products = findAll();
        long maxRandomId = products.get(products.size() - 1).getId();

        List<Long> ids = new ArrayList<>();
        for (Product product : products) {
            ids.add(product.getId());
        }

        long id = 0;
        while (!ids.contains(id)) {
            id = random.nextInt((int) maxRandomId) + 1;
        }

        return id;
    }
}
