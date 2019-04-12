package com.bolsavalores.pesobalanco;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="peso_balanco")
public class PesoBalanco {
	
	@EmbeddedId
	private PesoBalancoIdentity pesoBalancoIdentity;
	
	@NotNull
	@Size(max=3)
	private String operador;
	
	@NotNull
	private double objetivo;
	
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
	public PesoBalancoIdentity getPesoBalancoIdentity() {
		return pesoBalancoIdentity;
	}
	public void setPesoBalancoIdentity(PesoBalancoIdentity pesoBalancoIdentity) {
		this.pesoBalancoIdentity = pesoBalancoIdentity;
	}
	
}
