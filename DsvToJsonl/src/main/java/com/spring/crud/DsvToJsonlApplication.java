package com.spring.crud;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DsvToJsonlApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(DsvToJsonlApplication.class, args);
		DsvToJsonlConvert.convert(args);
	}

}
