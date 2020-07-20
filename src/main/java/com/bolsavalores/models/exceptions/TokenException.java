package com.bolsavalores.models.exceptions;

public class TokenException extends StockmarketException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TokenException() {
        super("Não autorizado");
    }
	
	public TokenException(String msg) {
		super(msg);
	}
}
