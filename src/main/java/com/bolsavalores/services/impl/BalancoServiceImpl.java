package com.bolsavalores.services.impl;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolsavalores.clients.B3Client;
import com.bolsavalores.helpers.CalculadoraFundamentalista;
import com.bolsavalores.helpers.JsonConverter;
import com.bolsavalores.models.Acao;
import com.bolsavalores.models.Balanco;
import com.bolsavalores.models.DesempenhoFinanceiro;
import com.bolsavalores.models.MultiplosFundamentalistas;
import com.bolsavalores.models.b3.LstQtn;
import com.bolsavalores.models.exceptions.B3ClientInfoException;
import com.bolsavalores.models.exceptions.BalancoNotFoundException;
import com.bolsavalores.repositories.AcaoRepository;
import com.bolsavalores.repositories.BalancoRepository;
import com.bolsavalores.services.BalancoService;

@Service
public class BalancoServiceImpl implements BalancoService{

	@Autowired
	BalancoRepository balancoRepository;
	
	@Autowired
	AcaoRepository acaoRepository;
	
	@Autowired
	CalculadoraFundamentalista calculadoraFundamentalista;
	
	@Autowired
	JsonConverter jsonConverter;
	
	@Autowired
	B3Client b3Client;
	
	@Override
	public List<Balanco> getBalancosRecalculadosByAcaoId(long acaoId) throws ParseException, BalancoNotFoundException, B3ClientInfoException {
		List<Balanco> balancos = balancoRepository.findByAcaoId(acaoId);
		
		Collections.sort(balancos);
		boolean hasDailyUpdated = false;
		
		for(Balanco balanco : balancos) {
			if(balanco.isDailyUpdated()) {
				hasDailyUpdated = true;
				salvaBalancoDailyUpdated(balanco);
			}else {
				salvaBalanco(balanco);
			}
		}
		
		if(!hasDailyUpdated) {
			Balanco balancoDailyUpdated = new Balanco();
			balancoDailyUpdated.setAcao(getAcao(balancos, acaoId));
			salvaBalancoDailyUpdated(balancoDailyUpdated);
		}
		
		return balancos;
	}
	
	private Acao getAcao(List<Balanco> balancos, long acaoId) {
		if(balancos != null && !balancos.isEmpty())
			return balancos.get(0).getAcao();
		else
			return acaoRepository.findById(acaoId);
	}

	@Override
	public Balanco salvaBalanco(Balanco balanco) throws ParseException {
		List<Balanco> balancosAnteriores = balancoRepository.findByAcaoId(balanco.getAcao().getId());
	
		balanco.setLucroLiquidoAnual(calculadoraFundamentalista.getLucroLiquidoAnual(balanco, balancosAnteriores));
		
		MultiplosFundamentalistas multiplos = balanco.getMultiplosFundamentalistas() != null ? balanco.getMultiplosFundamentalistas() : new MultiplosFundamentalistas();
		multiplos.setPrecoSobreLucro(calculadoraFundamentalista.getPrecoSobreLucro(balanco.getCotacao(), balanco.getAcao().getQuantidade(), balanco.getLucroLiquidoAnual()));
		multiplos.setPrecoSobreValorPatrimonial(calculadoraFundamentalista.getPrecoSobreValorPatrimonial(balanco.getCotacao(), balanco.getAcao().getQuantidade(), balanco.getPatrimonioLiquido()));
		multiplos.setRoe(calculadoraFundamentalista.getRoe(balanco.getLucroLiquidoAnual(), balanco.getPatrimonioLiquido()));
		multiplos.setDividaBrutaSobrePatrimonioLiquido(calculadoraFundamentalista.getDividaBrutaSobrePatrimonioLiquido(balanco.getDividaBruta(), balanco.getPatrimonioLiquido()));
		multiplos.setCaixaDisponivelSobreDividaBruta(calculadoraFundamentalista.getCaixaDisponivelSobreDividaBruta(balanco.getCaixaDisponivel(), balanco.getDividaBruta()));
		multiplos.setMediaPrecoSobreLucro(calculadoraFundamentalista.getMediaPrecoSobreLucro(balanco, multiplos.getPrecoSobreLucro(), balancosAnteriores));
		multiplos.setMediaPrecoSobreValorPatrimonial(calculadoraFundamentalista.getMediaPrecoSobreValorPatrimonial(balanco, multiplos.getPrecoSobreValorPatrimonial(), balancosAnteriores));
		balanco.setMultiplosFundamentalistas(multiplos);
		
		DesempenhoFinanceiro desempenho = balanco.getDesempenhoFinanceiro() != null ? balanco.getDesempenhoFinanceiro() : new DesempenhoFinanceiro();
		desempenho.setEvolucaoLucroLiquidoTrimestral(calculadoraFundamentalista.getEvolucaoLucroLiquidoTrimestral(balanco, balancosAnteriores));
		desempenho.setEvolucaoLucroLiquidoAnual(calculadoraFundamentalista.getEvolucaoLucroLiquidoAnual(balanco, balancosAnteriores));
		desempenho.setHasCrescimentoLucroLiquidoTresAnos(calculadoraFundamentalista.hasLucroCrescenteTresAnos(balanco, balancosAnteriores));
		balanco.setDesempenhoFinanceiro(desempenho);
		
		balanco.setNota(calculadoraFundamentalista.getNota(balanco));
		balanco.setTrimestre(getTrimestre(balanco.getData()));
		balanco.setDailyUpdated(false);
		
		return balancoRepository.save(balanco);
	}
	
