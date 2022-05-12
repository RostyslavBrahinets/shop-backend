package com.shop.services;

import com.shop.exceptions.NotFoundException;
import com.shop.models.Basket;
import com.shop.models.Product;
import com.shop.models.Wallet;
import com.shop.repositories.ProductsBasketsRepository;
import com.shop.stripe.StripePayment;
import com.shop.validators.BasketValidator;
import com.shop.validators.PersonValidator;
import com.shop.validators.ProductValidator;
import com.stripe.exception.StripeException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductsBasketsService {
    private final ProductsBasketsRepository productsBasketsRepository;
    private final ProductValidator productValidator;
    private final BasketValidator basketValidator;
    private final PersonValidator personValidator;
    private final BasketService basketService;
    private final ProductService productService;
    private final WalletService walletService;
    private final StripePayment stripePayment;

    public ProductsBasketsService(
        ProductsBasketsRepository productsBasketsRepository,
        ProductValidator productValidator,
        BasketValidator basketValidator,
        PersonValidator personValidator,
        BasketService basketService,
        ProductService productService,
        WalletService walletService,
        StripePayment stripePayment
    ) {
        this.productsBasketsRepository = productsBasketsRepository;
        this.productValidator = productValidator;
        this.basketValidator = basketValidator;
        this.personValidator = personValidator;
        this.basketService = basketService;
        this.productService = productService;
        this.walletService = walletService;
        this.stripePayment = stripePayment;
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

    public void deleteProductsFromBasket(long basketId) {
        basketValidator.validate(basketId);
        productsBasketsRepository.deleteProductsFromBasket(basketId);
    }

    public void buy(long personId) throws StripeException {
        personValidator.validate(personId);

        Wallet wallet = walletService.getWalletByPerson(personId);
        Basket basket = basketService.getBasketByPerson(personId);

        double newAmountOfMoney = wallet.getAmountOfMoney();
        newAmountOfMoney -= basket.getTotalCost();

        if (newAmountOfMoney < 0) {
            throw new NotFoundException("Not enough money to buy");
        }

        wallet.setAmountOfMoney(newAmountOfMoney);
        walletService.updateWallet(wallet.getId(), wallet);
        stripePayment.updateCustomer(wallet.getNumber(), (long) newAmountOfMoney * -100);

        basket.setTotalCost(0);
        basket.setProducts(new ArrayList<>());
        basketService.updateBasket(basket.getId(), basket);

        deleteProductsFromBasket(basket.getId());
    }
}
