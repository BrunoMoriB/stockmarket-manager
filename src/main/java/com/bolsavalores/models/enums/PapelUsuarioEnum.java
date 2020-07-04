package com.bolsavalores.models.enums;

public enum PapelUsuarioEnum {
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
	
}
