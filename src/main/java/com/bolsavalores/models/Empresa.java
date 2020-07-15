package com.bolsavalores.models;

import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "razao_social")
	private String razaoSocial;

	@Column(name = "nome_pregao")
	private String nomePregao;

	@Column(name = "cnpj")
	private String cnpj;

	@ManyToMany
	@JoinTable(
		name = "empresa_setor",
		joinColumns = @JoinColumn(name = "id_empresa"),
		inverseJoinColumns = @JoinColumn(name = "id_setor")
	)
	private Set<Setor> setores;

	@OneToMany
	private Set<Acao> acoes;

    public long getId() {
        return id;
	}
	
	public String getRazaoSocial() {
		return razaoSocial;
	}

	public String getNomePregao() {
		return nomePregao;
	}

	public String getCnpj() {
		return cnpj;
	}
    
    @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Empresa))
			return false;
		Empresa other = (Empresa) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}