package com.game.kalaha.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResultStatus {
	SUCCESSFUL(0, "Success"),FAIELD(1,"Failer"),INVALID_PARAMETER(2,"Inavalid Parameters"),
	UNKNOWN(3,"Unkown Error"),	GAME_NOT_FOUND(4,"The Game Not Found"),
	INVALID_PIT_INDEX(5, "Pid Index Is Not Valid");
	private final int status;
	private final String description;

}
