package com.bolsavalores.setor;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="setor")
public class Setor implements Serializable, Comparable<Setor> {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private String nome;
	private String subsetor;

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
	
	public String getSubsetor() {
		return subsetor;
	}

	public void setSubsetor(String subsetor) {
		this.subsetor = subsetor;
	}

	@Override
	public int compareTo(Setor outroSetor) {
		// TODO Auto-generated method stub
		return this.nome.compareTo(outroSetor.nome);
	}
}
