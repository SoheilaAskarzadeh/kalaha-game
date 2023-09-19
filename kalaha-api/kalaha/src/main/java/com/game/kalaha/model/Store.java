package com.game.kalaha.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class Store extends Pit {

	public Store(Integer id, PlayerNumber owner) {
		super(id, 0, owner);
	}

	@Override
	public boolean allowable() {
		return false;
	}
}
