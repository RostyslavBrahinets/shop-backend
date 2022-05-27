package com.shop.controllers;

import com.shop.dto.ReportDto;
import com.shop.models.Basket;
import com.shop.models.Person;
import com.shop.models.Product;
import com.shop.models.Wallet;
import com.shop.services.BasketService;
import com.shop.services.PersonService;
import com.shop.services.ProductsBasketsService;
import com.shop.services.WalletService;
import com.shop.utilities.PdfUtility;
import com.stripe.exception.StripeException;
import org.springframework.http.MediaType;
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
@RequestMapping(value = ProductsBasketsController.PRODUCTS_BASKETS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductsBasketsController {
    public static final String PRODUCTS_BASKETS_URL = "/web-api/products-baskets";
    private final ProductsBasketsService productsBasketsService;
    private final BasketService basketService;
    private final PersonService personService;
    private final WalletService walletService;
    private final ReportDto report;

    public ProductsBasketsController(
        ProductsBasketsService productsBasketsService,
        BasketService basketService,
        PersonService personService,
        WalletService walletService,
        ReportDto report) {
        this.productsBasketsService = productsBasketsService;
        this.basketService = basketService;
        this.personService = personService;
        this.walletService = walletService;
        this.report = report;
    }

    @GetMapping
    public List<Product> findAllProductsInBasket(@AuthenticationPrincipal UserDetails userDetail) {
        Person person = personService.findByEmail(userDetail.getUsername());
        Basket basket = basketService.findByPerson(person.getId());
        return productsBasketsService.findAllProductsInBasket(basket.getId());
    }

    @PostMapping("/{id}")
    public long saveProductToBasket(
        @AuthenticationPrincipal UserDetails userDetail,
        @PathVariable long id,
        HttpServletResponse response
    ) throws IOException {
        Person person = personService.findByEmail(userDetail.getUsername());
        Basket basket = basketService.findByPerson(person.getId());
        response.sendRedirect("/basket");
        return productsBasketsService.saveProductToBasket(id, basket.getId());
    }

    @PostMapping("/{id}/delete")
    public void deleteProductFromBasket(
        @AuthenticationPrincipal UserDetails userDetail,
        @PathVariable long id,
        HttpServletResponse response
    ) throws IOException {
        Person person = personService.findByEmail(userDetail.getUsername());
        Basket basket = basketService.findByPerson(person.getId());
        response.sendRedirect("/basket");
        productsBasketsService.deleteProductFromBasket(id, basket.getId());
    }

    @PostMapping("/buy")
    public void buy(
        @AuthenticationPrincipal UserDetails userDetail,
        HttpServletResponse response
    ) throws StripeException, IOException {
        Person person = personService.findByEmail(userDetail.getUsername());
        Basket basket = basketService.findByPerson(person.getId());

        List<Product> productsInBasket = productsBasketsService
            .findAllProductsInBasket(basket.getId());

        report.setProducts(productsInBasket);
        report.setTotalCost(basket.getTotalCost());

        productsBasketsService.buy(person.getId());

        response.sendRedirect("/basket");
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
