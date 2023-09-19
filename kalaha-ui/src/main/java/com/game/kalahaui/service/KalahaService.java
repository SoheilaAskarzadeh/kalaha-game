package com.game.kalahaui.service;

import com.game.kalahaui.exception.ClientException;
import com.game.kalahaui.service.dto.GameInfo;

public interface KalahaService {

	GameInfo createGame() throws ClientException ;
	GameInfo play(String gameId,Integer pitId) throws ClientException;
}
