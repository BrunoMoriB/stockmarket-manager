package com.bolsavalores.security;

import com.bolsavalores.models.enums.PapelUsuarioEnum;

public class Token {
	private String hash;
	private Long idUsuario;
	private PapelUsuarioEnum papelUsuario;
    
    public Token(String hash, Long idUsuario, PapelUsuarioEnum papelUsuario) {
		this.hash = hash;
		this.idUsuario = idUsuario;
		this.papelUsuario = papelUsuario;
    }
    	
	public String getHash() {
		return hash;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public PapelUsuarioEnum getPapelUsuario() {
		return papelUsuario;
	}

}
