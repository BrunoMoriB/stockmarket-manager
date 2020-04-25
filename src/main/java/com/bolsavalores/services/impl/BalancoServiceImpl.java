package com.bolsavalores.services.impl;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolsavalores.entities.Balanco;
import com.bolsavalores.helpers.CalculadoraFundamentalista;
import com.bolsavalores.repositories.BalancoRepository;
import com.bolsavalores.services.BalancoService;

@Service
public class BalancoServiceImpl implements BalancoService{

	@Autowired
	BalancoRepository balancoRepository;
	
	@Autowired
	CalculadoraFundamentalista calculadoraFundamentalista;

	@Override
	public List<Balanco> getBalancosRecalculadosByAcaoId(long acaoId) throws ParseException {
		List<Balanco> balancos = balancoRepository.findByAcaoId(acaoId);
		
		Collections.sort(balancos);
		
		for(Balanco balanco : balancos)
			salvaBalanco(balanco);
		
		return balancos;
	}

	@Override
	public Balanco salvaBalanco(Balanco balanco) throws ParseException {
		List<Balanco> balancosAnteriores = balancoRepository.findByAcaoId(balanco.getAcao().getId());
		
		balanco.setEvolucaoLucroLiquidoMeses(calculadoraFundamentalista.getEvolucaoLucroLiquidoTrimestral(balanco, balancosAnteriores)); 
		balanco.setIsLucroCrescenteTresMeses(calculadoraFundamentalista.isLucroCrescenteTresMeses(balanco, balancosAnteriores));
		balanco.setLucroLiquidoAnual(calculadoraFundamentalista.getLucroLiquidoAnual(balanco, balancosAnteriores)); 
 		balanco.setEvolucaoLucroLiquidoAnos(calculadoraFundamentalista.getEvolucaoLucroLiquidoAnual(balanco, balancosAnteriores));
		balanco.setIsLucroCrescenteTresAnos(calculadoraFundamentalista.isLucroCrescenteTresAnos(balanco, balancosAnteriores));
		balanco.setMediaPrecoSobreLucro(calculadoraFundamentalista.getMediaPrecoSobreLucro(balanco, balancosAnteriores));
		balanco.setMediaPrecoSobreValorPatrimonial(calculadoraFundamentalista.getMediaPrecoSobreValorPatrimonial(balanco, balancosAnteriores));
		balanco.setNota(calculadoraFundamentalista.getNota(balanco));
		
		return balancoRepository.save(balanco);
	}
}
