package com.shop.controllers;

import com.shop.models.Basket;
import com.shop.models.Person;
import com.shop.models.Product;
import com.shop.services.BasketService;
import com.shop.services.PersonService;
import com.shop.services.ProductsBasketsService;
import com.stripe.exception.StripeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = ProductsBasketsController.PRODUCTS_BASKETS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductsBasketsController {
    public static final String PRODUCTS_BASKETS_URL = "/web-api/products-baskets";
    private static final Logger logger = LoggerFactory
        .getLogger(ProductsBasketsController.class);
    private final ProductsBasketsService productsBasketsService;
    private final BasketService basketService;
    private final PersonService personService;

    public ProductsBasketsController(
        ProductsBasketsService productsBasketsService,
        BasketService basketService,
        PersonService personService
    ) {
        this.productsBasketsService = productsBasketsService;
        this.basketService = basketService;
        this.personService = personService;
    }

    @GetMapping
    public List<Product> findAllProductsInBasket(@AuthenticationPrincipal UserDetails userDetail) {
        Person person = personService.getPerson(userDetail.getUsername());
        Basket basket = basketService.getBasketByPerson(person.getId());
        return productsBasketsService.getProductsFromBasket(basket.getId());
    }

    @PostMapping("/{id}")
    public long addProductToBasket(
        @AuthenticationPrincipal UserDetails userDetail,
        @PathVariable long id,
        HttpServletResponse response
    ) throws IOException {
        Person person = personService.getPerson(userDetail.getUsername());
        Basket basket = basketService.getBasketByPerson(person.getId());
        response.sendRedirect("/products");
        return productsBasketsService.addProductToBasket(id, basket.getId());
    }

    @PostMapping("/buy")
    public void buy(
        @AuthenticationPrincipal UserDetails userDetail,
        HttpServletResponse response
    ) throws IOException {
        try {
            Person person = personService.getPerson(userDetail.getUsername());
            productsBasketsService.buy(person.getId());
        } catch (StripeException e) {
            logger.error(e.getMessage(), e);
        }
        response.sendRedirect("/basket");
    }
}
