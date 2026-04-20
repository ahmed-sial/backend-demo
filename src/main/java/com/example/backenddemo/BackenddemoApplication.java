package com.example.backenddemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BackenddemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackenddemoApplication.class, args);
	}

}
