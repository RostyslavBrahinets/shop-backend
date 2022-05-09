package com.shop.services;

import com.shop.exceptions.NotFoundException;
import com.shop.models.Basket;
import com.shop.models.Product;
import com.shop.repositories.ProductsBasketsRepository;
import com.shop.validators.BasketValidator;
import com.shop.validators.ProductValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductsBasketsService {
    private final ProductsBasketsRepository productsBasketsRepository;
    private final BasketService basketService;
    private final ProductService productService;
    private final ProductValidator productValidator;
    private final BasketValidator basketValidator;

    public ProductsBasketsService(
        ProductsBasketsRepository productsBasketsRepository,
        BasketService basketService,
        ProductService productService,
        ProductValidator productValidator,
        BasketValidator basketValidator
    ) {
        this.productsBasketsRepository = productsBasketsRepository;
        this.basketService = basketService;
        this.productService = productService;
        this.productValidator = productValidator;
        this.basketValidator = basketValidator;
    }

    public List<Product> getProductsFromBasket(long basketId) {
        basketValidator.validate(basketId);
        return productsBasketsRepository.getProductsFromBasket(basketId);
    }

    public long addProductToBasket(long productId, long basketId) {
        productValidator.validate(productId);
        basketValidator.validate(basketId);
        productsBasketsRepository.addProductToBasket(productId, basketId);

        Product product = productService.getProduct(productId);
        Basket basket = basketService.getBasket(basketId);

        List<Product> products = basket.getProducts();
        products.add(product);
        basket.setProducts(products);

        double newTotalCost = basket.getTotalCost();
        newTotalCost += product.getPrice();
        basket.setTotalCost(newTotalCost);
        basketService.updateBasket(basketId, basket);

        return productId;
    }

    public void deleteProductFromBasket(long productId, long basketId) {
        productValidator.validate(productId);
        basketValidator.validate(basketId);
        productsBasketsRepository.deleteProductFromBasket(productId, productId);

        Product product = productService.getProduct(productId);
        Basket basket = basketService.getBasket(basketId);

        List<Product> products = basket.getProducts();
        products.remove((int) productId);
        basket.setProducts(products);

        double newTotalCost = basket.getTotalCost();
        newTotalCost -= product.getPrice();
        basket.setTotalCost(newTotalCost);
        basketService.updateBasket(basketId, basket);
    }

    public Product getProductFromBasket(long productId, long basketId) {
        productValidator.validate(productId);
        basketValidator.validate(basketId);

        Optional<Product> product = productsBasketsRepository
            .getProductFromBasket(productId, basketId);
        if (product.isEmpty()) {
            throw new NotFoundException("Product not found");
        } else {
            return product.get();
        }
    }
}
