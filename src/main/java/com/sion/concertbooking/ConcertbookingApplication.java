package com.sion.concertbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ConcertbookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConcertbookingApplication.class, args);
	}

}
