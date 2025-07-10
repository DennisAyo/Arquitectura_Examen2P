package com.banquito.analisis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AnalisisApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnalisisApplication.class, args);
	}

}
