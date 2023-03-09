package com.shop.controllers;

import com.shop.dto.ReportDto;
import com.shop.models.Cart;
import com.shop.models.Person;
import com.shop.models.Product;
import com.shop.models.Wallet;
import com.shop.services.CartService;
import com.shop.services.PersonService;
import com.shop.services.ProductsCartsService;
import com.shop.services.WalletService;
import com.shop.utilities.PdfUtility;
import com.stripe.exception.StripeException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(ProductsCartsController.PRODUCTS_CARTS_URL)
public class ProductsCartsController {
    public static final String PRODUCTS_CARTS_URL = "/web-api/products-carts";
    private final ProductsCartsService productsCartsService;
    private final CartService cartService;
    private final PersonService personService;
    private final WalletService walletService;
    private final ReportDto report;

    public ProductsCartsController(
        ProductsCartsService productsCartsService,
        CartService cartService,
        PersonService personService,
        WalletService walletService,
        ReportDto report) {
        this.productsCartsService = productsCartsService;
        this.cartService = cartService;
        this.personService = personService;
        this.walletService = walletService;
        this.report = report;
    }

    @GetMapping
    public List<Product> findAllProductsInCart(@AuthenticationPrincipal UserDetails userDetail) {
        Person person = personService.findByEmail(userDetail.getUsername());
        Cart cart = cartService.findByPerson(person.getId());
        return productsCartsService.findAllProductsInCart(cart.getId());
    }

    @PostMapping("/{id}")
    public long saveProductToCart(
        @AuthenticationPrincipal UserDetails userDetail,
        @PathVariable long id,
        HttpServletResponse response
    ) throws IOException {
        Person person = personService.findByEmail(userDetail.getUsername());
        Cart cart = cartService.findByPerson(person.getId());
        response.sendRedirect("/cart");
        return productsCartsService.saveProductToCart(id, cart.getId());
    }

    @PostMapping("/{id}/delete")
    public void deleteProductFromCart(
        @AuthenticationPrincipal UserDetails userDetail,
        @PathVariable long id
    ) {
        Person person = personService.findByEmail(userDetail.getUsername());
        Cart cart = cartService.findByPerson(person.getId());
        productsCartsService.deleteProductFromCart(id, cart.getId());
    }

    @PostMapping("/buy")
    public String buy(
        @AuthenticationPrincipal UserDetails userDetail
    ) throws StripeException {
        Person person = personService.findByEmail(userDetail.getUsername());
        Cart cart = cartService.findByPerson(person.getId());

        List<Product> productsInCart = productsCartsService
            .findAllProductsInCart(cart.getId());

        if (productsInCart.size() == 0) {
            return "Cart Is Empty";
        }

        report.setProducts(productsInCart);
        report.setTotalCost(cart.getTotalCost());

        productsCartsService.buy(person.getId());

        return "Products Bought";
    }

    @PostMapping("/download-report")
    public void downloadReport(
        @AuthenticationPrincipal UserDetails userDetail,
        HttpServletResponse response
    ) throws IOException {
        Person person = personService.findByEmail(userDetail.getUsername());
        Wallet wallet = walletService.findByPerson(person.getId());
        report.setAmountOfMoney(wallet.getAmountOfMoney());

        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=report_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
        PdfUtility pdfUtility = new PdfUtility(report);
        pdfUtility.export(response);
    }
}
