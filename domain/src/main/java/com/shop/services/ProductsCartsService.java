package com.shop.services;

import com.shop.models.Cart;
import com.shop.models.Product;
import com.shop.models.Wallet;
import com.shop.repositories.ProductsCartsRepository;
import com.shop.stripe.StripePayment;
import com.shop.validators.CartValidator;
import com.shop.validators.UserValidator;
import com.shop.validators.ProductValidator;
import com.shop.validators.WalletValidator;
import com.stripe.exception.StripeException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductsCartsService {
    private final ProductsCartsRepository productsCartsRepository;
    private final CartService cartService;
    private final UserService userService;
    private final ProductService productService;
    private final WalletService walletService;
    private final ProductValidator productValidator;
    private final CartValidator cartValidator;
    private final UserValidator userValidator;
    private final WalletValidator walletValidator;
    private final StripePayment stripePayment;

    public ProductsCartsService(
        ProductsCartsRepository productsCartsRepository,
        CartService cartService,
        UserService userService,
        ProductService productService,
        WalletService walletService,
        ProductValidator productValidator,
        CartValidator cartValidator,
        UserValidator userValidator,
        WalletValidator walletValidator,
        StripePayment stripePayment
    ) {
        this.productsCartsRepository = productsCartsRepository;
        this.cartService = cartService;
        this.userService = userService;
        this.productService = productService;
        this.walletService = walletService;
        this.productValidator = productValidator;
        this.cartValidator = cartValidator;
        this.userValidator = userValidator;
        this.walletValidator = walletValidator;
        this.stripePayment = stripePayment;
    }

    public List<Product> findAllProductsInCart(long cartId) {
        cartValidator.validate(cartId);
        return productsCartsRepository.findAllProductsInCart(cartId);
    }

    public long saveProductToCart(long productId, long cartId) {
        productValidator.validate(productId, productService.findAll());
        cartValidator.validate(cartId);
        productsCartsRepository.saveProductToCart(productId, cartId);

        Product product = productService.findById(productId);
        Cart cart = cartService.findById(cartId);

        List<Product> products = cart.getProducts();
        products.add(product);
        cart.setProducts(products);

        double newTotalCost = cart.getTotalCost();
        newTotalCost += product.getPrice();
        cartService.update(cartId, newTotalCost);

        return productId;
    }

    public void deleteProductFromCart(long productId, long cartId) {
        productValidator.validate(productId, productService.findAll());
        cartValidator.validate(cartId);
        productsCartsRepository.deleteProductFromCart(productId, cartId);

        Product product = productService.findById(productId);
        Cart cart = cartService.findById(cartId);

        double newTotalCost = cart.getTotalCost();
        newTotalCost -= product.getPrice();
        cartService.update(cartId, newTotalCost);
    }

    public void deleteProductsFromCart(long cartId) {
        cartValidator.validate(cartId);
        productsCartsRepository.deleteProductsFromCart(cartId);
    }

    public void buy(long userId) throws StripeException {
        userValidator.validate(userId, userService.findAll());

        Wallet wallet = walletService.findByUser(userId);
        Cart cart = cartService.findByUser(userId);

        double newAmountOfMoney = wallet.getAmountOfMoney();
        newAmountOfMoney -= cart.getTotalCost();

        walletValidator.validateAmountOfMoney(newAmountOfMoney);

        String[] splitMoney = String.valueOf(newAmountOfMoney).split("\\.");
        if (splitMoney[1].length() == 1) {
            splitMoney[1] += "0";
        }
        long money = Long.parseLong(splitMoney[0] + splitMoney[1]) * -1;

        walletService.update(wallet.getId(), newAmountOfMoney);
        stripePayment.updateCustomer(wallet.getNumber(), money);

        cartService.update(cart.getId(), 0);

        deleteProductsFromCart(cart.getId());
    }
}
