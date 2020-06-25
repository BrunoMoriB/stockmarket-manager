package com.bolsavalores.security;

public class Token {
	private String hash;
	private String email;
    private Long idUsuario;
    
    public Token(String hash) {
    	this.hash = hash;
    }
    
	public Token(String hash, String email, Long idUsuario) {
		super();
		this.hash = hash;
		this.email = email;
		this.idUsuario = idUsuario;
	}
	
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public Long getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
