package com.game.kalahaui.impl;

import com.game.kalaha.spec.dto.GameStatus;
import com.game.kalaha.spec.dto.Player;
import com.game.kalaha.spec.dto.ResultStatus;
import com.game.kalaha.spec.response.GameResponse;
import com.game.kalahaui.client.KalahaClient;
import com.game.kalahaui.service.KalahaService;
import com.game.kalahaui.service.dto.GameInfo.PlayerNumber;
import com.game.kalahaui.service.dto.GameInfo.Status;
import com.game.kalahaui.service.impl.KalahaServiceImpl;
import com.game.kalahaui.service.mapper.GameMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@Import({ KalahaServiceImpl.class, GameMapperImpl.class })
class KalahaServiceImplTest {

	@Autowired
	KalahaService service;
	
	@MockBean
	KalahaClient client;

	@Test
	void createGame_success() {
		when(client.createGame()).thenReturn(createGameResponse());
		var response=service.createGame();
		assertThat(response).isNotNull();
		assertThat(response.getGameId()).isEqualTo("gameId");
		assertThat(response.getGameStatus()).isEqualTo(Status.ACTIVE);
		assertThat(response.getPlayerNumber()).isEqualTo(PlayerNumber.ONE);
		assertThat(response.getResult().getMessage()).isEqualTo("Success");
	}

	@Test
	void play_gameNotFound()  {
		when(client.play(any(),any())).thenReturn(createNotFoundError());
		var response=service.play("game",5);
		assertThat(response).isNotNull();
		assertThat(response.getGameId()).isNull();
		assertThat(response.getGameStatus()).isNull();
		assertThat(response.getPitInfos()).isNull();
		assertThat(response.getPlayerNumber()).isNull();
		assertThat(response.getResult().getMessage()).isEqualTo("No game found");
		assertThat(response.getResult().getStatus()).isEqualTo("4");
		assertThat(response.getGameId()).isNull();
	}

	private GameResponse createGameResponse(){
		var game=new GameResponse();
		game.setGameId("gameId");
		game.setPlayer(Player.ONE);
		game.setStatus(GameStatus.ACTIVE);
		game.setResult(ResultStatus.SUCCESSFUL);
		return game;
	}

	private GameResponse createNotFoundError(){
		var game=new GameResponse();
		game.setResult(ResultStatus.GAME_NOT_FOUND);
		return game;
	}



}