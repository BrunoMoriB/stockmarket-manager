package com.bolsavalores.models.exceptions;

public class BalancoNotFoundException  extends StockmarketException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public BalancoNotFoundException() {
        super("Balanço não identificado");
    }
	
	public BalancoNotFoundException(String msg) {
		super(msg);
	}
}
