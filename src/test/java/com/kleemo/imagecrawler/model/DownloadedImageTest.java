package com.kleemo.imagecrawler.model;

import com.kleemo.imagecrawler.resource.ImageCrawlResource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DownloadedImageTest {

    private DownloadedImage cut;

    @Before
    public void setUp() {
        cut = DownloadedImage.of("https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Brillanten.jpg/220px-Brillanten.jpg");
    }

    @Test
    public void testExtractImageNameFromUrl$jpgImage() {

        String imageName = cut.getImageName();
        Assert.assertEquals("220px-Brillanten.jpg", imageName);
    }

}
