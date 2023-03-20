package com.shop.productscarts;

import com.shop.cart.Cart;
import com.shop.cart.CartService;
import com.shop.cart.CartValidator;
import com.shop.product.Product;
import com.shop.product.ProductService;
import com.shop.product.ProductValidator;
import com.shop.stripe.StripePayment;
import com.shop.user.User;
import com.shop.user.UserService;
import com.shop.user.UserValidator;
import com.shop.wallet.Wallet;
import com.shop.wallet.WalletService;
import com.shop.wallet.WalletValidator;
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
        cartService.update(Cart.of(newTotalCost, 0).withId(cartId));

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
        cartService.update(Cart.of(newTotalCost, 0).withId(cartId));
    }

    public void deleteProductsFromCart(long cartId) {
        cartValidator.validate(cartId);
        productsCartsRepository.deleteProductsFromCart(cartId);
    }

    public void buy(long userId) throws StripeException {
        userValidator.validate(userId, userService.findAll());

        Wallet wallet = walletService.findByUser(User.of(null, null).withId(userId));
        Cart cart = cartService.findByUser(User.of(null, null).withId(userId));

        double newAmountOfMoney = wallet.getAmountOfMoney();
        newAmountOfMoney -= cart.getTotalCost();

        walletValidator.validateAmountOfMoney(newAmountOfMoney);

        String[] splitMoney = String.valueOf(newAmountOfMoney).split("\\.");
        if (splitMoney[1].length() == 1) {
            splitMoney[1] += "0";
        }
        long money = Long.parseLong(splitMoney[0] + splitMoney[1]) * -1;

        walletService.update(Wallet.of(null, newAmountOfMoney, userId).withId(wallet.getId()));
        stripePayment.updateCustomer(wallet.getNumber(), money);

        cartService.update(Cart.of(0, 0).withId(cart.getId()));

        deleteProductsFromCart(cart.getId());
    }
}
