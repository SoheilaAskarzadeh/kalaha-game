package com.game.kalahaui.service.impl;

import com.game.kalahaui.client.KalahaClient;
import com.game.kalahaui.exception.ClientException;
import com.game.kalahaui.service.KalahaService;
import com.game.kalahaui.service.dto.GameInfo;
import com.game.kalahaui.service.mapper.GameMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KalahaServiceImpl implements KalahaService {

	private final KalahaClient client;
	private final GameMapper mapper;

	@Override
	public GameInfo createGame() throws ClientException {
		return mapper.toGameInfo(client.createGame());
	}

	@Override
	public GameInfo play(String gameId, Integer pitId) throws ClientException{
		return mapper.toGameInfo(client.play(gameId,pitId));
	}
}
