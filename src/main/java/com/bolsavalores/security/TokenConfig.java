package com.bolsavalores.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("token")
public class TokenConfig {
	private String jwtSegredo;
    private long diasDeVida;
    private String jwtIssuer;
    
	public String getJwtSegredo() {
		return jwtSegredo;
	}
	public void setJwtSegredo(String jwtSegredo) {
		this.jwtSegredo = jwtSegredo;
	}
	public long getDiasDeVida() {
		return diasDeVida;
	}
	public void setDiasDeVida(long diasDeVida) {
		this.diasDeVida = diasDeVida;
	}
	public String getJwtIssuer() {
		return jwtIssuer;
	}
	public void setJwtIssuer(String jwtIssuer) {
		this.jwtIssuer = jwtIssuer;
	}
}
