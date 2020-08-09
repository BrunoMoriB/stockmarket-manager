package com.bolsavalores.services;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolsavalores.clients.B3Client;
import com.bolsavalores.helpers.Avaliacao;
import com.bolsavalores.helpers.CalculadoraFundamentalista;
import com.bolsavalores.helpers.JsonConverter;
import com.bolsavalores.models.Acao;
import com.bolsavalores.models.Balanco;
import com.bolsavalores.models.DesempenhoFinanceiro;
import com.bolsavalores.models.MultiplosFundamentalistas;
import com.bolsavalores.models.b3.LstQtn;
import com.bolsavalores.repositories.AcaoRepository;
import com.bolsavalores.repositories.BalancoRepository;

@Service
public class BalancoService{

	@Autowired
	BalancoRepository balancoRepository;
	
	@Autowired
	CalculadoraFundamentalista calculadoraFundamentalista;
	
	@Autowired
	JsonConverter jsonConverter;
	
	@Autowired
	B3Client b3Client;

	@Autowired
	AcaoRepository acaoRepository;
	
	public List<Balanco> getBalancosRecalculadosByAcaoId(long acaoId) throws ParseException {
		Acao acao = acaoRepository.findById(acaoId);
		List<Balanco> balancos = balancoRepository.findByEmpresaId(acao.getEmpresa().getId());
		
		Collections.sort(balancos);
		boolean hasDailyUpdated = false;
		
		for(Balanco balanco : balancos) {
			if(balanco.isDailyUpdated()) {
				hasDailyUpdated = true;
				salvaBalancoDailyUpdated(balanco, acaoId);
			}else {
				salvaBalanco(balanco);
			}
		}
		
		if(!hasDailyUpdated) {
			Balanco balancoDailyUpdated = new Balanco(balancos.get(0).getEmpresa());
			salvaBalancoDailyUpdated(balancoDailyUpdated, acaoId);
		}
		
		return balancos;
	}

	public Balanco salvaBalanco(Balanco balanco) throws ParseException {
		List<Balanco> balancosAnteriores = balancoRepository.findByEmpresaId(balanco.getEmpresa().getId());
		balanco.setLucroLiquidoAnual(calculadoraFundamentalista.getLucroLiquidoAnual(balanco, balancosAnteriores));
		
		List<MultiplosFundamentalistas> multiplosFundamentalistas = new ArrayList<MultiplosFundamentalistas>();
		if(balanco.getMultiplosFundamentalistas() == null || balanco.getMultiplosFundamentalistas().isEmpty()) {
			balanco.getEmpresa().getAcoes().stream().forEach(a -> multiplosFundamentalistas.add(buildMultiplosFundamentalistas(new MultiplosFundamentalistas(), balanco, balancosAnteriores, a)));
			
		}else {
			balanco.getEmpresa().getAcoes().stream().forEach(a -> {
				MultiplosFundamentalistas multiplos = balanco.getMultiplosFundamentalistas().stream().filter(m -> m.getAcao().getId() == a.getId()).findFirst().orElse(null);
				multiplosFundamentalistas.add(buildMultiplosFundamentalistas(multiplos == null ? new MultiplosFundamentalistas() : multiplos, 
						 													  balanco, 
						 													  balancosAnteriores, 
						 													  a));
			});
		}
		
		balanco.setMultiplosFundamentalistas(multiplosFundamentalistas);
		
		DesempenhoFinanceiro desempenho = balanco.getDesempenhoFinanceiro() != null ? balanco.getDesempenhoFinanceiro() : new DesempenhoFinanceiro();
		desempenho.setEvolucaoLucroLiquidoTrimestral(calculadoraFundamentalista.getEvolucaoLucroLiquidoTrimestral(balanco, balancosAnteriores));
		desempenho.setEvolucaoLucroLiquidoAnual(calculadoraFundamentalista.getEvolucaoLucroLiquidoAnual(balanco, balancosAnteriores));
		desempenho.setHasCrescimentoLucroLiquidoTresAnos(calculadoraFundamentalista.hasLucroCrescenteTresAnos(balanco, balancosAnteriores));
		balanco.setDesempenhoFinanceiro(desempenho);
		
		balanco.getEmpresa().getAcoes().stream().forEach(a -> {
			Avaliacao avaliacao = calculadoraFundamentalista.getNota(balanco, balanco.getMultiplosFundamentalistasByAcaoId(a.getId()));
			balanco.getMultiplosFundamentalistasByAcaoId(a.getId()).setNota(avaliacao.getNota());
			balanco.getMultiplosFundamentalistasByAcaoId(a.getId()).setJustificativaNota(avaliacao.getJustificativa());
		});
		
		balanco.setTrimestre(getTrimestre(balanco.getData()));
		balanco.setDailyUpdated(false);
		
		return balancoRepository.save(balanco);
	}
	
