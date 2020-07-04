package com.bolsavalores.models.exceptions;

public class UsuarioNotFoundException extends StockmarketException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UsuarioNotFoundException() {
        super("Usuário não identificado");
    }
	
	public UsuarioNotFoundException(String msg) {
		super(msg);
	}

}
