package com.game.kalaha.spec.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.game.kalaha.spec.dto.Result;
import com.game.kalaha.spec.dto.ResultStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResponseService {

	Result result;

	@JsonProperty
	public void setResult(Result result) {
		this.result = result;
	}

	public void setResult(ResultStatus resultStatus) {
		if (resultStatus == null) {
			return;
		}
		Result resultInfo = new Result();
		resultInfo.setTitle(resultStatus);
		this.result = resultInfo;
	}
}
