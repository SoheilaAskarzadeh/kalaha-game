package com.game.kalahaui.exception;

import lombok.Getter;

@Getter
public class ClientException extends RuntimeException{

	private static final long serialVersionUID = 5908763584514681742L;

	public ClientException(String errorMessage) {
		this(errorMessage, null);
	}

	public ClientException(String errorMessage, Throwable cause) {
		super(errorMessage,cause);
	}


}
