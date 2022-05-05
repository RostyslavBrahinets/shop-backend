package com.shop.services;

import com.shop.exceptions.NotFoundException;
import com.shop.models.Basket;
import com.shop.models.Product;
import com.shop.repositories.BasketRepository;
import com.shop.repositories.ProductRepository;
import com.shop.repositories.ProductsBasketsRepository;
import com.shop.validators.BasketValidator;
import com.shop.validators.ProductValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductsBasketsService {
    private final ProductsBasketsRepository productsBasketsRepository;
    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;
    private final ProductValidator productValidator;
    private final BasketValidator basketValidator;

    public ProductsBasketsService(
        ProductsBasketsRepository productsBasketsRepository,
        BasketRepository basketRepository,
        ProductRepository productRepository,
        ProductValidator productValidator,
        BasketValidator basketValidator
    ) {
        this.productsBasketsRepository = productsBasketsRepository;
        this.basketRepository = basketRepository;
        this.productRepository = productRepository;
        this.productValidator = productValidator;
        this.basketValidator = basketValidator;
    }

    public List<Product> getProductsFromBasket(long idBasket) {
        basketValidator.validate(idBasket);
        return productsBasketsRepository.getProductsFromBasket(idBasket);
    }

    public long addProductToBasket(long idProduct, long idBasket) {
        productValidator.validate(idProduct);
        basketValidator.validate(idBasket);
        productsBasketsRepository.addProduct(idProduct, idProduct);

        Optional<Product> productOptional = productRepository.getProduct(idProduct);
        Optional<Basket> basketOptional = basketRepository.getBasket(idBasket);

        if (basketOptional.isPresent()) {
            Basket basket = basketOptional.get();
            List<Product> products = basket.getProducts();
            productOptional.ifPresent(products::add);
            basket.setProducts(products);
        }

        return idProduct;
    }

    public void deleteProductFromBasket(long idProduct, long idBasket) {
        productValidator.validate(idProduct);
        basketValidator.validate(idBasket);
        productsBasketsRepository.deleteProduct(idProduct, idProduct);

        Optional<Basket> basketOptional = basketRepository.getBasket(idBasket);

        if (basketOptional.isPresent()) {
            Basket basket = basketOptional.get();
            List<Product> products = basket.getProducts();
            products.remove((int) idProduct);
            basket.setProducts(products);
        }

    }

    public Product getProductFromBasket(long idProduct, long idBasket) {
        productValidator.validate(idProduct);
        basketValidator.validate(idBasket);

        Optional<Product> product = productsBasketsRepository.getProduct(idProduct, idBasket);
        if (product.isEmpty()) {
            throw new NotFoundException("Product not found");
        } else {
            return product.get();
        }
    }
}
