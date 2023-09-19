package com.game.kalahaui.action;

import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.game.kalahaui.service.KalahaService;
import com.game.kalahaui.service.dto.GameInfo;
import com.game.kalahaui.service.dto.GameInfo.Status;
import com.game.kalahaui.service.dto.PitInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;

@Getter
@Setter
@Named("kalaha")
@RequiredArgsConstructor
@ViewScoped
@Slf4j
public class Kalaha {

	private boolean hasGame=false;

	private boolean finished=false;
	private final KalahaService service;

	private GameInfo gameInfo;

	private List<PitInfo> playerOnePits;
	private List<PitInfo> playerTwoPits;
	private PitInfo playerOneStore;
	private PitInfo playerTwoStore;

	public void createGame() {
		if(!hasGame){
			try{
				logger.info("create new game");
				gameInfo=service.createGame();
				if(gameInfo!=null){
					setHasGame(true);
					setGameProperty(gameInfo);
				}
			}catch (Exception ex){
				addMessage(FacesMessage.SEVERITY_ERROR,"Create Game Error","Service UnAvailable");
			}
		}
	}

	public void move(Integer pitIndex){
		try{
			logger.info("paly game with player: {} from pit: {}",gameInfo.getPlayerNumber().name(),pitIndex);
			gameInfo= service.play(gameInfo.getGameId(),pitIndex+1);
			setGameProperty(gameInfo);
			if(!gameInfo.getGameStatus().equals(Status.ACTIVE)){
				showResult();
				if(gameInfo.getGameStatus()!=Status.ACTIVE){
					setFinished(true);
				}
			}
		}catch (Exception ex){
			addMessage(FacesMessage.SEVERITY_ERROR,"Play Game Error", ex.getMessage());
		}
	}

	private void setGameProperty(GameInfo gameInfo){
		setPlayerOnePits(gameInfo.getPitInfos().subList(0,6));
		var pitsPlayerTwo=gameInfo.getPitInfos().subList(7,13);
		Collections.reverse(pitsPlayerTwo);
		setPlayerTwoPits(pitsPlayerTwo);
		setPlayerOneStore(gameInfo.getPitInfos().get(6));
		setPlayerTwoStore(gameInfo.getPitInfos().get(13));
	}

	private void showResult(){
		logger.info("game is finished with status: {}",gameInfo.getGameStatus().name());
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Game Result", gameInfo.getGameStatus().name());
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}

	public void clearData(){
		setFinished(false);
		setGameInfo(null);
		setPlayerTwoPits(null);
		setPlayerOnePits(null);
		setPlayerTwoStore(null);
		setPlayerOneStore(null);
		setHasGame(false);
	}

	public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage(severity, summary, detail));
	}

}
