package com.shop.utilities;

import com.shop.db.DatabaseTemplate;
import com.shop.product.Product;
import com.shop.product.ProductRepository;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class ImageDatabaseUtility {
    public void setImagesToDatabase(List<String> images) throws Exception {
        ProductRepository productRepository = new ProductRepository(DatabaseTemplate.getJdbcTemplate());
        for (int i = 0; i < images.size(); i++) {
            Optional<Product> product = productRepository.findById(i + 1L);
            if (product.isPresent() && (product.get().getImage() == null)) {
                byte[] byteImage = ImageUtility.imageToBytes(new File(images.get(i)));
                productRepository.saveImageForProduct(byteImage, i + 1L);

            }
        }
    }
}
