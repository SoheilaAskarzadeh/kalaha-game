package com.game.kalaha.spec.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResultStatus {
	SUCCESSFUL(0, "Success"),FAILED(1,"An error happen during play the game"),INVALID_PARAMETER(2,"Invalid parameters"),
	UNKNOWN(3,"Unknown error happen during play the game"),	GAME_NOT_FOUND(4,"No game found"),
	INVALID_PIT_INDEX(5, "Pid Index Is Not Valid");
	private final int status;
	private final String description;

}
