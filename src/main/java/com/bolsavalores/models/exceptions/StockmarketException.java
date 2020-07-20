package com.bolsavalores.models.exceptions;

public class StockmarketException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public StockmarketException() {
		super("Ocorreu um erro interno. Por favor tente mais tarde! ");
	}
	
	public StockmarketException(String msg) {
		super(msg);
	}
}