	private MultiplosFundamentalistas buildMultiplosFundamentalistas(MultiplosFundamentalistas multiplos, Balanco balanco, List<Balanco> balancosAnteriores, Acao acao) {
		try {
			
//			TODO futuro ajuste quando existe tabela COTAÇÃO;
			
			multiplos.setPrecoSobreLucro(calculadoraFundamentalista.getPrecoSobreLucro(balanco.getCotacao(), balanco.getQtdPapeis(), balanco.getLucroLiquidoAnual()));
			multiplos.setPrecoSobreValorPatrimonial(calculadoraFundamentalista.getPrecoSobreValorPatrimonial(balanco.getCotacao(), balanco.getQtdPapeis(), balanco.getPatrimonioLiquido()));
			multiplos.setRoe(calculadoraFundamentalista.getRoe(balanco.getLucroLiquidoAnual(), balanco.getPatrimonioLiquido()));
			multiplos.setDividaBrutaSobrePatrimonioLiquido(calculadoraFundamentalista.getDividaBrutaSobrePatrimonioLiquido(balanco.getDividaBruta(), balanco.getPatrimonioLiquido()));
			multiplos.setCaixaDisponivelSobreDividaBruta(calculadoraFundamentalista.getCaixaDisponivelSobreDividaBruta(balanco.getCaixaDisponivel(), balanco.getDividaBruta()));
			multiplos.setMediaPrecoSobreLucro(calculadoraFundamentalista.getMediaPrecoSobreLucro(balanco, multiplos.getPrecoSobreLucro(), balancosAnteriores, acao.getId()));
			multiplos.setMediaPrecoSobreValorPatrimonial(calculadoraFundamentalista.getMediaPrecoSobreValorPatrimonial(balanco, multiplos.getPrecoSobreValorPatrimonial(), balancosAnteriores, acao.getId()));
			multiplos.setAcao(acao);
			multiplos.setBalanco(balanco);
		}catch(Exception e) {
//			TODO tratar: pensar como lidar com essa situação.
		}
		
		return multiplos;
	}
	
	private MultiplosFundamentalistas buildMultiplosFundamentalistasDailyUpdated(MultiplosFundamentalistas multiplos, Balanco balancoDailyUpdate, MultiplosFundamentalistas ultimoMultiplos) {
//		if(ultimosMultiplos == null)
//			TODO tratar;
		
		try {
			
//			TODO futuro ajuste quando existe tabela COTAÇÃO;
			
			multiplos.setCaixaDisponivelSobreDividaBruta(ultimoMultiplos.getCaixaDisponivelSobreDividaBruta());
			multiplos.setDividaBrutaSobrePatrimonioLiquido(ultimoMultiplos.getDividaBrutaSobrePatrimonioLiquido());
			multiplos.setMediaPrecoSobreLucro(ultimoMultiplos.getMediaPrecoSobreLucro());
			multiplos.setMediaPrecoSobreValorPatrimonial(ultimoMultiplos.getMediaPrecoSobreValorPatrimonial());
			multiplos.setRoe(ultimoMultiplos.getRoe());
			
			multiplos.setPrecoSobreLucro(calculadoraFundamentalista.getPrecoSobreLucro(balancoDailyUpdate.getCotacao(), balancoDailyUpdate.getQtdPapeis(), balancoDailyUpdate.getLucroLiquidoAnual()));
			multiplos.setPrecoSobreValorPatrimonial(calculadoraFundamentalista.getPrecoSobreValorPatrimonial(balancoDailyUpdate.getCotacao(), balancoDailyUpdate.getQtdPapeis(), balancoDailyUpdate.getPatrimonioLiquido()));
		}catch(Exception e) {
//			TODO tratar: pensar como lidar com essa situação.
		}
		
		return multiplos;
	}
	
