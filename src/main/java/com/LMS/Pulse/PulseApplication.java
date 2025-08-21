package com.LMS.Pulse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PulseApplication {

	public static void main(String[] args) {
		SpringApplication.run(PulseApplication.class, args);
		System.out.println("Feeling Dizzy");
	}

}
