package com.bolsavalores.models.exceptions;

public class InvalidPapelUsuarioException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidPapelUsuarioException() {
		super("Papel do Usuário inválido. ");
	}
	
	public InvalidPapelUsuarioException(String msg) {
		super(msg);
	}
}
