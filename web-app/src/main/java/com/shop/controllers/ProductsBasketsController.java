package com.shop.controllers;

import com.shop.models.Basket;
import com.shop.models.Person;
import com.shop.models.Product;
import com.shop.services.BasketService;
import com.shop.services.PersonService;
import com.shop.services.ProductsBasketsService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = ProductsBasketsController.PRODUCTS_BASKETS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductsBasketsController {
    public static final String PRODUCTS_BASKETS_URL = "/web-api/products-baskets";
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
}
