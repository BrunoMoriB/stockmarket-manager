package com.bolsavalores.pesobalanco;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="table_balanco")
public class PesoBalanco {
	
	@EmbeddedId
	private PesoBalancoIdentity pesoBalancoIdentity;
	
	@NotNull
	@Size(max=3)
	private String operador;
	
	@NotNull
	private double objetivo;
	
	@NotNull
	private int nota;
	
	
	public String getOperador() {
		return operador;
	}
	public void setOperador(String operador) {
		this.operador = operador;
	}
	public double getObjetivo() {
		return objetivo;
	}
	public void setObjetivo(double objetivo) {
		this.objetivo = objetivo;
	}
	public int getNota() {
		return nota;
	}
	public void setNota(int nota) {
		this.nota = nota;
	}
	public PesoBalancoIdentity getPesoBalancoIdentity() {
		return pesoBalancoIdentity;
	}
	public void setPesoBalancoIdentity(PesoBalancoIdentity pesoBalancoIdentity) {
		this.pesoBalancoIdentity = pesoBalancoIdentity;
	}
	
}
