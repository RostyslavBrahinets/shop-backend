package com.shop.mvc;

import com.shop.models.Product;
import com.shop.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductViewController {
    private final ProductService productService;

    public ProductViewController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String index(
        @AuthenticationPrincipal UserDetails userDetails,
        Model model
    ) {
        if (userDetails != null) {
            GrantedAuthority role = userDetails.getAuthorities().stream().toList().get(0);
            model.addAttribute("role", role.getAuthority());
        } else {
            model.addAttribute("role", "ROLE_GUEST");
        }

        return "products/index";
    }

    @GetMapping("/{id}")
    public String find(
        @PathVariable long id,
        @AuthenticationPrincipal UserDetails userDetails,
        Model model
    ) {
        final Logger logger = LoggerFactory.getLogger(ProductViewController.class);

        if (userDetails != null) {
            GrantedAuthority role = userDetails.getAuthorities().stream().toList().get(0);
            model.addAttribute("role", role.getAuthority());
        } else {
            model.addAttribute("role", "ROLE_GUEST");
        }

        model.addAttribute("id", id);

        try {
            Product product = productService.findById(id);
            model.addAttribute("inStock", product.isInStock());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return "products/find";
    }
}
