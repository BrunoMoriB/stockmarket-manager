package com.bolsavalores.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "acao")
public class Acao implements Serializable, Comparable<Acao> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String codigo;

	@ManyToOne
	@JoinColumn(name="id_empresa")
	private Empresa empresa;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "acao")
	private Set<Cotacao> cotacoes;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "acao")
	private Set<Provento> proventos;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "acao")
	private Units units;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
	@Override
	public String toString() {
		return this.codigo;
	}
	
	@Override
	public int compareTo(Acao outraAcao) {
		return this.codigo.compareTo(outraAcao.codigo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Acao))
			return false;
		Acao other = (Acao) obj;
		return Objects.equals(id, other.id);
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
	public Cotacao getCotacaoDailyUpdated() {
		return this.cotacoes.stream().filter(c -> c.isDailyUpdated() == true).findFirst().orElse(null);
		
	}
	public Set<Cotacao> getCotacoes() {
		return cotacoes;
	}
	public void setCotacoes(Set<Cotacao> cotacoes) {
		this.cotacoes = cotacoes;
	}
	public void setCotacao(Cotacao cotacao) {
		if(this.cotacoes == null)
			this.cotacoes = new HashSet<Cotacao>();

		if(cotacao.isDailyUpdated()) 
			this.cotacoes = this.cotacoes.stream().filter(c -> !c.isDailyUpdated()).collect(Collectors.toSet());
		
		this.cotacoes.add(cotacao);
	}
	public Set<Provento> getProventos() {
		return proventos;
	}
	public void setProventos(Set<Provento> proventos) {
		this.proventos = proventos;
	}
	
	public boolean isUnit() {
		return this.codigo.contains("11");
	}
	public Units getUnits() {
		return units;
	}
	public void setUnits(Units units) {
		this.units = units;
	}
}