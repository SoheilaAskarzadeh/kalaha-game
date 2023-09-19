package com.game.kalaha;

import com.game.kalaha.api.exception.KalahaExceptionHandler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Import(KalahaExceptionHandler.class)
@SpringBootApplication
@EnableDiscoveryClient
@EnableMongoRepositories
public class KalahaApplication {

	public static void main(String[] args) {
		SpringApplication.run(KalahaApplication.class, args);
	}
}