	public Balanco salvaBalancoDailyUpdated(Balanco balancoDailyUpdate, long acaoId) throws ParseException  {
		Acao acao = acaoRepository.findById(acaoId);
		LstQtn cotacaoAtual = b3Client.getCotacaoMaisAtualByCodigoAcao(acao.getCodigo());
		double precoAtual   = cotacaoAtual.getClosPric();
		
		Balanco ultimoBalanco = balancoRepository.findLastBalancoByEmpresaId(balancoDailyUpdate.getEmpresa().getId());
		
		balancoDailyUpdate.setCotacao(precoAtual);
		balancoDailyUpdate.setCaixaDisponivel(ultimoBalanco.getCaixaDisponivel());
		balancoDailyUpdate.setDailyUpdated(true);
		balancoDailyUpdate.setData(LocalDate.now());
		balancoDailyUpdate.setDividaBruta(ultimoBalanco.getDividaBruta());
		balancoDailyUpdate.setLucroLiquidoAnual(ultimoBalanco.getLucroLiquidoAnual());
		balancoDailyUpdate.setLucroLiquidoTrimestral(ultimoBalanco.getLucroLiquidoTrimestral());
		balancoDailyUpdate.setPatrimonioLiquido(ultimoBalanco.getPatrimonioLiquido());
		balancoDailyUpdate.setQtdPapeis(ultimoBalanco.getQtdPapeis());
		balancoDailyUpdate.setTrimestre(balancoDailyUpdate.getData().toString());
		
		DesempenhoFinanceiro desempenho = balancoDailyUpdate.getDesempenhoFinanceiro() != null ? balancoDailyUpdate.getDesempenhoFinanceiro() : new DesempenhoFinanceiro();
		desempenho.setEvolucaoLucroLiquidoAnual(ultimoBalanco.getDesempenhoFinanceiro().getEvolucaoLucroLiquidoAnual());
		desempenho.setEvolucaoLucroLiquidoTrimestral(ultimoBalanco.getDesempenhoFinanceiro().getEvolucaoLucroLiquidoTrimestral());
		desempenho.setHasCrescimentoLucroLiquidoTresAnos(ultimoBalanco.getDesempenhoFinanceiro().getHasCrescimentoLucroLiquidoTresAnos());
		balancoDailyUpdate.setDesempenhoFinanceiro(desempenho);
		
		List<MultiplosFundamentalistas> multiplosFundamentalistas = new ArrayList<MultiplosFundamentalistas>();
		if(balancoDailyUpdate.getMultiplosFundamentalistas() == null || balancoDailyUpdate.getMultiplosFundamentalistas().isEmpty()) {
			balancoDailyUpdate.getEmpresa().getAcoes().stream().forEach(a -> multiplosFundamentalistas.add(buildMultiplosFundamentalistasDailyUpdated(new MultiplosFundamentalistas(), 
																																					  balancoDailyUpdate, 
																																					  getUltimoMultiplosFundamentalistaByAcao(ultimoBalanco, a))));
			
		}else {
			balancoDailyUpdate.getEmpresa().getAcoes().stream().forEach(a -> {
				MultiplosFundamentalistas multiplos = balancoDailyUpdate.getMultiplosFundamentalistas().stream().filter(m -> m.getAcao().getId() == a.getId()).findFirst().orElse(null);
				multiplosFundamentalistas.add(buildMultiplosFundamentalistasDailyUpdated(multiplos == null ? new MultiplosFundamentalistas() : multiplos, 
																						 balancoDailyUpdate, 
																						 getUltimoMultiplosFundamentalistaByAcao(ultimoBalanco, a)));
			});
		}
		
		balancoDailyUpdate.setMultiplosFundamentalistas(multiplosFundamentalistas);
		balancoDailyUpdate.getEmpresa().getAcoes().stream().forEach(a -> {
			Avaliacao avaliacao = calculadoraFundamentalista.getNota(balancoDailyUpdate, balancoDailyUpdate.getMultiplosFundamentalistasByAcaoId(a.getId()));
			balancoDailyUpdate.getMultiplosFundamentalistasByAcaoId(a.getId()).setNota(avaliacao.getNota());
			balancoDailyUpdate.getMultiplosFundamentalistasByAcaoId(a.getId()).setJustificativaNota(avaliacao.getJustificativa());
		});
		
		return balancoRepository.save(balancoDailyUpdate);
	}
	
	private MultiplosFundamentalistas getUltimoMultiplosFundamentalistaByAcao(Balanco balanco, Acao acao) {
		return balanco.getMultiplosFundamentalistas().stream().filter(m -> m.getAcao().getId() == acao.getId()).findFirst().orElse(null);
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
