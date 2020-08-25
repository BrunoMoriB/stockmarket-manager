package com.bolsavalores.models.enums;

import org.springframework.security.core.GrantedAuthority;

public enum PapelUsuarioEnum implements GrantedAuthority {
	COMUM("Comum"),
	PREMIUM("Premium"),
	ADMINISTRADOR("Administrador");
	
	private String descricao;
	
	private PapelUsuarioEnum(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String getAuthority() {
		return name();
	}
	
}
