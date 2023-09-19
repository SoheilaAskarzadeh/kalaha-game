package com.game.kalaha.repository;

import com.game.kalaha.model.Game;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends MongoRepository<Game,String > {
}
