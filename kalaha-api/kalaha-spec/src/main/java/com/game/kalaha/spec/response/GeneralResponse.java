package com.game.kalaha.spec.response;

import com.game.kalaha.spec.dto.ResultStatus;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GeneralResponse extends ResponseService{

	String detailMessage;
	public GeneralResponse(ResultStatus resultStatus) {
		setResult(resultStatus);
	}

	public GeneralResponse(ResultStatus resultStatus, String detailMessage) {
		setResult(resultStatus);
		this.detailMessage = detailMessage;
	}
}
