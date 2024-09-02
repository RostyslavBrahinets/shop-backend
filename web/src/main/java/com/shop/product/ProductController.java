package com.shop.product;

import com.shop.utilities.ImageUtility;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping(ProductController.PRODUCTS_URL)
public class ProductController {
    public static final String PRODUCTS_URL = "/api/v1/products";
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public List<Product> findAll(
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if (filter != null && !filter.isBlank()) {
            return productService.findAllByNameLike("%" + filter + "%");
        }

        return productService.findAll();
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable long id) {
        return productService.findById(id);
    }

    @PostMapping("")
    public Product save(@RequestBody Product product) throws IOException {
        if (product.getImage().length > 0) {
            return productService.save(getNewProduct(product));
        }

        product = productService.save(getNewProduct(product));

        Product newProduct = productService.findByBarcode(product.getBarcode());
        String imagePath = Objects.requireNonNull(getClass().getClassLoader().getResource(
            "static/images/empty.jpg"
        )).getFile();
        byte[] byteImage = ImageUtility.imageToBytes(new File(imagePath));
        productService.saveImageForProduct(byteImage, newProduct.getId());
        newProduct.setImage(byteImage);
        return newProduct;
    }

    @PutMapping("/{id}")
    public Product update(
        @PathVariable long id,
        @RequestBody Product product
    ) {
        return productService.update(id, product);
    }

    @DeleteMapping("/{barcode}")
    public void delete(@PathVariable String barcode) {
        productService.delete(Product.of(barcode));
    }

    @GetMapping("/image/{id}")
    public void showProductImage(
        @PathVariable long id,
        HttpServletResponse response
    ) throws IOException {
        Product product = productService.findById(id);
        InputStream is = new ByteArrayInputStream(product.getImage());
        IOUtils.copy(is, response.getOutputStream());
    }

    private static Product getNewProduct(Product product) {
        return Product.of(
            product.getName(),
            product.getDescribe(),
            product.getPrice(),
            product.getBarcode(),
            product.isInStock(),
            product.getImage()
        );
    }
}
