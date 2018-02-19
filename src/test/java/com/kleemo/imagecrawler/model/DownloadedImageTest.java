package com.kleemo.imagecrawler.model;

import static org.junit.Assert.assertTrue;

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
    
    @Test
    public void testToFail() {
    	assertTrue("This is supposed to fail", false);
    }

}
