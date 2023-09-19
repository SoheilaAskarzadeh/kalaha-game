package com.game.kalaha.model;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.constraints.NotNull;

import com.game.kalaha.exception.KalahaException;
import com.game.kalaha.spec.dto.ResultStatus;
import com.game.kalaha.util.KalahaConstant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@ToString
@Getter
@Setter
@Document(collection = "games")
public class Game implements Serializable {

	private static final long serialVersionUID = 4721248378491525692L;

	@Id
	private String id;

	@NotNull
	private transient List<Pit> pits;

	@NotNull
	private PlayerNumber player;

	@Transient
	private int currentPitIndex;

	private Status status;

	public Game() {
		this(KalahaConstant.STONES);
	}

	public Game(int pitStones) {
		initializePitsAndHouses(pitStones);
	}


	public enum Status {
		ACTIVE, DRAW, PLAYER_ONE_WIN, PLAYER_TWO_WIN
	}
	public Pit getPit(Integer pitIndex) throws KalahaException {
		try {
			return this.pits.get(pitIndex - 1);
		} catch (IndexOutOfBoundsException e) {
			throw new KalahaException("Invalid pit index: " + pitIndex, ResultStatus.INVALID_PIT_INDEX);
		}
	}

	public List<Pit> getPits(PlayerNumber owner){
		return this.getPits().stream().filter(pit -> pit.getOwner().equals(owner) ).collect(Collectors.toList());
	}

	private void initializePitsAndHouses(int pitStones) {
		this.pits = IntStream.rangeClosed(0, KalahaConstant.STONES)
				.mapToObj(i -> i == KalahaConstant.STONES ? new Store(i,  PlayerNumber.ONE) : new House(i, pitStones, PlayerNumber.ONE))
				.collect(Collectors.toList());

		this.pits.addAll(IntStream.rangeClosed(KalahaConstant.PLAYER_ONE_STORE, KalahaConstant.TOTAL_PITS -1)
				.mapToObj(i -> i == KalahaConstant.TOTAL_PITS -1 ? new Store(i, PlayerNumber.TWO) : new House(i, pitStones, PlayerNumber.TWO))
				.collect(Collectors.toList()));
	}

}