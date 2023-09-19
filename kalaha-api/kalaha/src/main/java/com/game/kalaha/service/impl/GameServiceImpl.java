package com.game.kalaha.service.impl;

import java.util.List;
import java.util.stream.IntStream;

import com.game.kalaha.exception.BusinessException;
import com.game.kalaha.exception.KalahaException;
import com.game.kalaha.model.Game;
import com.game.kalaha.model.Game.Status;
import com.game.kalaha.model.Pit;
import com.game.kalaha.model.PlayerNumber;
import com.game.kalaha.repository.GameRepository;
import com.game.kalaha.service.GameService;
import com.game.kalaha.service.mapper.GameServiceMapper;
import com.game.kalaha.service.model.GameResponseModel;
import com.game.kalaha.service.model.PlayRequestModel;
import com.game.kalaha.spec.dto.ResultStatus;
import com.game.kalaha.util.KalahaConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

	private final GameRepository repository;
	private final GameServiceMapper mapper;

	@Override
	public GameResponseModel createGame() throws BusinessException {
		Game game = createGameWithInitialStones();
		return mapper.toGameResponseModel(game);
	}

	private Game createGameWithInitialStones() {
		Game game = new Game(KalahaConstant.STONES);
		game.setPlayer(PlayerNumber.ONE);
		game.setStatus(Status.ACTIVE);
		repository.save(game);
		logger.info("game: {} is created",game);
		return game;
	}

	@Override
	public GameResponseModel move(PlayRequestModel request) throws BusinessException {
		var game=findGame(request.getGameId());
		if(game.getStatus().equals(Status.ACTIVE)){
			var updatedGame=sow(game,request.getHouseId());
			if(updatedGame!=null){
				updatedGame = repository.save(updatedGame);
			}
			return mapper.toGameResponseModel(updatedGame != null ? updatedGame : game);
		}
		return mapper.toGameResponseModel(game);
	}

	private Game findGame(String id) {
		return repository.findById(id)
				.orElseThrow(() -> new KalahaException("Game not found", ResultStatus.GAME_NOT_FOUND));
	}

	private Game sow(Game game, int pitId) {
		logger.info("sowing from pitIndex: {}",pitId);
		// check index of store, player can not select store's stones
		if (pitId == KalahaConstant.PLAYER_ONE_STORE || pitId ==KalahaConstant.PLAYER_TWO_STORE) {
			return null;
		}
		// if player is null set player based on pitId
		if (game.getPlayer() == null) {
			game.setPlayer(pitId < KalahaConstant.PLAYER_ONE_STORE ? PlayerNumber.ONE : PlayerNumber.TWO);
		}
		// playerOne can select pits between 1 and 6 and playerTwo can select pits between 8 and 13
		else if ((game.getPlayer() == PlayerNumber.ONE && pitId > KalahaConstant.PLAYER_ONE_STORE)
				|| (game.getPlayer() == PlayerNumber.TWO && pitId < KalahaConstant.PLAYER_ONE_STORE)) {
			return null;
		}

		Pit selectedPit = game.getPit(pitId);
		int stones = selectedPit.getStones();
		// nothing happen if player select empty pit
		if (stones == 0) {
			return null;
		}

		selectedPit.setStones(0);
		game.setCurrentPitIndex(pitId);

		IntStream.range(0, stones).forEach(index -> sowRight(game, index == stones - 1));
		// set player's turn
		if (game.getStatus().equals(Status.ACTIVE) && game.getCurrentPitIndex() !=KalahaConstant.PLAYER_ONE_STORE && game.getCurrentPitIndex() != KalahaConstant.PLAYER_TWO_STORE) {
			game.setPlayer(nextTurn(game.getPlayer()));
		}
		handleGameCompletion(game);
		return game;
	}
	private void sowRight(Game game, boolean lastStone) {
		int currentPitIndex = (game.getCurrentPitIndex() % KalahaConstant.TOTAL_PITS) + 1;
		PlayerNumber playerTurn = game.getPlayer();
		// Skip opponent's player's store
		if ((currentPitIndex == KalahaConstant.PLAYER_ONE_STORE && playerTurn == PlayerNumber.TWO)
				|| (currentPitIndex == KalahaConstant.PLAYER_TWO_STORE && playerTurn == PlayerNumber.ONE)) {
			currentPitIndex = (currentPitIndex % KalahaConstant.TOTAL_PITS) + 1;
		}

		game.setCurrentPitIndex(currentPitIndex);
		Pit targetPit = game.getPit(currentPitIndex);

		if (!lastStone || currentPitIndex == KalahaConstant.PLAYER_ONE_STORE || currentPitIndex == KalahaConstant.PLAYER_TWO_STORE) {
			targetPit.sow();
			return ;
		}

		Pit oppositePit = game.getPit(KalahaConstant.PLAYER_TWO_STORE - currentPitIndex);
		// handle opposite's pit's rule
		if (Boolean.TRUE.equals(targetPit.isEmpty()) && Boolean.FALSE.equals(oppositePit.isEmpty())) {
			logger.info("handle opposite rule for gameId: {} , pitIndex: {}",game.getId(),currentPitIndex);
			int oppositeStones = oppositePit.getStones();
			oppositePit.clear();
			int pitHouseIndex = currentPitIndex < KalahaConstant.PLAYER_ONE_STORE ? KalahaConstant.PLAYER_ONE_STORE : KalahaConstant.PLAYER_TWO_STORE;
			Pit pitHouse = game.getPit(pitHouseIndex);
			pitHouse.addStones(oppositeStones + 1);
			return ;
		}

		targetPit.sow();
	}
	private void handleGameCompletion(Game game) {
		logger.info("checking game status for gameId: {}", game.getId());

		// Check if either player has no stones in their pits
		List<Pit> playerOnePits = game.getPits().subList(0, KalahaConstant.PLAYER_ONE_STORE-1);
		List<Pit> playerTwoPits = game.getPits().subList(KalahaConstant.PLAYER_ONE_STORE , KalahaConstant.TOTAL_PITS - 1);

		boolean playerOneEmpty = playerOnePits.stream().allMatch(Pit::isEmpty);
		boolean playerTwoEmpty = playerTwoPits.stream().allMatch(Pit::isEmpty);

		if (playerOneEmpty || playerTwoEmpty) {
			collect(game);

			// Determine the game status based on stone counts
			int playerOneStoreStones = game.getPit(KalahaConstant.PLAYER_ONE_STORE).getStones();
			int playerTwoStoreStones = game.getPit(KalahaConstant.PLAYER_TWO_STORE).getStones();

			if (playerOneStoreStones == playerTwoStoreStones) {
				game.setStatus(Status.DRAW);
			} else if (playerOneStoreStones > playerTwoStoreStones) {
				game.setStatus(Status.PLAYER_ONE_WIN);
			} else {
				game.setStatus(Status.PLAYER_TWO_WIN);
			}
			logger.info("game: {} status is: {}",game.getId(), game.getStatus());
		}
	}


	private PlayerNumber nextTurn(PlayerNumber currentTurn) {
		return (currentTurn == PlayerNumber.ONE) ? PlayerNumber.TWO : PlayerNumber.ONE;
	}

	private void collect(Game game) {
		logger.debug("collect all stones in each house for game: {}", game.getId());
		PlayerNumber currentPlayer = nextTurn(game.getPlayer());

		List<Pit> currentPlayerPits = game.getPits(currentPlayer);
		int remainingStones = currentPlayerPits.stream()
				.mapToInt(Pit::getStones)
				.sum();
		// collect remain stones
		if (currentPlayer == PlayerNumber.ONE) {
			game.getPit(KalahaConstant.PLAYER_ONE_STORE).setStones(remainingStones);
		} else {
			game.getPit(KalahaConstant.PLAYER_TWO_STORE).setStones(remainingStones);
		}
		// clear all remain houses
		game.getPits().stream()
				.filter(Pit::allowable)
				.forEach(Pit::clear);

	}
}
