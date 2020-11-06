package com.bolsavalores.models;

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
@Table(name = "cotacao")
public class Cotacao implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private LocalDate data;
	private double valor;
	
	@Column(name="isdailyupdated")
	private boolean dailyUpdated;

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

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public Acao getAcao() {
		return acao;
	}

	public void setAcao(Acao acao) {
		this.acao = acao;
	}

	public boolean isDailyUpdated() {
		return dailyUpdated;
	}

	public void setDailyUpdated(boolean dailyUpdated) {
		this.dailyUpdated = dailyUpdated;
	}
}
