package com.example.literAlura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.literAlura.client.ApiClient;
import com.example.literAlura.service.AuthorService;
import com.example.literAlura.service.BookService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class LiterAluraApplication implements CommandLineRunner {

	@Autowired
	private BookService livroService;

	@Autowired
	private AuthorService autorService;

	@Autowired
	private ApiClient apiClient;

	public static void main(String[] args) {
		SpringApplication.run(LiterAluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// displayMenu();
	}
}
