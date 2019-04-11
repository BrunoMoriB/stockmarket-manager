package com.bolsavalores.operacao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.bolsavalores.acao.Acao;
import com.bolsavalores.ordem.Ordem;

@Entity
@Table(name="operacao")
public class Operacao {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@OneToOne
	@JoinColumn(name="id_oc")
	private Ordem ordemCompra;
	
	@OneToOne
	@JoinColumn(name="id_ov")
	private Ordem ordemVenda;
	
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

	public Ordem getOrdemCompra() {
		return ordemCompra;
	}

	public void setOrdemCompra(Ordem ordemCompra) {
		this.ordemCompra = ordemCompra;
	}

	public Ordem getOrdemVenda() {
		return ordemVenda;
	}

	public void setOrdemVenda(Ordem ordemVenda) {
		this.ordemVenda = ordemVenda;
	}
}
