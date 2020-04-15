package com.bolsavalores.entities;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="balanco")
public class Balanco implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private LocalDate data;
	
	@Column(name="p_l")
	private double precoSobreLucro;
	
	@Column(name="p_vpa")
	private double precoSobreValorPatrimonial;
	
	private double roe;
	
	@Column(name="liquidez_corrente")
	private double liquidezCorrente;
	
	@Column(name="divbruta_patrimonioliq")
	private double dividaBrutaSobrePatrimonioLiq;
	
	@Column(name="margem_liquida")
	private double margemLiquida;
	
	@Column(name="dy")
	private double dividendYield;
	
	@Column(name="lucroliq_trimestral")
	private long lucroLiquidoTrimestral;
	
	@Column(name="lucroliq_anual")
	private long lucroLiquidoAnual;
	
	@Column(name="evolucao_lucroliqmeses")
	private double evolucaoLucroLiquidoMeses;
	
	@Column(name="evolucao_lucroliqanos")
	private double evolucaoLucroLiquidoAnos;
	
	@Column(name="lucrocrescente_tresmeses")
	private Boolean lucroCrescenteTresMeses;
	
	@Column(name="lucrocrescente_tresanos")
	private Boolean lucroCrescenteTresAnos;
	
	private int nota;
	
	@ManyToOne
	@JoinColumn(name="id_acao")
	private Acao acao;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public double getPrecoSobreLucro() {
		return precoSobreLucro;
	}

	public void setPrecoSobreLucro(double precoSobreLucro) {
		this.precoSobreLucro = precoSobreLucro;
	}

	public double getPrecoSobreValorPatrimonial() {
		return precoSobreValorPatrimonial;
	}

	public void setPrecoSobreValorPatrimonial(double precoSobreValorPatrimonial) {
		this.precoSobreValorPatrimonial = precoSobreValorPatrimonial;
	}

	public double getLiquidezCorrente() {
		return liquidezCorrente;
	}

	public void setLiquidezCorrente(double liquidezCorrente) {
		this.liquidezCorrente = liquidezCorrente;
	}

	public double getDividaBrutaSobrePatrimonioLiq() {
		return dividaBrutaSobrePatrimonioLiq;
	}

	public void setDividaBrutaSobrePatrimonioLiq(double dividaBrutaSobrePatrimonioLiq) {
		this.dividaBrutaSobrePatrimonioLiq = dividaBrutaSobrePatrimonioLiq;
	}

	public long getLucroLiquidoTrimestral() {
		return lucroLiquidoTrimestral;
	}

	public void setLucroLiquidoTrimestral(long lucroLiquidoTrimestral) {
		this.lucroLiquidoTrimestral = lucroLiquidoTrimestral;
	}

	public int getNota() {
		return nota;
	}

	public void setNota(int nota) {
		this.nota = nota;
	}

	public Acao getAcao() {
		return acao;
	}

	public void setAcao(Acao acao) {
		this.acao = acao;
	}

	public Boolean getLucroCrescenteTresMeses() {
		return lucroCrescenteTresMeses;
	}

	public void setLucroCrescenteTresMeses(Boolean lucroCrescenteTresMeses) {
		this.lucroCrescenteTresMeses = lucroCrescenteTresMeses;
	}

	public Boolean getLucroCrescenteTresAnos() {
		return lucroCrescenteTresAnos;
	}

	public void setLucroCrescenteTresAnos(Boolean lucroCrescenteTresAnos) {
		this.lucroCrescenteTresAnos = lucroCrescenteTresAnos;
	}

	public double getRoe() {
		return roe;
	}

	public void setRoe(double roe) {
		this.roe = roe;
	}

	public double getMargemLiquida() {
		return margemLiquida;
	}

	public void setMargemLiquida(double margemLiquida) {
		this.margemLiquida = margemLiquida;
	}

	public double getDividendYield() {
		return dividendYield;
	}

	public void setDividendYield(double dividendYield) {
		this.dividendYield = dividendYield;
	}

	public double getEvolucaoLucroLiquidoMeses() {
		return evolucaoLucroLiquidoMeses;
	}

	public void setEvolucaoLucroLiquidoMeses(double evolucaoLucroLiquidoMeses) {
		this.evolucaoLucroLiquidoMeses = evolucaoLucroLiquidoMeses;
	}

	public double getEvolucaoLucroLiquidoAnos() {
		return evolucaoLucroLiquidoAnos;
	}

	public void setEvolucaoLucroLiquidoAnos(double evolucaoLucroLiquidoAnos) {
		this.evolucaoLucroLiquidoAnos = evolucaoLucroLiquidoAnos;
	}

	public long getLucroLiquidoAnual() {
		return lucroLiquidoAnual;
	}

	public void setLucroLiquidoAnual(long lucroLiquidoAnual) {
		this.lucroLiquidoAnual = lucroLiquidoAnual;
	}
}
