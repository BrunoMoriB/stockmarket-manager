package com.bolsavalores.models.exceptions;

public class B3ClientInfoException extends StockmarketException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public B3ClientInfoException() {
        super("Não foi possível obter as informações do Client da B3");
    }
	
	public B3ClientInfoException(String msg) {
		super(msg);
	}

}
