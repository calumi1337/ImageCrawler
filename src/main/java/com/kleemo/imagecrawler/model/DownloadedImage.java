package com.kleemo.imagecrawler.model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

public class DownloadedImage {

    private String sourceUrl;

    private Optional<BufferedImage> image;

    private DownloadedImage(String sourceUrl, InputStream inputStream) {
        this.sourceUrl = sourceUrl;
        try {
            this.image = Optional.ofNullable(ImageIO.read(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
            this.image = Optional.empty();
        }

    }

    public static DownloadedImage of(String imageUrl) {

        try (InputStream in = new URL(imageUrl).openStream()) {
            return new DownloadedImage(imageUrl, in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public Optional<BufferedImage> getImage() {
        return image;
    }

    public String getImageName() {
        int endOfUrlPath = sourceUrl.lastIndexOf("/") + 1;
        int endOfUrl = sourceUrl.length();
        return sourceUrl.substring(endOfUrlPath, endOfUrl);
    }

    public String getFileType() {
        int fileTypeStart = sourceUrl.lastIndexOf(".") + 1;
        int fileTypeEnd = sourceUrl.length();
        return sourceUrl.substring(fileTypeStart, fileTypeEnd);
    }
}
