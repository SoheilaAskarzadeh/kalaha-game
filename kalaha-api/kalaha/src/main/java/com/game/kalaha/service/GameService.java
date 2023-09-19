package com.game.kalaha.service;

import com.game.kalaha.exception.BusinessException;
import com.game.kalaha.service.model.GameResponseModel;
import com.game.kalaha.service.model.PlayRequestModel;

public interface GameService {

	GameResponseModel createGame() throws BusinessException;

	GameResponseModel move(PlayRequestModel request) throws BusinessException;
}
