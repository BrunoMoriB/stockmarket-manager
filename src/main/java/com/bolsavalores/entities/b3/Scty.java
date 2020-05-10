package com.bolsavalores.entities.b3;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Scty {
	
	private List<LstQtn> lstQtn;
	private String symb;
	
	public String getSymb() {
		return symb;
	}
	public void setSymb(String symb) {
		this.symb = symb;
	}
	
	@JsonProperty("lstQtn")
	public List<LstQtn> getLstQtn() {
		return lstQtn;
	}
	public void setLstQtn(List<LstQtn> lstQtn) {
		this.lstQtn = lstQtn;
	}
}
