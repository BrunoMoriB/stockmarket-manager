package com.bolsavalores.balanco;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.bolsavalores.acao.Acao;

@Entity
@Table(name="balanco")
public class Balanco implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private Date data;
	
	@Column(name="p_l")
	private double precoSobreLucro;
	
	@Column(name="p_vpa")
	private double precoSobreValorPatrimonial;
	
	private String roe;
	
	@Column(name="liquidez_corrente")
	private double liquidezCorrente;
	
	@Column(name="divbruta_patrimonioliq")
	private double dividaBrutaSobrePatrimonioLiq;
	
	@Column(name="margem_liquida")
	private String margemLiquida;
	
	@Column(name="dy")
	private String dividendYield;
	
	@Column(name="lucroliq_trimestral")
	private long lucroLiquidoTrimestral;
	
	@Column(name="evolucao_lucroliq")
	private String evolucaoLucroLiquido;
	
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

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
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

	public String getRoe() {
		return roe;
	}

	public void setRoe(String roe) {
		this.roe = roe;
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

	public String getMargemLiquida() {
		return margemLiquida;
	}

	public void setMargemLiquida(String margemLiquida) {
		this.margemLiquida = margemLiquida;
	}

	public String getDividendYield() {
		return dividendYield;
	}

	public void setDividendYield(String dividendYield) {
		this.dividendYield = dividendYield;
	}

	public long getLucroLiquidoTrimestral() {
		return lucroLiquidoTrimestral;
	}

	public void setLucroLiquidoTrimestral(long lucroLiquidoTrimestral) {
		this.lucroLiquidoTrimestral = lucroLiquidoTrimestral;
	}

	public String getEvolucaoLucroLiquido() {
		return evolucaoLucroLiquido;
	}

	public void setEvolucaoLucroLiquido(String evolucaoLucroLiquido) {
		this.evolucaoLucroLiquido = evolucaoLucroLiquido;
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
}
