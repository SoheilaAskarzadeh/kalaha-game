package com.game.kalaha.exception;

import com.game.kalaha.spec.dto.ResultStatus;

public class KalahaException extends BusinessException {
	private static final long serialVersionUID = -1833551034637125950L;

	public KalahaException(String message, ResultStatus status) {
		super(message,null,status);
	}
}