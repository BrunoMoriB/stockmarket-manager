package com.bolsavalores.models;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="balanco")
public class Balanco implements Serializable, Comparable<Balanco> {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	@JoinColumn(name="id_acao")
	private Acao acao;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="id_multiplos")
	private MultiplosFundamentalistas multiplosFundamentalistas;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="id_desempenho")
	private DesempenhoFinanceiro desempenhoFinanceiro;
	
	private LocalDate data;
	
	@Column(name="lucroliq_trimestral")
	private Long lucroLiquidoTrimestral;
	
	@Column(name="lucroliq_anual")
	private Long lucroLiquidoAnual;
	
	@Column(name="patrimonioliquido")
	private Long patrimonioLiquido;
	
	@Column(name="dividabruta")
	private Long dividaBruta;
	
	@Column(name="caixadisponivel")
	private Long caixaDisponivel;
	
	@Column(name="qtdpapeis")
	private Long qtdPapeis;
	
	@Column(name="justificativa_nota")
	private String justificativaNota;
	
	@Column(name="isdailyupdated")
	private boolean dailyUpdated;
	
	private Double cotacao;
	
	private String trimestre;
	
	private int nota;
	
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

	public Long getLucroLiquidoTrimestral() {
		return lucroLiquidoTrimestral;
	}

	public void setLucroLiquidoTrimestral(Long lucroLiquidoTrimestral) {
		this.lucroLiquidoTrimestral = lucroLiquidoTrimestral;
	}

	public Long getLucroLiquidoAnual() {
		return lucroLiquidoAnual;
	}

	public void setLucroLiquidoAnual(Long lucroLiquidoAnual) {
		this.lucroLiquidoAnual = lucroLiquidoAnual;
	}

	public String getTrimestre() {
		return trimestre;
	}

	public void setTrimestre(String trimestre) {
		this.trimestre = trimestre;
	}

	public MultiplosFundamentalistas getMultiplosFundamentalistas() {
		return multiplosFundamentalistas;
	}

	public void setMultiplosFundamentalistas(MultiplosFundamentalistas multiplosFundamentalistas) {
		this.multiplosFundamentalistas = multiplosFundamentalistas;
	}

	public DesempenhoFinanceiro getDesempenhoFinanceiro() {
		return desempenhoFinanceiro;
	}

	public void setDesempenhoFinanceiro(DesempenhoFinanceiro desempenhoFinanceiro) {
		this.desempenhoFinanceiro = desempenhoFinanceiro;
	}

	public Long getPatrimonioLiquido() {
		return patrimonioLiquido;
	}

	public void setPatrimonioLiquido(Long patrimonioLiquido) {
		this.patrimonioLiquido = patrimonioLiquido;
	}

	public Long getDividaBruta() {
		return dividaBruta;
	}

	public void setDividaBruta(Long dividaBruta) {
		this.dividaBruta = dividaBruta;
	}

	public Long getCaixaDisponivel() {
		return caixaDisponivel;
	}

	public void setCaixaDisponivel(Long caixaDisponivel) {
		this.caixaDisponivel = caixaDisponivel;
	}

	public Long getQtdPapeis() {
		return qtdPapeis;
	}

	public void setQtdPapeis(Long qtdPapeis) {
		this.qtdPapeis = qtdPapeis;
	}

	public Double getCotacao() {
		return cotacao;
	}
	
	public String getJustificativaNota() {
		return justificativaNota;
	}

	public void setJustificativaNota(String justificativaNota) {
		this.justificativaNota = justificativaNota;
	}
	
	public void setCotacao(Double cotacao) {
		this.cotacao = cotacao;
	}

	public boolean isDailyUpdated() {
		return dailyUpdated;
	}

	public void setDailyUpdated(boolean isDailyUpdated) {
		this.dailyUpdated = isDailyUpdated;
	}
	
	@Override
	public int compareTo(Balanco outroBalanco) {
		// TODO Auto-generated method stub
		if(this.dailyUpdated)
			return 1;
		else if(outroBalanco.isDailyUpdated())
			return -1;
		else		
			return this.data.compareTo(outroBalanco.getData());
	}
}
