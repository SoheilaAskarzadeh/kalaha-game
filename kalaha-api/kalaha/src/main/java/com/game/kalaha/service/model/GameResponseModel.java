package com.game.kalaha.service.model;

import java.util.List;

import com.game.kalaha.model.Game.Status;
import com.game.kalaha.model.Pit;
import com.game.kalaha.model.PlayerNumber;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GameResponseModel{
 private String gameId;
 private Status status;
 private List<Pit> pits;
 private PlayerNumber player;
}
