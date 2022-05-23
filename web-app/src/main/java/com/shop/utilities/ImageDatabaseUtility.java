package com.shop.utilities;

import com.shop.dao.ProductDao;
import com.shop.models.Product;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class ImageDatabaseUtility {
    private final ProductDao productDao = new ProductDao();

    public void setImagesToDatabase(List<String> images) throws Exception {
        for (int i = 0; i < images.size(); i++) {
            Optional<Product> product = productDao.getProduct(i + 1);
            if (product.isPresent()) {
                if (product.get().getImage() == null) {
                    byte[] byteImage = ImageUtility.imageToBytes(new File(images.get(i)));
                    productDao.addImage(byteImage, i + 1);
                }
            }
        }
    }
}
