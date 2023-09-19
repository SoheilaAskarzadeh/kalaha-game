package com.game.kalahaui.client;


import com.game.kalaha.spec.response.GameResponse;
import com.game.kalahaui.exception.ClientErrorDecoder;
import com.game.kalahaui.exception.ClientException;
import feign.codec.ErrorDecoder;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Component
@FeignClient(name="kalaha-api")
public interface KalahaClient {

	@PostMapping("/kalaha/games")
	GameResponse createGame() throws ClientException;


	@PutMapping("/kalaha/games/{gameId}/pits/{pitId}")
	GameResponse play(@PathVariable @NotBlank String gameId,
			@PathVariable @Min(1) @Max(14) Integer pitId) throws ClientException;

	@Configuration
	class FeignClientConfiguration{
		@Bean
		public ErrorDecoder errorDecoder(){
			return new ClientErrorDecoder();
		}
	}
}
