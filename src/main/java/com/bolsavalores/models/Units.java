package com.bolsavalores.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="Units")
public class Units {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@OneToOne
	@JoinColumn(name="id_acao")
	private Acao acao;
	
	@Column(name="qtd_on")
	private long qtdOn;
	
	@Column(name="qtd_pn")
	private long qtdPn;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Acao getAcao() {
		return acao;
	}

	public void setAcao(Acao acao) {
		this.acao = acao;
	}

	public long getQtdOn() {
		return qtdOn;
	}

	public void setQtdOn(long qtdOn) {
		this.qtdOn = qtdOn;
	}

	public long getQtdPn() {
		return qtdPn;
	}

	public void setQtdPn(long qtdPn) {
		this.qtdPn = qtdPn;
	}
}
