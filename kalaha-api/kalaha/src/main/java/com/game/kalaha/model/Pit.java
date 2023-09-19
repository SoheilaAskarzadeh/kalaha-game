package com.game.kalaha.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public abstract class Pit {

	private Integer id;
	private Integer stones;
	private PlayerNumber owner;

	protected Pit(Integer id, Integer stones,PlayerNumber owner) {
		this.id = id;
		this.stones = stones;
		this.owner=owner;
	}

	// this method shows if we can take a stone from a pit
	public abstract boolean allowable();

	@JsonIgnore
	public Boolean isEmpty (){
		return this.stones == 0;
	}

	public void clear (){
		this.stones = 0;
	}

	public void sow () {
		this.stones++;
	}

	public void addStones (Integer stones){
		this.stones+= stones;
	}

}
