package com.game.kalahaui.service.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GameInfo{
	private String gameId;
	private List<PitInfo> pitInfos;
	private Status gameStatus;
	private PlayerNumber playerNumber;
	private Result result;

	public enum Status {
		ACTIVE, DRAW, PLAYER_ONE_WIN, PLAYER_TWO_WIN
	}

	public enum PlayerNumber {
		ONE, TWO
	}
}
