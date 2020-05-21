package com.bolsavalores.models.b3;

import java.time.LocalTime;

public class LstQtn implements Comparable<LstQtn>{
	
	private Double closPric;
	private LocalTime dtTm;
	private Double prcFlcn;
	
	public Double getClosPric() {
		return closPric;
	}
	public void setClosPric(Double closPric) {
		this.closPric = closPric;
	}
	public LocalTime getDtTm() {
		return dtTm;
	}
	public void setDtTm(LocalTime dtTm) {
		this.dtTm = dtTm;
	}
	public Double getPrcFlcn() {
		return prcFlcn;
	}
	public void setPrcFlcn(Double prcFlcn) {
		this.prcFlcn = prcFlcn;
	}
	
	@Override
	public int compareTo(LstQtn outraLstQtn) {
		// TODO Auto-generated method stub
		return this.dtTm.compareTo(outraLstQtn.getDtTm());
	}
}
