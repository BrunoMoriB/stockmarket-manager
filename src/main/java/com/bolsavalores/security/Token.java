package com.bolsavalores.security;

import com.bolsavalores.models.Usuario;

public class Token {
	private String hash;
//	private String apelido;
//	private String email;
//    private Long idUsuario;
    
    private Usuario usuario;
    
    public Token(String hash) {
    	this.hash = hash;
    }
    
	/*public Token(String hash, String apelido, String email, Long idUsuario) {
		super();
		this.hash = hash;
		this.apelido = apelido;
		this.email = email;
		this.idUsuario = idUsuario;
	}*/
    
    public Token(String hash, Usuario usuario) {
    	this.hash 	 = hash;
    	this.usuario = usuario;
    }
	
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
