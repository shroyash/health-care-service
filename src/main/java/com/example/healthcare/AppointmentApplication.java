package com.example.healthcare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.healthcare.feign")
@EnableJpaAuditing
public class AppointmentApplication {
	public static void main(String[] args) {
		SpringApplication.run(AppointmentApplication.class, args);
	}
}
