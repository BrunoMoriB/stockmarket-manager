package com.bolsavalores.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="acao")
public class Acao implements Serializable, Comparable<Acao> {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	private String nome;
	private String codigo;
	private long quantidade;
	
	@OneToOne
	@JoinColumn(name="id_setor")
	private Setor setor;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public long getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(long quantidade) {
		this.quantidade = quantidade;
	}
	public Setor getSetor() {
		return setor;
	}
	public void setSetor(Setor setor) {
		this.setor = setor;
	}
	
	@Override
	public String toString() {
		return this.nome + " - " + this.codigo;
	}
	
	@Override
	public int compareTo(Acao outraAcao) {
		// TODO Auto-generated method stub
		return this.nome.compareTo(outraAcao.nome);
	}
}
