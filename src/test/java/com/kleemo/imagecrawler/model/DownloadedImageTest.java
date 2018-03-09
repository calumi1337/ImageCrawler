package com.kleemo.imagecrawler.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DownloadedImageTest {

    private DownloadedImage cut;
    
    private DownloadedImage cutWithNoFileExtension;

    @Before
    public void setUp() {
        cut = DownloadedImage.of("https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Brillanten.jpg/220px-Brillanten.jpg");
        cutWithNoFileExtension = DownloadedImage.of("https://static1.squarespace.com/static/52a7344ee4b07aa6d0d25831/t/5948eba9b11be1345caf7ad5/1497951173521/?format=1000w");
    }

    @Test
    public void testExtractImageNameFromUrl$jpgImage() {

        String imageName = cut.getImageName();
        Assert.assertEquals("220px-Brillanten.jpg", imageName);
    }
    
    @Test
    public void testGenerateRandomNameWhenNoNameIsGiven() {
    	
    	assertNotNull(cutWithNoFileExtension.getImageName());
    	assertEquals(14 ,cutWithNoFileExtension.getImageName().length());
    }
    
    @Test
    public void testRetrieveImageExtensionOfFile$withExtension() {
    	assertEquals("jpeg", cut.getFileType().get());
    }
    
    @Test
    public void testRetrieveImageExtensionOfFile$withoutExtension() {
        assertEquals("png", cutWithNoFileExtension.getFileType().get());    	
    }
    
    @Test
    public void testGetImageNameReturnsEqualValue() {
    	
    	String firstCall = cutWithNoFileExtension.getImageName();
    	String secondCall = cutWithNoFileExtension.getImageName();
        assertEquals(firstCall, secondCall);    	
    }
    
    @Test
    public void testThisShouldFail() {
    	assertTrue(false);
    }
    
}
