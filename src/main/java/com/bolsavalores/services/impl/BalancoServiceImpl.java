package com.bolsavalores.services.impl;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolsavalores.entities.Balanco;
import com.bolsavalores.entities.DesempenhoFinanceiro;
import com.bolsavalores.entities.MultiplosFundamentalistas;
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
	
		balanco.setLucroLiquidoAnual(calculadoraFundamentalista.getLucroLiquidoAnual(balanco, balancosAnteriores));
		
		MultiplosFundamentalistas multiplos = balanco.getMultiplosFundamentalistas() != null ? balanco.getMultiplosFundamentalistas() : new MultiplosFundamentalistas();
		multiplos.setPrecoSobreLucro(calculadoraFundamentalista.getPrecoSobreLucro(balanco.getCotacao(), balanco.getQtdPapeis(), balanco.getLucroLiquidoAnual()));
		multiplos.setPrecoSobreValorPatrimonial(calculadoraFundamentalista.getPrecoSobreValorPatrimonial(balanco.getCotacao(), balanco.getQtdPapeis(), balanco.getPatrimonioLiquido()));
		multiplos.setRoe(calculadoraFundamentalista.getRoe(balanco.getLucroLiquidoAnual(), balanco.getPatrimonioLiquido()));
		multiplos.setDividaBrutaSobrePatrimonioLiquido(calculadoraFundamentalista.getDividaBrutaSobrePatrimonioLiquido(balanco.getDividaBruta(), balanco.getPatrimonioLiquido()));
		multiplos.setDividaliquida(calculadoraFundamentalista.getDividaLiquida(balanco.getDividaBruta(), balanco.getCaixaDisponivel()));
		multiplos.setMediaPrecoSobreLucro(calculadoraFundamentalista.getMediaPrecoSobreLucro(balanco, multiplos.getPrecoSobreLucro(), balancosAnteriores));
		multiplos.setMediaPrecoSobreValorPatrimonial(calculadoraFundamentalista.getMediaPrecoSobreValorPatrimonial(balanco, multiplos.getPrecoSobreValorPatrimonial(), balancosAnteriores));
		balanco.setMultiplosFundamentalistas(multiplos);
		
		DesempenhoFinanceiro desempenho = balanco.getDesempenhoFinanceiro() != null ? balanco.getDesempenhoFinanceiro() : new DesempenhoFinanceiro();
		desempenho.setEvolucaoLucroLiquidoTrimestral(calculadoraFundamentalista.getEvolucaoLucroLiquidoTrimestral(balanco, balancosAnteriores));
		desempenho.setEvolucaoLucroLiquidoAnual(calculadoraFundamentalista.getEvolucaoLucroLiquidoAnual(balanco, balancosAnteriores));
		desempenho.setEvolucaoDividaLiquidaAnual(calculadoraFundamentalista.getEvolucaoDividaLiquidaAnual(balanco, balancosAnteriores));
		desempenho.setHasCrescimentoLucroLiquidoTresTrimestres(calculadoraFundamentalista.hasLucroCrescenteTresMeses(balanco, balancosAnteriores, desempenho.getEvolucaoLucroLiquidoTrimestral()));
		desempenho.setHasCrescimentoLucroLiquidoTresAnos(calculadoraFundamentalista.hasLucroCrescenteTresAnos(balanco, balancosAnteriores));
		desempenho.setHasCrescimentoDividaLiquidaTresAnos(calculadoraFundamentalista.hasDividaLiquidaCrescenteTresAnos(balanco, balancosAnteriores, multiplos.getDividaliquida()));
		balanco.setDesempenhoFinanceiro(desempenho);
		
		balanco.setNota(calculadoraFundamentalista.getNota(balanco));
		balanco.setTrimestre(getTrimestre(balanco.getData()));
		
		return balancoRepository.save(balanco);
	}
	
	private String getTrimestre(LocalDate data) {
		Month mes = data.getMonth();
		
		if(mes.name().equals(Month.APRIL.name()) || mes.name().equals(Month.MAY.name()))
			return "1T" + data.getYear();
		else if(mes.name().equals(Month.JULY.name()) || mes.name().equals(Month.AUGUST.name()))
			return "2T" + data.getYear();
		else if(mes.name().equals(Month.OCTOBER.name()) || mes.name().equals(Month.NOVEMBER.name()))
			return "3T" + data.getYear();
		else if(mes.name().equals(Month.FEBRUARY.name()) || mes.name().equals(Month.MARCH.name()))
			return "4T" + (data.getYear() - 1);
		else
			return "";
	}
}
