package com.game.kalaha.api.exception;

import com.game.kalaha.exception.BusinessException;
import com.game.kalaha.spec.dto.ResultStatus;
import com.game.kalaha.spec.response.GeneralResponse;
import com.game.kalaha.spec.response.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RequiredArgsConstructor
@Slf4j
@ControllerAdvice
public class KalahaExceptionHandler  extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		return new ResponseEntity<>(new GeneralResponse(ResultStatus.INVALID_PARAMETER), HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(BusinessException.class)
	public final ResponseEntity<ResponseService> handleBusinessException(BusinessException ex) {
		return new ResponseEntity<>(new GeneralResponse(ex.getResultStatus()), HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ResponseEntity<ResponseService> handleGenericException(final Exception ex ,final WebRequest request) {
		return new ResponseEntity<>(new GeneralResponse(ResultStatus.FAILED,ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
