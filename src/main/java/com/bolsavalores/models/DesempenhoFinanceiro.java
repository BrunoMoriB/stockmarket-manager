package com.bolsavalores.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="desempenhofinanceiro")
public class DesempenhoFinanceiro implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(name="evolucaolucroliquido_trimestral")
	private Double evolucaoLucroLiquidoTrimestral;
	
	@Column(name="evolucaolucroliquido_anual")
	private Double evolucaoLucroLiquidoAnual;
	
	@Column(name="hascrescimentolucroliquido_tresanos")
	private Boolean hasCrescimentoLucroLiquidoTresAnos;

	public Double getEvolucaoLucroLiquidoTrimestral() {
		return evolucaoLucroLiquidoTrimestral;
	}

	public void setEvolucaoLucroLiquidoTrimestral(Double evolucaoLucroLiquidoTrimestral) {
		this.evolucaoLucroLiquidoTrimestral = evolucaoLucroLiquidoTrimestral;
	}

	public Double getEvolucaoLucroLiquidoAnual() {
		return evolucaoLucroLiquidoAnual;
	}

	public void setEvolucaoLucroLiquidoAnual(Double evolucaoLucroLiquidoAnual) {
		this.evolucaoLucroLiquidoAnual = evolucaoLucroLiquidoAnual;
	}

	public Boolean getHasCrescimentoLucroLiquidoTresAnos() {
		return hasCrescimentoLucroLiquidoTresAnos;
	}

	public void setHasCrescimentoLucroLiquidoTresAnos(Boolean hasCrescimentoLucroLiquidoTresAnos) {
		this.hasCrescimentoLucroLiquidoTresAnos = hasCrescimentoLucroLiquidoTresAnos;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
