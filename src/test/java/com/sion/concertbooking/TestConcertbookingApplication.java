package com.sion.concertbooking;

import org.springframework.boot.SpringApplication;

public class TestConcertbookingApplication {

	public static void main(String[] args) {
		SpringApplication.from(ConcertbookingApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
