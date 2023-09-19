package com.game.kalahaui.service.mapper;

import com.game.kalaha.spec.dto.Pit;
import com.game.kalaha.spec.response.GameResponse;
import com.game.kalahaui.service.dto.GameInfo;
import com.game.kalahaui.service.dto.PitInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


@Mapper(componentModel = "spring")
public interface GameMapper {

	@Mapping(target = "gameId" , source = "gameId")
	@Mapping(target = "pitInfos" , source = "pits" ,qualifiedByName = "pitInfo")
	@Mapping(target = "gameStatus" , source = "status")
	@Mapping(target = "playerNumber" , source = "player")
	@Mapping(target = "result.message" , source = "result.title.description")
	@Mapping(target = "result.status" , source = "result.title.status")
	GameInfo toGameInfo(GameResponse game);

	@Named("pitInfo")
	@Mapping(target = "index",source = "id")
	@Mapping(target = "stones",source = "stones")
	PitInfo toPitInfo(Pit pit);
}
