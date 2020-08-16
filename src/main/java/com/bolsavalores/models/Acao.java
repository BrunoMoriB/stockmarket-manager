package com.bolsavalores.models;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "acao")
public class Acao implements Serializable, Comparable<Acao> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String codigo;

	@ManyToOne
	@JoinColumn(name="id_empresa")
	private Empresa empresa;
	
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
}
