package com.game.kalaha.service.impl;

import java.util.Arrays;
import java.util.Optional;

import com.game.kalaha.exception.KalahaException;
import com.game.kalaha.model.Game;
import com.game.kalaha.model.Game.Status;
import com.game.kalaha.model.House;
import com.game.kalaha.model.PlayerNumber;
import com.game.kalaha.model.Store;
import com.game.kalaha.repository.GameRepository;
import com.game.kalaha.service.GameService;
import com.game.kalaha.service.mapper.GameServiceMapperImpl;
import com.game.kalaha.service.model.PlayRequestModel;
import org.junit.jupiter.api.Assertions;
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
@Import({ GameServiceMapperImpl.class,GameServiceImpl.class })
class GameServiceImplTest {

	@Autowired
	GameService gameService;

	@MockBean
	GameRepository gameRepository;

	@Test
	void createGame_success(){
		var game=createGame(PlayerNumber.ONE,Status.ACTIVE);
		when(gameRepository.save(any())).thenReturn(game);
		var response=gameService.createGame();
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Status.ACTIVE);
		assertThat(response.getPits()).isNotNull();
		assertThat(response.getPits()).hasSizeGreaterThan(0);
		assertThat(response.getPlayer()).isEqualTo(PlayerNumber.ONE);
	}

	@Test
	void move_gameNotFound(){
		when(gameRepository.findById("gameId")).thenReturn(Optional.empty());
		var request=new PlayRequestModel("gameId",3);
		Assertions.assertThrows(KalahaException.class,()->gameService.move(request));
	}

	@Test
	void move_gameWithInvalidStatus(){
		var game=createGame(PlayerNumber.ONE,Status.DRAW);
		when(gameRepository.findById("gameId")).thenReturn(Optional.of(game));
		var request=new PlayRequestModel("gameId",3);
		var response=gameService.move(request);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(game.getStatus());
		assertThat(response.getPits()).isEqualTo(game.getPits());
		assertThat(response.getPlayer()).isEqualTo(game.getPlayer());
	}

	@Test
	void move_Index_seven_Selected(){
		var game=createGame(PlayerNumber.ONE,Status.ACTIVE);
		when(gameRepository.findById("gameId")).thenReturn(Optional.of(game));
		var request=new PlayRequestModel("gameId",7);
		var response=gameService.move(request);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(game.getStatus());
		assertThat(response.getPlayer()).isEqualTo(game.getPlayer());
	}

	@Test
	void move_Index_fourteen_Selected(){
		var game=createGame(PlayerNumber.TWO,Status.ACTIVE);
		when(gameRepository.findById("gameId")).thenReturn(Optional.of(game));
		var request=new PlayRequestModel("gameId",14);
		var response=gameService.move(request);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(game.getStatus());
		assertThat(response.getPlayer()).isEqualTo(game.getPlayer());
	}

	@Test
	void move_playerIsNull_selectOne_turnPlayer(){
		var game=createGame(null,Status.ACTIVE);
		when(gameRepository.findById("gameId")).thenReturn(Optional.of(game));
		var request=new PlayRequestModel("gameId",2);
		var response=gameService.move(request);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(game.getStatus());
		assertThat(response.getPits()).isNotNull();
		assertThat(response.getPlayer()).isEqualTo(PlayerNumber.TWO);
	}

	@Test
	void move_playerIsNull_selectTwo_turnPlayer(){
		var game=createGame(null,Status.ACTIVE);
		when(gameRepository.findById("gameId")).thenReturn(Optional.of(game));
		var request=new PlayRequestModel("gameId",9);
		var response=gameService.move(request);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(game.getStatus());
		assertThat(response.getPits()).isNotNull();
		assertThat(response.getPlayer()).isEqualTo(PlayerNumber.ONE);
	}

	@Test
	void move_playerOne_invalidPidId(){
		var game=createGame(PlayerNumber.ONE,Status.ACTIVE);
		when(gameRepository.findById("gameId")).thenReturn(Optional.of(game));
		var request=new PlayRequestModel("gameId",9);
		var response=gameService.move(request);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(game.getStatus());
		assertThat(response.getPits()).isNotNull();
		assertThat(response.getPlayer()).isEqualTo(PlayerNumber.ONE);
	}

	@Test
	void move_playerTwo_invalidPidId(){
		var game=createGame(PlayerNumber.TWO,Status.ACTIVE);
		when(gameRepository.findById("gameId")).thenReturn(Optional.of(game));
		var request=new PlayRequestModel("gameId",3);
		var response=gameService.move(request);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(game.getStatus());
		assertThat(response.getPits()).isNotNull();
		assertThat(response.getPlayer()).isEqualTo(PlayerNumber.TWO);
	}

	@Test
	void move_playerOne_sowRight(){
		var game=createGame(PlayerNumber.ONE,Status.ACTIVE);
		when(gameRepository.findById("gameId")).thenReturn(Optional.of(game));
		var request=new PlayRequestModel("gameId",6);
		var response=gameService.move(request);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(game.getStatus());
		assertThat(response.getPits()).hasSizeGreaterThan(0);
		assertThat(response.getPlayer()).isEqualTo(PlayerNumber.TWO);
		var pits= response.getPits();
		assertThat(pits.get(0).getStones()).isEqualTo(6);
		assertThat(pits.get(1).getStones()).isEqualTo(6);
		assertThat(pits.get(2).getStones()).isEqualTo(6);
		assertThat(pits.get(3).getStones()).isEqualTo(6);
		assertThat(pits.get(4).getStones()).isEqualTo(6);
		assertThat(pits.get(5).getStones()).isZero();
		assertThat(pits.get(6).getStones()).isEqualTo(1);
		assertThat(pits.get(7).getStones()).isEqualTo(7);
		assertThat(pits.get(8).getStones()).isEqualTo(7);
		assertThat(pits.get(9).getStones()).isEqualTo(7);
		assertThat(pits.get(10).getStones()).isEqualTo(7);
		assertThat(pits.get(11).getStones()).isEqualTo(7);
		assertThat(pits.get(12).getStones()).isEqualTo(6);
		assertThat(pits.get(13).getStones()).isZero();
	}

	@Test
	void move_playerTwo_sowRight(){
		var game=createGame(PlayerNumber.TWO,Status.ACTIVE);
		when(gameRepository.findById("gameId")).thenReturn(Optional.of(game));
		var request=new PlayRequestModel("gameId",10);
		var response=gameService.move(request);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(game.getStatus());
		assertThat(response.getPits()).hasSizeGreaterThan(1);
		assertThat(response.getPlayer()).isEqualTo(PlayerNumber.ONE);
		var pits= response.getPits();
		assertThat(pits.get(0).getStones()).isEqualTo(7);
		assertThat(pits.get(1).getStones()).isEqualTo(7);
		assertThat(pits.get(2).getStones()).isEqualTo(6);
		assertThat(pits.get(3).getStones()).isEqualTo(6);
		assertThat(pits.get(4).getStones()).isEqualTo(6);
		assertThat(pits.get(5).getStones()).isEqualTo(6);
		assertThat(pits.get(6).getStones()).isZero();
		assertThat(pits.get(7).getStones()).isEqualTo(6);
		assertThat(pits.get(8).getStones()).isEqualTo(6);
		assertThat(pits.get(9).getStones()).isZero();
		assertThat(pits.get(10).getStones()).isEqualTo(7);
		assertThat(pits.get(11).getStones()).isEqualTo(7);
		assertThat(pits.get(12).getStones()).isEqualTo(7);
		assertThat(pits.get(13).getStones()).isEqualTo(1);
	}

	@Test
	void move_handleOpposite(){
		var game=createGame(PlayerNumber.TWO);
		when(gameRepository.findById("gameId")).thenReturn(Optional.of(game));
		var request=new PlayRequestModel("gameId",8);
		var response=gameService.move(request);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Status.ACTIVE);
		assertThat(response.getPits()).hasSizeGreaterThan(0);
		var pits= response.getPits();
		assertThat(pits.get(0).getStones()).isEqualTo(6);
		assertThat(pits.get(1).getStones()).isEqualTo(6);
		assertThat(pits.get(2).getStones()).isEqualTo(6);
		assertThat(pits.get(3).getStones()).isEqualTo(6);
		assertThat(pits.get(4).getStones()).isZero();
		assertThat(pits.get(5).getStones()).isEqualTo(6);
		assertThat(pits.get(6).getStones()).isZero();
		assertThat(pits.get(7).getStones()).isZero();
		assertThat(pits.get(8).getStones()).isZero();
		assertThat(pits.get(9).getStones()).isEqualTo(6);
		assertThat(pits.get(10).getStones()).isEqualTo(6);
		assertThat(pits.get(11).getStones()).isEqualTo(6);
		assertThat(pits.get(12).getStones()).isEqualTo(6);
		assertThat(pits.get(13).getStones()).isEqualTo(7);
	}

	@Test
	void move_handleComplete(){
		var game=createGame();
		when(gameRepository.findById("gameId")).thenReturn(Optional.of(game));
		var request=new PlayRequestModel("gameId",13);
		var response=gameService.move(request);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Status.PLAYER_TWO_WIN);
		assertThat(response.getPits()).hasSizeGreaterThan(1);
		var pits= response.getPits();
		assertThat(pits.get(0).getStones()).isZero();
		assertThat(pits.get(1).getStones()).isZero();
		assertThat(pits.get(2).getStones()).isZero();
		assertThat(pits.get(3).getStones()).isZero();
		assertThat(pits.get(4).getStones()).isZero();
		assertThat(pits.get(5).getStones()).isZero();
		assertThat(pits.get(6).getStones()).isEqualTo(35);
		assertThat(pits.get(7).getStones()).isZero();
		assertThat(pits.get(8).getStones()).isZero();
		assertThat(pits.get(9).getStones()).isZero();
		assertThat(pits.get(10).getStones()).isZero();
		assertThat(pits.get(11).getStones()).isZero();
		assertThat(pits.get(12).getStones()).isZero();
		assertThat(pits.get(13).getStones()).isEqualTo(37);
	}

	private Game createGame(PlayerNumber player, Status status){
		var game=new Game();
		game.setId("gameId");
		game.setStatus(status);
		game.setPlayer(player);
		game.setPits(Arrays.asList(
				new House(1, 6,PlayerNumber.ONE),
				new House(2, 6,PlayerNumber.ONE),
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

	private Game createGame(PlayerNumber player){
		var game=new Game();
		game.setId("gameId");
		game.setStatus(Status.ACTIVE);
		game.setPlayer(player);
		game.setPits(Arrays.asList(
				new House(1, 6,PlayerNumber.ONE),
				new House(2, 6,PlayerNumber.ONE),
				new House(3, 6,PlayerNumber.ONE),
				new House(4, 6,PlayerNumber.ONE),
				new House(5, 6,PlayerNumber.ONE),
				new House(6, 6,PlayerNumber.ONE),
				new Store(7,PlayerNumber.ONE),
				new House(8, 1,PlayerNumber.TWO),
				new House(9, 0,PlayerNumber.TWO),
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
		game.setPlayer(PlayerNumber.TWO);
		Store storePlayerOne=new Store(7,PlayerNumber.ONE);
		storePlayerOne.setStones(28);
		Store storePlayerTwo=new Store(14,PlayerNumber.TWO);
		storePlayerTwo.setStones(36);
		game.setPits(Arrays.asList(
				new House(1, 0,PlayerNumber.ONE),
				new House(2, 0,PlayerNumber.ONE),
				new House(3, 1,PlayerNumber.ONE),
				new House(4, 0,PlayerNumber.ONE),
				new House(5, 4,PlayerNumber.ONE),
				new House(6, 2,PlayerNumber.ONE),
				storePlayerOne,
				new House(8, 0,PlayerNumber.TWO),
				new House(9, 0,PlayerNumber.TWO),
				new House(10, 0,PlayerNumber.TWO),
				new House(11, 0,PlayerNumber.TWO),
				new House(12, 0,PlayerNumber.TWO),
				new House(13, 1,PlayerNumber.TWO),
				storePlayerTwo));

		return game;
	}

}