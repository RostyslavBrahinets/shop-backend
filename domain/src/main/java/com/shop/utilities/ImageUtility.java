package com.shop.utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageUtility {
    private ImageUtility() {
    }

    public static byte[] imageToBytes(File file) throws IOException {
        try (
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream()
        ) {
            byte[] buf = new byte[1024];

            for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                bos.write(buf, 0, readNum);
            }

            return bos.toByteArray();
        }
    }
}
