package com.bolsavalores.models;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="setor")
public class Setor implements Serializable, Comparable<Setor> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@ManyToMany
	@JoinTable(
		name = "empresasetor",
		joinColumns = @JoinColumn(name = "id_setor"),
		inverseJoinColumns = @JoinColumn(name = "id_empresa")
	)
	private Set<Empresa> empresas;
	
	private String nome;

	public long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public Set<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(Set<Empresa> empresas) {
		this.empresas = empresas;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public int compareTo(Setor outroSetor) {
		return this.nome.compareTo(outroSetor.nome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Setor))
			return false;
		Setor other = (Setor) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
