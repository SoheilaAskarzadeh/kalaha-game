package com.game.kalaha.api;

import java.util.Arrays;

import com.game.kalaha.model.Game;
import com.game.kalaha.model.Game.Status;
import com.game.kalaha.model.House;
import com.game.kalaha.model.PlayerNumber;
import com.game.kalaha.model.Store;
import com.game.kalaha.repository.GameRepository;
import com.game.kalaha.service.GameService;
import com.game.kalaha.spec.dto.GameStatus;
import com.game.kalaha.spec.dto.Player;
import com.game.kalaha.spec.dto.ResultStatus;
import com.game.kalaha.spec.response.GameResponse;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class KalahaResourceIT {

	@Autowired
	private GameService gameService;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private GameRepository gameRepository;

	@LocalServerPort
	int port;

	@Test
	void createGame_success(){
		var url = String.format("http://localhost:%s/kalaha/games", port);
		var response = restTemplate.exchange(url, HttpMethod.POST, null, GameResponse.class);
		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getGameId()).isNotNull();
		assertThat(response.getBody().getStatus()).isEqualTo(GameStatus.ACTIVE);
		assertThat(response.getBody().getPits()).isNotNull();
		assertThat(response.getBody().getPits()).hasSize(14);
		assertThat(response.getBody().getResult().getTitle()).isEqualTo(ResultStatus.SUCCESSFUL);
	}


	@Test
	void play_invalidArgument(){
		var url = String.format("http://localhost:%s/kalaha/games/%s/pits/%s", port,null,30);
		var response = restTemplate.exchange(url,HttpMethod.PUT,null, GameResponse.class);
		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getResult().getTitle()).isEqualTo(ResultStatus.FAILED);
		assertThat(response.getBody().getResult().getTitle()).isNotNull();
		assertThat(response.getBody().getGameId()).isNull();
		assertThat(response.getBody().getPlayer()).isNull();
		assertThat(response.getBody().getPits()).isNull();
		assertThat(response.getBody().getStatus()).isNull();
	}
	@Test
	void play_gameNotfound() {
		var url = String.format("http://localhost:"+port+"/kalaha/games/newGame/pits/"+4);
		var response=restTemplate.exchange(url, HttpMethod.PUT,null, GameResponse.class);
		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getResult().getTitle()).isEqualTo(ResultStatus.GAME_NOT_FOUND);
		assertThat(response.getBody().getGameId()).isNull();
		assertThat(response.getBody().getPlayer()).isNull();
		assertThat(response.getBody().getPits()).isNull();
		assertThat(response.getBody().getStatus()).isNull();
	}

	@Test
	void play_gameFinished(){
		var game=createGame(PlayerNumber.ONE,Status.DRAW);
		gameRepository.save(game);
		var url = String.format("http://localhost:%s/kalaha/games/%s/pits/%s", port,"gameId",4);
		var response=restTemplate.exchange(url, HttpMethod.PUT,null, GameResponse.class);
		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getResult().getTitle()).isEqualTo(ResultStatus.SUCCESSFUL);
		assertThat(response.getBody().getGameId()).isEqualTo("gameId");
		assertThat(response.getBody().getPits()).isNotNull();
		assertThat(response.getBody().getStatus()).isEqualTo(GameStatus.DRAW);
		gameRepository.delete(game);
	}
	@Test
	void play_invalidPit(){
		var game=createGame(PlayerNumber.ONE,Status.ACTIVE);
		gameRepository.save(game);
		var url = String.format("http://localhost:%s/kalaha/games/%s/pits/%s", port,"gameId",9);
		var response = restTemplate.exchange(url, HttpMethod.PUT,null, GameResponse.class);
		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getGameId()).isEqualTo("gameId");
		assertThat(response.getBody().getStatus()).isEqualTo(GameStatus.ACTIVE);
		var pits=response.getBody().getPits();
		assertThat(pits).hasSize(14);
		assertThat(pits.get(0).getStones()).isEqualTo(6);
		assertThat(pits.get(1).getStones()).isEqualTo(6);
		assertThat(pits.get(2).getStones()).isEqualTo(6);
		assertThat(pits.get(3).getStones()).isEqualTo(6);
		assertThat(pits.get(4).getStones()).isEqualTo(6);
		assertThat(pits.get(5).getStones()).isEqualTo(6);
		assertThat(pits.get(6).getStones()).isZero();
		assertThat(pits.get(7).getStones()).isEqualTo(6);
		assertThat(pits.get(8).getStones()).isEqualTo(6);
		assertThat(pits.get(9).getStones()).isEqualTo(6);
		assertThat(pits.get(10).getStones()).isEqualTo(6);
		assertThat(pits.get(11).getStones()).isEqualTo(6);
		assertThat(pits.get(12).getStones()).isEqualTo(6);
		assertThat(pits.get(13).getStones()).isZero();
		assertThat(response.getBody().getPlayer()).isEqualTo(Player.ONE);
		gameRepository.delete(game);
	}
	@Test
	void play_playerOne_turn(){
		var game=createGame(PlayerNumber.ONE,Status.ACTIVE);
		gameRepository.save(game);
		var url = String.format("http://localhost:%s/kalaha/games/%s/pits/%s", port,"gameId",4);
		var response = restTemplate.exchange(url, HttpMethod.PUT,null, GameResponse.class);
		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getGameId()).isEqualTo("gameId");
		assertThat(response.getBody().getStatus()).isEqualTo(GameStatus.ACTIVE);
		var pits=response.getBody().getPits();
		assertThat(pits).hasSize(14);
		assertThat(pits.get(3).getStones()).isZero();
		assertThat(pits.get(4).getStones()).isEqualTo(7);
		assertThat(pits.get(5).getStones()).isEqualTo(7);
		assertThat(pits.get(6).getStones()).isEqualTo(1);
		assertThat(pits.get(7).getStones()).isEqualTo(7);
		assertThat(pits.get(8).getStones()).isEqualTo(7);
		assertThat(pits.get(9).getStones()).isEqualTo(7);
		assertThat(response.getBody().getPlayer()).isEqualTo(Player.TWO);
		gameRepository.delete(game);
	}
	@Test
	void play_playerOne_putLastStoneInStore(){
		var game=createGame(PlayerNumber.ONE,Status.ACTIVE);
		gameRepository.save(game);
		var url = String.format("http://localhost:%s/kalaha/games/%s/pits/%s", port,"gameId",1);
		var response = restTemplate.exchange(url, HttpMethod.PUT,null, GameResponse.class);
		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getGameId()).isEqualTo("gameId");
		assertThat(response.getBody().getStatus()).isEqualTo(GameStatus.ACTIVE);
		assertThat(response.getBody().getPits()).hasSize(14);
		var pits=response.getBody().getPits();
		assertThat(pits.get(0).getStones()).isZero();
		assertThat(pits.get(1).getStones()).isEqualTo(7);
		assertThat(pits.get(2).getStones()).isEqualTo(7);
		assertThat(pits.get(3).getStones()).isEqualTo(7);
		assertThat(pits.get(4).getStones()).isEqualTo(7);
		assertThat(pits.get(5).getStones()).isEqualTo(7);
		assertThat(pits.get(6).getStones()).isEqualTo(1);
		assertThat(response.getBody().getPlayer()).isEqualTo(Player.ONE);
		gameRepository.delete(game);
	}
	@Test
	void play_playerTwo_turn(){
		var game=createGame(PlayerNumber.TWO,Status.ACTIVE);
		gameRepository.save(game);
		var url = String.format("http://localhost:%s/kalaha/games/%s/pits/%s", port,"gameId",9);
		var response = restTemplate.exchange(url, HttpMethod.PUT,null, GameResponse.class);
		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getGameId()).isEqualTo("gameId");
		assertThat(response.getBody().getStatus()).isEqualTo(GameStatus.ACTIVE);
		var pits=response.getBody().getPits();
		assertThat(pits).hasSize(14);
		assertThat(pits.get(8).getStones()).isZero();
		assertThat(pits.get(9).getStones()).isEqualTo(7);
		assertThat(pits.get(10).getStones()).isEqualTo(7);
		assertThat(pits.get(11).getStones()).isEqualTo(7);
		assertThat(pits.get(12).getStones()).isEqualTo(7);
		assertThat(pits.get(13).getStones()).isEqualTo(1);
		assertThat(pits.get(0).getStones()).isEqualTo(7);
		assertThat(response.getBody().getPlayer()).isEqualTo(Player.ONE);
		gameRepository.delete(game);
	}
	@Test
	void play_playerTwo_putLastStoneInStore(){
		var game=createGame(PlayerNumber.TWO,Status.ACTIVE);
		gameRepository.save(game);
		var url = String.format("http://localhost:%s/kalaha/games/%s/pits/%s", port,"gameId",8);
		var response = restTemplate.exchange(url, HttpMethod.PUT,null, GameResponse.class);
		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getGameId()).isEqualTo("gameId");
		assertThat(response.getBody().getStatus()).isEqualTo(GameStatus.ACTIVE);
		var pits=response.getBody().getPits();
		assertThat(pits).hasSize(14);
		assertThat(pits.get(7).getStones()).isZero();
		assertThat(pits.get(8).getStones()).isEqualTo(7);
		assertThat(pits.get(9).getStones()).isEqualTo(7);
		assertThat(pits.get(10).getStones()).isEqualTo(7);
		assertThat(pits.get(11).getStones()).isEqualTo(7);
		assertThat(pits.get(12).getStones()).isEqualTo(7);
		assertThat(pits.get(13).getStones()).isEqualTo(1);
		assertThat(response.getBody().getPlayer()).isEqualTo(Player.TWO);
		gameRepository.delete(game);
	}

	@Test
	void play_collectOppositePit(){
		var game=createGame(PlayerNumber.ONE);
		gameRepository.save(game);
		var url = String.format("http://localhost:%s/kalaha/games/%s/pits/%s", port,"gameId",1);
		var response = restTemplate.exchange(url, HttpMethod.PUT,null, GameResponse.class);
		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getGameId()).isEqualTo("gameId");
		assertThat(response.getBody().getStatus()).isEqualTo(GameStatus.ACTIVE);
		var pits=response.getBody().getPits();
		assertThat(pits).hasSize(14);
		assertThat(pits.get(0).getStones()).isZero();
		assertThat(pits.get(1).getStones()).isZero();
		assertThat(pits.get(6).getStones()).isEqualTo(7);
		assertThat(response.getBody().getPlayer()).isEqualTo(Player.TWO);
		gameRepository.delete(game);
	}

	@Test
	void play_finishGame(){
		var game=createGame();
		gameRepository.save(game);
		var url = String.format("http://localhost:%s/kalaha/games/%s/pits/%s", port,"gameId",6);
		var response = restTemplate.exchange(url, HttpMethod.PUT,null, GameResponse.class);
		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getGameId()).isEqualTo("gameId");
		assertThat(response.getBody().getStatus()).isEqualTo(GameStatus.PLAYER_ONE_WIN);
		var pits=response.getBody().getPits();
		assertThat(pits).hasSize(14);
		assertThat(pits.get(0).getStones()).isZero();
		assertThat(pits.get(1).getStones()).isZero();
		assertThat(pits.get(2).getStones()).isZero();
		assertThat(pits.get(3).getStones()).isZero();
		assertThat(pits.get(4).getStones()).isZero();
		assertThat(pits.get(5).getStones()).isZero();
		assertThat(pits.get(6).getStones()).isEqualTo(38);
		assertThat(pits.get(7).getStones()).isZero();
		assertThat(pits.get(8).getStones()).isZero();
		assertThat(pits.get(9).getStones()).isZero();
		assertThat(pits.get(10).getStones()).isZero();
		assertThat(pits.get(11).getStones()).isZero();
		assertThat(pits.get(12).getStones()).isZero();
		assertThat(pits.get(13).getStones()).isEqualTo(34);
		gameRepository.delete(game);
	}

	private Game createGame(PlayerNumber player,Status status){
		var game=new Game();
		game.setId("gameId");
		game.setStatus(status);
		game.setPlayer(player);
		game.setPits(Arrays.asList(
				new House(1, 6,PlayerNumber.ONE),
				new House(2, 6,PlayerNumber.ONE),
				new House(3, 6,PlayerNumber.ONE),
				new House(4, 6,PlayerNumber.ONE),
				new House(5,6,PlayerNumber.ONE),
				new House(6,6,PlayerNumber.ONE),
				new Store(7,PlayerNumber.ONE),
				new House(8, 6,PlayerNumber.TWO),
				new House(9, 6,PlayerNumber.TWO),
				new House(10, 6,PlayerNumber.TWO),
				new House(11, 6,PlayerNumber.TWO),
				new House(12, 6,PlayerNumber.TWO),
				new House(13, 6,PlayerNumber.TWO),
				new Store(14,PlayerNumber.TWO)));
		return game;
	}

	private Game createGame(PlayerNumber player){
		var game=new Game();
		game.setId("gameId");
		game.setStatus(Status.ACTIVE);
		game.setPlayer(player);
		game.setPits(Arrays.asList(
				new House(1, 1,PlayerNumber.ONE),
				new House(2, 0,PlayerNumber.ONE),
				new House(3, 6,PlayerNumber.ONE),
				new House(4, 6,PlayerNumber.ONE),
				new House(5, 6,PlayerNumber.ONE),
				new House(6, 6,PlayerNumber.ONE),
				new Store(7,PlayerNumber.ONE),
				new House(8, 6,PlayerNumber.TWO),
				new House(9, 6,PlayerNumber.TWO),
				new House(10, 6,PlayerNumber.TWO),
				new House(11, 6,PlayerNumber.TWO),
				new House(12, 6,PlayerNumber.TWO),
				new House(13, 6,PlayerNumber.TWO),
				new Store(14,PlayerNumber.TWO)));
		return game;
	}

	private Game createGame(){
		var game=new Game();
		game.setId("gameId");
		game.setStatus(Status.ACTIVE);
		game.setPlayer(PlayerNumber.ONE);
		Store storePlayerOne=new Store(7,PlayerNumber.ONE);
		storePlayerOne.setStones(37);
		Store storePlayerTwo=new Store(14,PlayerNumber.TWO);
		storePlayerTwo.setStones(28);
		game.setPits(Arrays.asList(
				new House(1, 0,PlayerNumber.ONE),
				new House(2, 0,PlayerNumber.ONE),
				new House(3, 0,PlayerNumber.ONE),
				new House(4, 0,PlayerNumber.ONE),
				new House(5, 0,PlayerNumber.ONE),
				new House(6, 1,PlayerNumber.ONE),
				storePlayerOne,
				new House(8, 0,PlayerNumber.TWO),
				new House(9, 4,PlayerNumber.TWO),
				new House(10, 0,PlayerNumber.TWO),
				new House(11, 0,PlayerNumber.TWO),
				new House(12, 1,PlayerNumber.TWO),
				new House(13, 1,PlayerNumber.TWO),
				storePlayerTwo));
		return game;
	}
}