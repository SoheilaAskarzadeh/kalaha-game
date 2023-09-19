package com.game.kalaha.spec.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Result implements Serializable {

	private static final long serialVersionUID = -1093687655164702441L;

	private ResultStatus title;
}
