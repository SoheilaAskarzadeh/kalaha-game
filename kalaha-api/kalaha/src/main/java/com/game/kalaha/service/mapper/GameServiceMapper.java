package com.game.kalaha.service.mapper;

import com.game.kalaha.model.Game;
import com.game.kalaha.service.model.GameResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface GameServiceMapper {

	@Mapping(target = "gameId" , source = "id")
	GameResponseModel toGameResponseModel(Game game);
}
