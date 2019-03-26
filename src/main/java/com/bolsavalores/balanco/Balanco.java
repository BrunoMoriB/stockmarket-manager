package com.bolsavalores.balanco;

import java.io.Serializable;
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
	
	@Column(name="patrimonio_liquido")
	private long patrimonioLiquido;
	
	@Column(name="evolucao_patrimonioliq")
	private String evolucaoPatrimonioLiq;
	
	@Column(name="p_l")
	private double preçoSobreLucro;
	
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
	private long LucroLiquidoTrimestral;
	
	@Column(name="evolucao_lucroliq")
	private String evolucaoLucroLiquido;
	
	private long ebitda;
	
	private long caixa;
	
	@Column(name="divida_bruta")
	private long dividaBruta;
	
	@Column(name="evolucao_dividabru")
	private String evolucaoDividaBruta;
	
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

	public long getPatrimonioLiquido() {
		return patrimonioLiquido;
	}

	public void setPatrimonioLiquido(long patrimonioLiquido) {
		this.patrimonioLiquido = patrimonioLiquido;
	}

	public String getEvolucaoPatrimonioLiq() {
		return evolucaoPatrimonioLiq;
	}

	public void setEvolucaoPatrimonioLiq(String evolucaoPatrimonioLiq) {
		this.evolucaoPatrimonioLiq = evolucaoPatrimonioLiq;
	}

	public double getPreçoSobreLucro() {
		return preçoSobreLucro;
	}

	public void setPreçoSobreLucro(double preçoSobreLucro) {
		this.preçoSobreLucro = preçoSobreLucro;
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
		return LucroLiquidoTrimestral;
	}

	public void setLucroLiquidoTrimestral(long lucroLiquidoTrimestral) {
		LucroLiquidoTrimestral = lucroLiquidoTrimestral;
	}

	public String getEvolucaoLucroLiquido() {
		return evolucaoLucroLiquido;
	}

	public void setEvolucaoLucroLiquido(String evolucaoLucroLiquido) {
		this.evolucaoLucroLiquido = evolucaoLucroLiquido;
	}

	public long getEbitda() {
		return ebitda;
	}

	public void setEbitda(long ebitda) {
		this.ebitda = ebitda;
	}

	public long getCaixa() {
		return caixa;
	}

	public void setCaixa(long caixa) {
		this.caixa = caixa;
	}

	public long getDividaBruta() {
		return dividaBruta;
	}

	public void setDividaBruta(long dividaBruta) {
		this.dividaBruta = dividaBruta;
	}

	public String getEvolucaoDividaBruta() {
		return evolucaoDividaBruta;
	}

	public void setEvolucaoDividaBruta(String evolucaoDividaBruta) {
		this.evolucaoDividaBruta = evolucaoDividaBruta;
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
}
