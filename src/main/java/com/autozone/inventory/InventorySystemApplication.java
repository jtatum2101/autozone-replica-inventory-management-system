package com.autozone.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
@SpringBootApplication
@EnableJpaAuditing
public class InventorySystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventorySystemApplication.class, args);
	}

}
