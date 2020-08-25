package com.bolsavalores.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class TokenException extends StockmarketException {

	private static final long serialVersionUID = 1L;

	public TokenException() {
        super("Não autorizado");
    }
	
	public TokenException(String msg) {
		super(msg);
	}
}
