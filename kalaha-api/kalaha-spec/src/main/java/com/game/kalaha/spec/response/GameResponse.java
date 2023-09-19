package com.game.kalaha.spec.response;

import java.util.List;

import com.game.kalaha.spec.dto.GameStatus;
import com.game.kalaha.spec.dto.Pit;
import com.game.kalaha.spec.dto.Player;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class GameResponse extends ResponseService {

	private String gameId;
	private List<Pit> pits;
	private GameStatus status;
	private Player player;
}
