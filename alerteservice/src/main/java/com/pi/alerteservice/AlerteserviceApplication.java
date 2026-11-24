package com.pi.alerteservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AlerteserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlerteserviceApplication.class, args);
	}

}
