package com.shop.controllers;

import com.shop.dto.ReportDto;
import com.shop.models.Cart;
import com.shop.models.User;
import com.shop.models.Product;
import com.shop.models.Wallet;
import com.shop.services.CartService;
import com.shop.services.UserService;
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
    private final UserService userService;
    private final WalletService walletService;
    private final ReportDto report;

    public ProductsCartsController(
        ProductsCartsService productsCartsService,
        CartService cartService,
        UserService userService,
        WalletService walletService,
        ReportDto report) {
        this.productsCartsService = productsCartsService;
        this.cartService = cartService;
        this.userService = userService;
        this.walletService = walletService;
        this.report = report;
    }

    @GetMapping
    public List<Product> findAllProductsInCart(@AuthenticationPrincipal UserDetails userDetail) {
        User user = userService.findByEmail(userDetail.getUsername());
        Cart cart = cartService.findByUser(user.getId());
        return productsCartsService.findAllProductsInCart(cart.getId());
    }

    @PostMapping("/{id}")
    public long saveProductToCart(
        @AuthenticationPrincipal UserDetails userDetail,
        @PathVariable long id,
        HttpServletResponse response
    ) throws IOException {
        User user = userService.findByEmail(userDetail.getUsername());
        Cart cart = cartService.findByUser(user.getId());
        response.sendRedirect("/cart");
        return productsCartsService.saveProductToCart(id, cart.getId());
    }

    @PostMapping("/{id}/delete")
    public void deleteProductFromCart(
        @AuthenticationPrincipal UserDetails userDetail,
        @PathVariable long id
    ) {
        User user = userService.findByEmail(userDetail.getUsername());
        Cart cart = cartService.findByUser(user.getId());
        productsCartsService.deleteProductFromCart(id, cart.getId());
    }

    @PostMapping("/buy")
    public String buy(
        @AuthenticationPrincipal UserDetails userDetail
    ) throws StripeException {
        User user = userService.findByEmail(userDetail.getUsername());
        Cart cart = cartService.findByUser(user.getId());

        List<Product> productsInCart = productsCartsService
            .findAllProductsInCart(cart.getId());

        if (productsInCart.size() == 0) {
            return "Cart Is Empty";
        }

        report.setProducts(productsInCart);
        report.setTotalCost(cart.getTotalCost());

        productsCartsService.buy(user.getId());

        return "Products Bought";
    }

    @PostMapping("/download-report")
    public void downloadReport(
        @AuthenticationPrincipal UserDetails userDetail,
        HttpServletResponse response
    ) throws IOException {
        User user = userService.findByEmail(userDetail.getUsername());
        Wallet wallet = walletService.findByUser(user.getId());
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
