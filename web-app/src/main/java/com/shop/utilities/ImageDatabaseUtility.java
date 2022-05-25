package com.shop.utilities;

import com.shop.dao.ProductDao;
import com.shop.db.DatabaseTemplate;
import com.shop.models.Product;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class ImageDatabaseUtility {
    public void setImagesToDatabase(List<String> images) throws Exception {
        ProductDao productDao = new ProductDao(DatabaseTemplate.getJdbcTemplate());
        for (int i = 0; i < images.size(); i++) {
            Optional<Product> product = productDao.findById(i + 1);
            if (product.isPresent()) {
                if (product.get().getImage() == null) {
                    byte[] byteImage = ImageUtility.imageToBytes(new File(images.get(i)));
                    productDao.saveImageForProduct(byteImage, i + 1);
                }
            }
        }
    }
}
