package com.bolsavalores.models.b3;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TradgFlr {
	
	private LocalDate date;
	private Scty scty;
	
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	@JsonProperty("scty")
	public Scty getScty() {
		return scty;
	}
	public void setScty(Scty scty) {
		this.scty = scty;
	}
	
}
