package com.mapfre.tron.api.swaggergen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SwaggergenApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwaggergenApplication.class, args);
	}

}
