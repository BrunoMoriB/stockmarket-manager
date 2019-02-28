package com.bolsavalores.ordem;

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
@Table(name="ordem")
public class Ordem implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private Date data;
	private String ordem;
	private double cotacao;
	private int quantidade;
	
	@Column(name="taxa_corretagem")
	private double taxaCorretagem;
	
	@ManyToOne
	@JoinColumn(name="id_acao")
	private Acao idAcao;

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

	public String getOrdem() {
		return ordem;
	}

	public void setOrdem(String ordem) {
		this.ordem = ordem;
	}

	public double getCotacao() {
		return cotacao;
	}

	public void setCotacao(double cotacao) {
		this.cotacao = cotacao;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public double getTaxaCorretagem() {
		return taxaCorretagem;
	}

	public void setTaxaCorretagem(double taxaCorretagem) {
		this.taxaCorretagem = taxaCorretagem;
	}

	public Acao getIdAcao() {
		return idAcao;
	}

	public void setIdAcao(Acao idAcao) {
		this.idAcao = idAcao;
	}
}
