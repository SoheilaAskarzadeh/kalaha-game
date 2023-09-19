package com.game.kalaha.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class House extends Pit{
	public House(Integer id, Integer stones, PlayerNumber owner) {
		super(id, stones, owner);
	}

	@Override
	public boolean allowable() {
		return true;
	}
}
