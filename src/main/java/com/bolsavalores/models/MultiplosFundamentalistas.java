package com.bolsavalores.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="multiplosfundamentalistas")
public class MultiplosFundamentalistas implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(name="preco_lucro")
	private Double precoSobreLucro;
	
	@Column(name="media_precolucro")
	private Double mediaPrecoSobreLucro;
	 
	@Column(name="preco_valorpatrimonial")
	private Double precoSobreValorPatrimonial;
	
	@Column(name="media_precovalorpatrimonial")
	private Double mediaPrecoSobreValorPatrimonial;
	
	private Double roe;
	
	@Column(name="dividabruta_patrimonioliquido")
	private Double dividaBrutaSobrePatrimonioLiquido;
	
	@Column(name="caixadisponivel_dividabruta")
	private Double caixaDisponivelSobreDividaBruta;
	
	@Column(name="justificativa_nota")
	private String justificativaNota;
	
	private int nota;
	
	@ManyToOne()
	@JoinColumn(name="id_balanco")
	private Balanco balanco;
	
	@ManyToOne()
	@JoinColumn(name="id_acao")
	private Acao acao;

	public Double getPrecoSobreLucro() {
		return precoSobreLucro;
	}

	public void setPrecoSobreLucro(Double precoSobreLucro) {
		this.precoSobreLucro = precoSobreLucro;
	}

	public Double getMediaPrecoSobreLucro() {
		return mediaPrecoSobreLucro;
	}

	public void setMediaPrecoSobreLucro(Double mediaPrecoSobreLucro) {
		this.mediaPrecoSobreLucro = mediaPrecoSobreLucro;
	}

	public Double getPrecoSobreValorPatrimonial() {
		return precoSobreValorPatrimonial;
	}

	public void setPrecoSobreValorPatrimonial(Double precoSobreValorPatrimonial) {
		this.precoSobreValorPatrimonial = precoSobreValorPatrimonial;
	}

	public Double getMediaPrecoSobreValorPatrimonial() {
		return mediaPrecoSobreValorPatrimonial;
	}

	public void setMediaPrecoSobreValorPatrimonial(Double mediaPrecoSobreValorPatrimonial) {
		this.mediaPrecoSobreValorPatrimonial = mediaPrecoSobreValorPatrimonial;
	}

	public Double getRoe() {
		return roe;
	}

	public void setRoe(Double roe) {
		this.roe = roe;
	}

	public Double getDividaBrutaSobrePatrimonioLiquido() {
		return dividaBrutaSobrePatrimonioLiquido;
	}

	public void setDividaBrutaSobrePatrimonioLiquido(Double dividaBrutaSobrePatrimonioLiquido) {
		this.dividaBrutaSobrePatrimonioLiquido = dividaBrutaSobrePatrimonioLiquido;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Double getCaixaDisponivelSobreDividaBruta() {
		return caixaDisponivelSobreDividaBruta;
	}

	public void setCaixaDisponivelSobreDividaBruta(Double caixaDisponivelSobreDividaBruta) {
		this.caixaDisponivelSobreDividaBruta = caixaDisponivelSobreDividaBruta;
	}

	public Balanco getBalanco() {
		return balanco;
	}

	public void setBalanco(Balanco balanco) {
		this.balanco = balanco;
	}

	public Acao getAcao() {
		return acao;
	}

	public void setAcao(Acao acao) {
		this.acao = acao;
	}

	public int getNota() {
		return nota;
	}

	public void setNota(int nota) {
		this.nota = nota;
	}

	public String getJustificativaNota() {
		return justificativaNota;
	}

	public void setJustificativaNota(String justificativaNota) {
		this.justificativaNota = justificativaNota;
	}
}
