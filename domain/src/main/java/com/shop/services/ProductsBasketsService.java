package com.shop.services;

import com.shop.models.Basket;
import com.shop.models.Product;
import com.shop.models.Wallet;
import com.shop.repositories.ProductsBasketsRepository;
import com.shop.stripe.StripePayment;
import com.shop.validators.BasketValidator;
import com.shop.validators.PersonValidator;
import com.shop.validators.ProductValidator;
import com.shop.validators.WalletValidator;
import com.stripe.exception.StripeException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductsBasketsService {
    private final ProductsBasketsRepository productsBasketsRepository;
    private final BasketService basketService;
    private final ProductService productService;
    private final WalletService walletService;
    private final ProductValidator productValidator;
    private final BasketValidator basketValidator;
    private final PersonValidator personValidator;
    private final WalletValidator walletValidator;
    private final StripePayment stripePayment;

    public ProductsBasketsService(
        ProductsBasketsRepository productsBasketsRepository,
        BasketService basketService,
        ProductService productService,
        WalletService walletService,
        ProductValidator productValidator,
        BasketValidator basketValidator,
        PersonValidator personValidator,
        WalletValidator walletValidator,
        StripePayment stripePayment
    ) {
        this.productsBasketsRepository = productsBasketsRepository;
        this.basketService = basketService;
        this.productService = productService;
        this.walletService = walletService;
        this.productValidator = productValidator;
        this.basketValidator = basketValidator;
        this.personValidator = personValidator;
        this.walletValidator = walletValidator;
        this.stripePayment = stripePayment;
    }

    public List<Product> findAllProductsInBasket(long basketId) {
        basketValidator.validate(basketId);
        return productsBasketsRepository.findAllProductsInBasket(basketId);
    }

    public long saveProductToBasket(long productId, long basketId) {
        productValidator.validate(productId);
        basketValidator.validate(basketId);
        productsBasketsRepository.saveProductToBasket(productId, basketId);

        Product product = productService.findById(productId);
        Basket basket = basketService.findById(basketId);

        List<Product> products = basket.getProducts();
        products.add(product);
        basket.setProducts(products);

        double newTotalCost = basket.getTotalCost();
        newTotalCost += product.getPrice();
        basketService.update(basketId, newTotalCost);

        return productId;
    }

    public void deleteProductFromBasket(long productId, long basketId) {
        productValidator.validate(productId);
        basketValidator.validate(basketId);
        productsBasketsRepository.deleteProductFromBasket(productId, basketId);

        Product product = productService.findById(productId);
        Basket basket = basketService.findById(basketId);

        double newTotalCost = basket.getTotalCost();
        newTotalCost -= product.getPrice();
        basketService.update(basketId, newTotalCost);
    }

    public void deleteProductsFromBasket(long basketId) {
        basketValidator.validate(basketId);
        productsBasketsRepository.deleteProductsFromBasket(basketId);
    }

    public void buy(long personId) throws StripeException {
        personValidator.validate(personId);

        Wallet wallet = walletService.findByPerson(personId);
        Basket basket = basketService.findByPerson(personId);

        double newAmountOfMoney = wallet.getAmountOfMoney();
        newAmountOfMoney -= basket.getTotalCost();

        walletValidator.validateAmountOfMoney(newAmountOfMoney);

        String[] splitMoney = String.valueOf(newAmountOfMoney).split("\\.");
        long money = Long.parseLong(splitMoney[0] + splitMoney[1]) * -1;

        walletService.update(wallet.getId(), newAmountOfMoney);
        stripePayment.updateCustomer(wallet.getNumber(), money);

        basketService.update(basket.getId(), 0);

        deleteProductsFromBasket(basket.getId());
    }
}
