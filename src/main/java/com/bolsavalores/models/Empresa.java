package com.bolsavalores.models;

import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
public class Empresa implements Comparable<Empresa> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "razao_social")
	private String razaoSocial;

	@Column(name = "nome_pregao")
	private String nomePregao;

	@Column(name = "cnpj")
	private String cnpj;
	
	private long quantidade;

	@ManyToMany
	@JoinTable(
		name = "empresasetor",
		joinColumns = @JoinColumn(name = "id_empresa"),
		inverseJoinColumns = @JoinColumn(name = "id_setor")
	)
	private Set<Setor> setores;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "empresa")
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
    
	public long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(long quantidade) {
		this.quantidade = quantidade;
	}

	public Set<Setor> getSetores() {
		return setores;
	}

	public void setSetores(Set<Setor> setores) {
		this.setores = setores;
	}

	public Set<Acao> getAcoes() {
		return acoes;
	}

	public void setAcoes(Set<Acao> acoes) {
		this.acoes = acoes;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public void setNomePregao(String nomePregao) {
		this.nomePregao = nomePregao;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
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
	
	@Override
	public int compareTo(Empresa outraEmpresa) {
		return this.nomePregao.compareTo(outraEmpresa.nomePregao);
	}
}