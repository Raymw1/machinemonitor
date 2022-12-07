package com.api.machinemonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class MachinemonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MachinemonitorApplication.class, args);
	}

}
