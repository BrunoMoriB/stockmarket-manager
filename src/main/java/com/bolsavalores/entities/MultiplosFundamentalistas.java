package com.bolsavalores.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="multiplosfundamentalistas")
public class MultiplosFundamentalistas implements Serializable{

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
}
