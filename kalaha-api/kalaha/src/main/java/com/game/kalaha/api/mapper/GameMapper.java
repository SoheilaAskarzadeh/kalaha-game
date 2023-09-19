package com.game.kalaha.api.mapper;

import com.game.kalaha.service.model.GameResponseModel;
import com.game.kalaha.service.model.PlayRequestModel;
import com.game.kalaha.spec.response.GameResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface GameMapper {

	@Mapping(target = "pits",source = "pits")
	@Mapping(target = "gameId",source = "gameId")
	@Mapping(target = "status",source = "status")
	@Mapping(target = "player",source = "player")
	@Mapping(target = "result", expression = "java(com.game.kalaha.spec.dto.ResultStatus.SUCCESSFUL)")
	GameResponse toGameResponse(GameResponseModel response);

	@Mapping(target = "gameId",source = "gameId")
	@Mapping(target = "houseId",source = "pitId")
	PlayRequestModel toPlayRequestModel(String gameId,Integer pitId);
}
