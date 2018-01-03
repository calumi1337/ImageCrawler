package com.kleemo.imagecrawler;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ImagecrawlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImagecrawlerApplication.class, args);
	}

	@Bean
	public UrlValidator urlValidatorBean() {
		return new UrlValidator();
	}

}
