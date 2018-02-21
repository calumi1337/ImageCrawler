package com.kleemo.imagecrawler.resource;

import com.kleemo.imagecrawler.model.DownloadedImage;
import org.apache.commons.io.IOUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ImageCrawlResource {

    private static final int MIN_IMAGE_HEIGHT = 100;

    private static final int MIN_IMAGE_WIDTH = 100;

    @Value("${images.disk.path}")
    private String imagesDiskPath;

    @Autowired
    private UrlValidator urlValidator;

    @RequestMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("Test Successful", HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/image/{imageName:.+}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImageAsResponseEntity(@PathVariable String imageName) {

        byte[] media = null;
        try {
            File imageFile = new File(imagesDiskPath + imageName);
            InputStream in = new FileInputStream(imageFile);
            media = IOUtils.toByteArray(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        return new ResponseEntity<>(media, headers, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping( value = "/crawlUrl", method = RequestMethod.POST)
    public ResponseEntity<List<String>> crawlImages(@RequestParam String url) throws IOException {

        long startTime = System.currentTimeMillis();

        Document doc = Jsoup.connect(url).get();
        Elements imgElements = doc.select("img");
        
        List<String> downloadedImages = imgElements.parallelStream()
                .filter(this::elementIsBigEnough)
                .map(element -> element.absUrl("src"))
                .filter(urlValidator::isValid)
                .map(DownloadedImage::of)
                .filter(this::imageIsPresent)
                .filter(this::imageIsBigEnough)
                .map(this::saveImageToDisk)
                .collect(Collectors.toList());

        System.out.println(String.format("Request took: %d ms", System.currentTimeMillis() - startTime));

        return new ResponseEntity<>(downloadedImages, HttpStatus.CREATED);
    }

    private String saveImageToDisk(DownloadedImage image) {

            String imagePath = imagesDiskPath + image.getImageName();

            try {
                ImageIO.write(image.getImage().get(), image.getFileType(), new File(imagePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image.getImageName();
    }

    private boolean elementIsBigEnough(Element imageElement) {

        if (!elementSizeIsKnown(imageElement)) return true;

        String width = imageElement.attr("width");
        String height = imageElement.attr("height");
        return Double.parseDouble(width.isEmpty() ? "0" : width) > MIN_IMAGE_WIDTH || Double.parseDouble(height.isEmpty() ? "0" : height) > MIN_IMAGE_HEIGHT;
    }

    private boolean imageIsBigEnough(DownloadedImage downloadedImage) {
        return downloadedImage.getImage().get().getHeight() > MIN_IMAGE_HEIGHT || downloadedImage.getImage().get().getWidth() > MIN_IMAGE_WIDTH;
    }

    private boolean elementSizeIsKnown(Element imageElement) {
        return !imageElement.attr("width").isEmpty() || !imageElement.attr("height").isEmpty();
    }

    private boolean imageIsPresent(DownloadedImage image) {
        return image != null && image.getImage() != null && image.getImage().isPresent();
    }

}
