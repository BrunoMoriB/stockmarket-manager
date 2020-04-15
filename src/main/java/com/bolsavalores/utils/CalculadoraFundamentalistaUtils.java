package com.bolsavalores.utils;

import org.springframework.beans.factory.annotation.Autowired;

import com.bolsavalores.entities.Balanco;
import com.bolsavalores.repositories.BalancoRepository;

public class CalculadoraFundamentalistaUtils {
	
	@Autowired
	BalancoRepository balancoRepository;
	
	public static String getEvolucaoLucroLiquidoTrimestral(Balanco balanco) {
		return null;
	}
	
}
