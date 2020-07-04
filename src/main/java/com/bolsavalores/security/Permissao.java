package com.bolsavalores.security;

import org.springframework.security.core.GrantedAuthority;

public class Permissao implements GrantedAuthority {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String nome;

	public Permissao(String nome) {
		super();
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String getAuthority() {
		return this.nome;
	}
}