	@Override
	public Balanco salvaBalancoDailyUpdated(Balanco balancoDailyUpdate) throws ParseException, BalancoNotFoundException, B3ClientInfoException  {
		LstQtn cotacaoAtual = b3Client.getCotacaoMaisAtualByCodigoAcao(balancoDailyUpdate.getAcao().getCodigo());
		double precoAtual   = cotacaoAtual.getClosPric();
		
		Balanco ultimoBalanco = balancoRepository.findLastBalancoByAcaoId(balancoDailyUpdate.getAcao().getId());
		
		if(ultimoBalanco == null)
			throw new BalancoNotFoundException("Não existe Balanço cadastrado para Ação(id " + balancoDailyUpdate.getAcao().getId() + "). ");
		
		balancoDailyUpdate.setCotacao(precoAtual);
		balancoDailyUpdate.setCaixaDisponivel(ultimoBalanco.getCaixaDisponivel());
		balancoDailyUpdate.setDailyUpdated(true);
		balancoDailyUpdate.setData(LocalDate.now());
		balancoDailyUpdate.setDividaBruta(ultimoBalanco.getDividaBruta());
		balancoDailyUpdate.setLucroLiquidoAnual(ultimoBalanco.getLucroLiquidoAnual());
		balancoDailyUpdate.setLucroLiquidoTrimestral(ultimoBalanco.getLucroLiquidoTrimestral());
		balancoDailyUpdate.setPatrimonioLiquido(ultimoBalanco.getPatrimonioLiquido());
		balancoDailyUpdate.setTrimestre(balancoDailyUpdate.getData().toString());
		
		DesempenhoFinanceiro desempenho = balancoDailyUpdate.getDesempenhoFinanceiro() != null ? balancoDailyUpdate.getDesempenhoFinanceiro() : new DesempenhoFinanceiro();
		desempenho.setEvolucaoLucroLiquidoAnual(ultimoBalanco.getDesempenhoFinanceiro().getEvolucaoLucroLiquidoAnual());
		desempenho.setEvolucaoLucroLiquidoTrimestral(ultimoBalanco.getDesempenhoFinanceiro().getEvolucaoLucroLiquidoTrimestral());
		desempenho.setHasCrescimentoLucroLiquidoTresAnos(ultimoBalanco.getDesempenhoFinanceiro().getHasCrescimentoLucroLiquidoTresAnos());
		balancoDailyUpdate.setDesempenhoFinanceiro(desempenho);
		
		MultiplosFundamentalistas multiplos = balancoDailyUpdate.getMultiplosFundamentalistas() != null ? balancoDailyUpdate.getMultiplosFundamentalistas() : new MultiplosFundamentalistas();
		multiplos.setCaixaDisponivelSobreDividaBruta(ultimoBalanco.getMultiplosFundamentalistas().getCaixaDisponivelSobreDividaBruta());
		multiplos.setDividaBrutaSobrePatrimonioLiquido(ultimoBalanco.getMultiplosFundamentalistas().getDividaBrutaSobrePatrimonioLiquido());
		multiplos.setMediaPrecoSobreLucro(ultimoBalanco.getMultiplosFundamentalistas().getMediaPrecoSobreLucro());
		multiplos.setMediaPrecoSobreValorPatrimonial(ultimoBalanco.getMultiplosFundamentalistas().getMediaPrecoSobreValorPatrimonial());
		multiplos.setRoe(ultimoBalanco.getMultiplosFundamentalistas().getRoe());
		
		multiplos.setPrecoSobreLucro(calculadoraFundamentalista.getPrecoSobreLucro(balancoDailyUpdate.getCotacao(), balancoDailyUpdate.getAcao().getQuantidade(), balancoDailyUpdate.getLucroLiquidoAnual()));
		multiplos.setPrecoSobreValorPatrimonial(calculadoraFundamentalista.getPrecoSobreValorPatrimonial(balancoDailyUpdate.getCotacao(), balancoDailyUpdate.getAcao().getQuantidade(), balancoDailyUpdate.getPatrimonioLiquido()));
		
		balancoDailyUpdate.setMultiplosFundamentalistas(multiplos);
		balancoDailyUpdate.setNota(calculadoraFundamentalista.getNota(balancoDailyUpdate));
		
		return balancoRepository.save(balancoDailyUpdate);
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
