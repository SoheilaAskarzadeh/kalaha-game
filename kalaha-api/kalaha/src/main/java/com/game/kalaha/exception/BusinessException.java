package com.game.kalaha.exception;

import com.game.kalaha.spec.dto.ResultStatus;
import lombok.Getter;

public abstract class BusinessException extends RuntimeException {
	private static final long serialVersionUID = -4274668028885263244L;

	@Getter
	private final ResultStatus resultStatus;

	protected BusinessException(String message, Throwable cause, ResultStatus resultStatus) {
		super(message, cause);
		this.resultStatus = resultStatus;
	}
}
