package com.game.kalaha.api;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.game.kalaha.api.mapper.GameMapper;
import com.game.kalaha.service.GameService;
import com.game.kalaha.spec.response.GameResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("/kalaha/games")
@AllArgsConstructor
public class KalahaResource {

	private final GameMapper mapper;
	private final GameService gameService;

	@PostMapping
	GameResponse createGame(){
		logger.info("start new kalaha game");
		return mapper.toGameResponse(gameService.createGame());
	}
	@PutMapping("/{gameId}/pits/{pitId}")
	public GameResponse play(@PathVariable @NotBlank String gameId,
			@PathVariable @Min(1) @Max(14) Integer pitId){
		logger.info("play gameId: {}, selected Pit is: {}",gameId,pitId);
		var response=gameService.move(mapper.toPlayRequestModel(gameId,pitId));
		return mapper.toGameResponse(response);
	}

}
