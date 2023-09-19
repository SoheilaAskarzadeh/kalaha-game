package com.game.kalahaui.exception;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.kalaha.spec.response.GeneralResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

@Slf4j
public class ClientErrorDecoder extends ErrorDecoder.Default {

	private final ObjectMapper objectMapper;

	public ClientErrorDecoder() {
		this.objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	@Override
	public Exception decode (String methodKey, Response response){
		if (response.status() == HttpStatus.UNPROCESSABLE_ENTITY.value()) {
			GeneralResponse generalResponse = read(response);
			if (generalResponse == null || generalResponse.getResult() == null) {
				return new ClientException("Error happen");
			}
			return new ClientException(generalResponse.getResult().getTitle().getDescription());
		}
		logger.error("received error from client status: {}", response.status());
		return super.decode(methodKey, response);
	}

	@Nullable
	private GeneralResponse read (Response response){
		try {
			return objectMapper.readValue(response.body().asReader(StandardCharsets.UTF_8), GeneralResponse.class);
		} catch (IOException e) {
			logger.error("couldn't parse general response");
			return null;
		}
	}
}
