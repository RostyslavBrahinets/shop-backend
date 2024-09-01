package com.shop.product;

import com.shop.category.Category;
import com.shop.interfaces.ServiceInterface;
import com.shop.productcategory.ProductCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ProductService implements ServiceInterface<Product> {
    private final ProductRepository productRepository;
    private final ProductValidator productValidator;
    private final ProductCategoryRepository productCategoryRepository;
    private final Random random;

    public ProductService(
        ProductRepository productRepository,
        ProductValidator productValidator,
        ProductCategoryRepository productCategoryRepository
    ) {
        this.productRepository = productRepository;
        this.productValidator = productValidator;
        this.productCategoryRepository = productCategoryRepository;
        this.random = new Random();
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(long id) {
        productValidator.validate(id, productRepository.findAll());
        Optional<Product> product = productRepository.findById(id);
        return product.orElseGet(Product::new);
    }

    @Override
    @Transactional
    public Product save(Product product) {
        productValidator.validate(
            product.getName(),
            product.getDescribe(),
            product.getPrice(),
            product.getBarcode(),
            productRepository.findAll()
        );

        productRepository.save(product);
        product.setId(productRepository.findAll().size() + 1L);
        return product;
    }

    @Override
    @Transactional
    public Product update(long id, Product product) {
        productValidator.validate(id, findAll());
        productValidator.validateUpdatedProduct(product);
        productRepository.update(id, product);
        product.setId(id);
        return product;
    }

    @Override
    @Transactional
    public void delete(Product product) {
        productValidator.validateBarcode(product.getBarcode(), productRepository.findAll());

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

        productRepository.delete(product);
    }

    public Product findByBarcode(String barcode) {
        productValidator.validateBarcode(barcode, productRepository.findAll());
        Optional<Product> product = productRepository.findByBarcode(barcode);
        return product.orElseGet(Product::new);
    }

    @Transactional
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
            if (product.isPresent() && (!products.contains(product.get()))) {
                products.add(product.get());
                i++;
            }
        }

        return products;
    }

    private long getRandomIndex() {
        List<Product> products = findAll();
        long maxRandomId = products.get(products.size() - 1).getId();

        List<Long> ids = new ArrayList<>();
        for (Product product : products) {
            ids.add(product.getId());
        }

        long id = 0;
        while (!ids.contains(id)) {
            id = random.nextInt((int) maxRandomId) + 1L;
        }

        return id;
    }
}
