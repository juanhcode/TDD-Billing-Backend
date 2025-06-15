package com.tdd.billing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TddBillingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TddBillingBackendApplication.class, args);
	}

}
