package com.shop.controllers;

import com.shop.configs.AppConfig;
import com.shop.models.Basket;
import com.shop.services.BasketService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = BasketController.BASKETS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class BasketController {
    public static final String BASKETS_URL = "/baskets";
    public static final AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext(AppConfig.class);
    public static final BasketService basketService =
        applicationContext.getBean(BasketService.class);

    @GetMapping
    public List<Basket> findAllBaskets() {
        return basketService.getBaskets();
    }

    @GetMapping("/{id}")
    public Basket findByIdBasket(@PathVariable int id) {
        return basketService.getBasket(id);
    }

    @PostMapping
    public Basket saveWallet(
        @RequestBody Basket basket,
        @RequestBody int personId
    ) {
        return basketService.addBasket(basket, personId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBasket(@PathVariable int id) {
        basketService.deleteBasket(id);
    }
}
