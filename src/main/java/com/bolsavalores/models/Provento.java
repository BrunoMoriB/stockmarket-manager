package com.bolsavalores.models;

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
@Table(name = "Provento")
public class Provento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String tipo;
	
	private double valor;
	
	@Column(name = "data_ex")
	private LocalDate dataEx;
	
	@Column(name="data_pagamento")
	private LocalDate dataPagamento;
	
	@ManyToOne()
	@JoinColumn(name = "id_acao")
	private Acao acao;

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public LocalDate getDataEx() {
		return dataEx;
	}

	public void setDataEx(LocalDate dataEx) {
		this.dataEx = dataEx;
	}

	public LocalDate getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(LocalDate dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Acao getAcao() {
		return acao;
	}

	public void setAcao(Acao acao) {
		this.acao = acao;
	}

	public long getId() {
		return id;
	}
	
	
}
