package com.bolsavalores.entities;

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
	
	@Column(name="evolucaodividaliquida_anual")
	private Double evolucaoDividaLiquidaAnual;
	
	@Column(name="hascrescimentolucroliquido_trestrimestres")
	private Boolean hasCrescimentoLucroLiquidoTresTrimestres;
	
	@Column(name="hascrescimentolucroliquido_tresanos")
	private Boolean hasCrescimentoLucroLiquidoTresAnos;
	
	@Column(name="hascrescimentodividaliquida_tresanos")
	private Boolean hasCrescimentoDividaLiquidaTresAnos;

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

	public Double getEvolucaoDividaLiquidaAnual() {
		return evolucaoDividaLiquidaAnual;
	}

	public void setEvolucaoDividaLiquidaAnual(Double evolucaoDividaLiquidaAnual) {
		this.evolucaoDividaLiquidaAnual = evolucaoDividaLiquidaAnual;
	}

	public Boolean getHasCrescimentoLucroLiquidoTresTrimestres() {
		return hasCrescimentoLucroLiquidoTresTrimestres;
	}

	public void setHasCrescimentoLucroLiquidoTresTrimestres(Boolean hasCrescimentoLucroLiquidoTresTrimestres) {
		this.hasCrescimentoLucroLiquidoTresTrimestres = hasCrescimentoLucroLiquidoTresTrimestres;
	}

	public Boolean getHasCrescimentoLucroLiquidoTresAnos() {
		return hasCrescimentoLucroLiquidoTresAnos;
	}

	public void setHasCrescimentoLucroLiquidoTresAnos(Boolean hasCrescimentoLucroLiquidoTresAnos) {
		this.hasCrescimentoLucroLiquidoTresAnos = hasCrescimentoLucroLiquidoTresAnos;
	}

	public Boolean getHasCrescimentoDividaLiquidaTresAnos() {
		return hasCrescimentoDividaLiquidaTresAnos;
	}

	public void setHasCrescimentoDividaLiquidaTresAnos(Boolean hasCrescimentoDividaLiquidaTresAnos) {
		this.hasCrescimentoDividaLiquidaTresAnos = hasCrescimentoDividaLiquidaTresAnos;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
