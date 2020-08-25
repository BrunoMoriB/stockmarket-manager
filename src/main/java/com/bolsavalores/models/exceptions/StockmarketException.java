package com.bolsavalores.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class StockmarketException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public StockmarketException() {
		super("Ocorreu um erro interno. Por favor tente mais tarde! ");
	}
	
	public StockmarketException(String msg) {
		super(msg);
	}
}
