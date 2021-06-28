package com.tripplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableDiscoveryClient
@PropertySource(value={"classpath:messages.properties","classpath:auth.properties"})
public class TripPlannerUserMicroservice {

	public static void main(String[] args) {
		SpringApplication.run(TripPlannerUserMicroservice.class, args);
	}

}
