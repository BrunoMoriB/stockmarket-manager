package com.bolsavalores.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="operacao")
public class Operacao {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(name="id_oc")
	private long idOrdemCompra;
	
	@Column(name="id_ov")
	private long idOrdemVenda;
	
	@Column(name="valor_compra")
	private double valorCompra;
	
	@Column(name="valor_venda")
	private double valorVenda;
	
	private double proventos;
	
	@Column(name="lucro_liquido")
	private double lucroLiquido;
	
	private double periodo;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIdOrdemCompra() {
		return idOrdemCompra;
	}

	public void setIdOrdemCompra(long idOrdemCompra) {
		this.idOrdemCompra = idOrdemCompra;
	}

	public long getIdOrdemVenda() {
		return idOrdemVenda;
	}

	public void setIdOrdemVenda(long idOrdemVenda) {
		this.idOrdemVenda = idOrdemVenda;
	}

	public double getValorCompra() {
		return valorCompra;
	}

	public void setValorCompra(double valorCompra) {
		this.valorCompra = valorCompra;
	}

	public double getValorVenda() {
		return valorVenda;
	}

	public void setValorVenda(double valorVenda) {
		this.valorVenda = valorVenda;
	}

	public double getProventos() {
		return proventos;
	}

	public void setProventos(double proventos) {
		this.proventos = proventos;
	}

	public double getLucroLiquido() {
		return lucroLiquido;
	}

	public void setLucroLiquido(double lucroLiquido) {
		this.lucroLiquido = lucroLiquido;
	}

	public double getPeriodo() {
		return periodo;
	}

	public void setPeriodo(double periodo) {
		this.periodo = periodo;
	}
}
