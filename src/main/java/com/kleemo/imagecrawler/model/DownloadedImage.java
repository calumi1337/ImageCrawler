package com.kleemo.imagecrawler.model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.RandomStringUtils;

public class DownloadedImage {

	private String sourceUrl;

	private Optional<BufferedImage> image;
	
	// jpg is fallback
	private Optional<String> fileType = Optional.of("jpg");
	
	private String imageName;

	private DownloadedImage(String sourceUrl, InputStream inputStream) {
		this.sourceUrl = sourceUrl;
        setImage(inputStream);
        setFileType(sourceUrl);
        setImageName();
	}
	
	private void setImage(InputStream inputStream) {
		
		try {
			this.image = Optional.ofNullable(ImageIO.read(inputStream));
		} catch (IOException e) {
			e.printStackTrace();
			this.image = Optional.empty();
		}
	}
	
    private void setFileType(String urlString) {
    	
    	try {
    		URL url = new URL(urlString);
    		// URLConnection.guessContentTypeFromStream only needs the first 12 bytes, but
    		// just to be safe from future java api enhancements, we'll use a larger number
    		int pushbackLimit = 100;
    		InputStream urlStream = url.openStream();
    		PushbackInputStream pushUrlStream = new PushbackInputStream(urlStream, pushbackLimit);
    		byte [] firstBytes = new byte[pushbackLimit];
    		// download the first initial bytes into a byte array, which we will later pass to 
    		// URLConnection.guessContentTypeFromStream  
    		pushUrlStream.read(firstBytes);
    		// push the bytes back onto the PushbackInputStream so that the stream can be read 
    		// by ImageIO reader in its entirety
    		pushUrlStream.unread(firstBytes);
    		
    		// Pass the initial bytes to URLConnection.guessContentTypeFromStream in the form of a
    		// ByteArrayInputStream, which is mark supported.
    		ByteArrayInputStream bais = new ByteArrayInputStream(firstBytes);
    		String mimeType = URLConnection.guessContentTypeFromStream(bais);
    		
    		if (mimeType == null) return;
    		
    		if (mimeType.startsWith("image/"))
    			fileType = Optional.of(mimeType.substring("image/".length()));
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    private void setImageName() {
		int endOfUrlPath = sourceUrl.lastIndexOf("/") + 1;
		int endOfUrl = sourceUrl.length();
		String nameFromUrlEnding = sourceUrl.substring(endOfUrlPath, endOfUrl);
		imageName = isValidImageName(nameFromUrlEnding) ? nameFromUrlEnding : generateRandomName();
    }
    
	private String generateRandomName() {
		return RandomStringUtils.random(10, true, false) + "." + fileType.get();
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
		return imageName;
	}
	
	private boolean isValidImageName(String name) {
		
		if (name == null || !name.contains(".")) return false;
		
		return true;
	}
	
	public Optional<String> getFileType() {
		return fileType;
	}
}
