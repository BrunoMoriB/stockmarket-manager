package com.bolsavalores.entities;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class PesoBalancoIdentity implements Serializable{

	public PesoBalancoIdentity(){
		
	}
	
	public PesoBalancoIdentity(String indicador, int peso) {
		this.indicador = indicador;
		this.peso 	   = peso;
	}
	
	@NotNull
	@Size(max=75)
	private String indicador;
	
	@NotNull
	private int peso;
	
	public String getIndicador() {
		return indicador;
	}
	public void setIndicador(String indicador) {
		this.indicador = indicador;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((indicador == null) ? 0 : indicador.hashCode());
		result = prime * result + peso;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PesoBalancoIdentity other = (PesoBalancoIdentity) obj;
		if (indicador == null) {
			if (other.indicador != null)
				return false;
		} else if (!indicador.equals(other.indicador))
			return false;
		if (peso != other.peso)
			return false;
		return true;
	}
	
}
