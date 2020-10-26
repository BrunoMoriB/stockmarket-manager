package com.bolsavalores.services;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolsavalores.clients.B3Client;
import com.bolsavalores.helpers.Avaliacao;
import com.bolsavalores.helpers.CalculadoraFundamentalista;
import com.bolsavalores.helpers.DataBalancoUtils;
import com.bolsavalores.helpers.JsonConverter;
import com.bolsavalores.models.Acao;
import com.bolsavalores.models.Balanco;
import com.bolsavalores.models.Cotacao;
import com.bolsavalores.models.DesempenhoFinanceiro;
import com.bolsavalores.models.MultiplosFundamentalistas;
import com.bolsavalores.models.b3.LstQtn;
import com.bolsavalores.repositories.AcaoRepository;
import com.bolsavalores.repositories.BalancoRepository;
import com.bolsavalores.repositories.CotacaoRepository;
import com.bolsavalores.models.exceptions.B3ClientInfoException;
import com.bolsavalores.models.exceptions.BalancoNotFoundException;

@Service
public class BalancoService{

    private static final Logger LOG = LoggerFactory.getLogger(BalancoService.class);

	
	@Autowired
	BalancoRepository balancoRepository;
	
	@Autowired
	CalculadoraFundamentalista calculadoraFundamentalista;
	
	@Autowired
	JsonConverter jsonConverter;
	
	@Autowired
	DataBalancoUtils dataBalancoUtils;
	
	@Autowired
	B3Client b3Client;

	@Autowired
	AcaoRepository acaoRepository;
	
	@Autowired
	CotacaoRepository cotacaoRepository;
	
	public List<Balanco> getBalancosRecalculadosByAcaoId(long acaoId) throws ParseException, B3ClientInfoException, BalancoNotFoundException {
		Acao acao = acaoRepository.findById(acaoId);
		List<Balanco> balancos = balancoRepository.findByEmpresaId(acao.getEmpresa().getId());
		
		if(balancos == null || balancos.isEmpty())
			return balancos;
		
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
		
		Set<MultiplosFundamentalistas> multiplosFundamentalistas = new HashSet<MultiplosFundamentalistas>();
		
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
		desempenho.setBalanco(balanco);
		balanco.setDesempenhoFinanceiro(desempenho);
		
		balanco.getEmpresa().getAcoes().stream().filter(a -> balanco.getMultiplosFundamentalistasByAcaoId(a.getId()) != null).forEach(a -> {
			Avaliacao avaliacao = calculadoraFundamentalista.getNota(balanco, balanco.getMultiplosFundamentalistasByAcaoId(a.getId()));
			balanco.getMultiplosFundamentalistasByAcaoId(a.getId()).setNota(avaliacao.getNota());
			balanco.getMultiplosFundamentalistasByAcaoId(a.getId()).setJustificativaNota(avaliacao.getJustificativa());
		});
		
//		balanco.setData(getData(balanco.getTrimestre()));
		balanco.setDailyUpdated(false);
		
		balanco.getEmpresa().getAcoes().forEach(a -> acaoRepository.save(a));
		return balancoRepository.save(balanco);
	}
	
	private MultiplosFundamentalistas buildMultiplosFundamentalistas(MultiplosFundamentalistas multiplos, Balanco balanco, List<Balanco> balancosAnteriores, Acao acao) {
		try {
			
			multiplos.setAcao(acao);
			multiplos.setBalanco(balanco);
			
			Date dateWithDayFirst = dataBalancoUtils.getDataDiaUmByTrimestreAndAno(balanco.getTrimestre(), balanco.getAno());
			Cotacao cotacao = cotacaoRepository.findCotacaoByAcaoIdAndData(acao.getId(), dateWithDayFirst);
			
			if(cotacao != null ) {
				multiplos.setPrecoSobreLucro(calculadoraFundamentalista.getPrecoSobreLucro(cotacao.getValor(), balanco.getEmpresa().getQuantidadePapeis(), balanco.getLucroLiquidoAnual()));
				multiplos.setPrecoSobreValorPatrimonial(calculadoraFundamentalista.getPrecoSobreValorPatrimonial(cotacao.getValor(), balanco.getEmpresa().getQuantidadePapeis(), balanco.getPatrimonioLiquido()));
			}else {
				multiplos.setPrecoSobreLucro(null);
				multiplos.setPrecoSobreValorPatrimonial(null);
			}
			
			multiplos.setRoe(calculadoraFundamentalista.getRoe(balanco.getLucroLiquidoAnual(), balanco.getPatrimonioLiquido()));
			multiplos.setDividaBrutaSobrePatrimonioLiquido(calculadoraFundamentalista.getDividaBrutaSobrePatrimonioLiquido(balanco.getDividaBruta(), balanco.getPatrimonioLiquido()));
			multiplos.setCaixaDisponivelSobreDividaBruta(calculadoraFundamentalista.getCaixaDisponivelSobreDividaBruta(balanco.getCaixaDisponivel(), balanco.getDividaBruta()));
			multiplos.setMediaPrecoSobreLucro(calculadoraFundamentalista.getMediaPrecoSobreLucro(balanco, multiplos.getPrecoSobreLucro(), balancosAnteriores, acao.getId()));
			multiplos.setMediaPrecoSobreValorPatrimonial(calculadoraFundamentalista.getMediaPrecoSobreValorPatrimonial(balanco, multiplos.getPrecoSobreValorPatrimonial(), balancosAnteriores, acao.getId()));
		}catch(Exception e) {
			LOG.error("NÃO FOI POSSÍVEL CONTRUIR O MULTIPLOS FUNDAMENTALISTA PARA " + multiplos.getId() + " acao " + acao.getId(), e);
		}
		
		return multiplos;
	}
	
	private MultiplosFundamentalistas buildMultiplosFundamentalistasDailyUpdated(MultiplosFundamentalistas multiplos, Balanco balancoDailyUpdate, MultiplosFundamentalistas ultimoMultiplos, double precoAtual, Acao acao) {
//		if(ultimosMultiplos == null)
//			TODO tratar;
		
		try {
			
			multiplos.setBalanco(balancoDailyUpdate);
			multiplos.setAcao(acao);
			
			multiplos.setCaixaDisponivelSobreDividaBruta(ultimoMultiplos.getCaixaDisponivelSobreDividaBruta());
			multiplos.setDividaBrutaSobrePatrimonioLiquido(ultimoMultiplos.getDividaBrutaSobrePatrimonioLiquido());
			multiplos.setMediaPrecoSobreLucro(ultimoMultiplos.getMediaPrecoSobreLucro());
			multiplos.setMediaPrecoSobreValorPatrimonial(ultimoMultiplos.getMediaPrecoSobreValorPatrimonial());
			multiplos.setRoe(ultimoMultiplos.getRoe());
			
			multiplos.setPrecoSobreLucro(calculadoraFundamentalista.getPrecoSobreLucro(precoAtual, balancoDailyUpdate.getEmpresa().getQuantidadePapeis(), balancoDailyUpdate.getLucroLiquidoAnual()));
			multiplos.setPrecoSobreValorPatrimonial(calculadoraFundamentalista.getPrecoSobreValorPatrimonial(precoAtual, balancoDailyUpdate.getEmpresa().getQuantidadePapeis(), balancoDailyUpdate.getPatrimonioLiquido()));
			
			LOG.info("CALCULO PL " + acao.getCodigo() + ": " + precoAtual + " / " +  balancoDailyUpdate.getLucroLiquidoAnual() + " / " + balancoDailyUpdate.getEmpresa().getQuantidadePapeis() + " = " + multiplos.getPrecoSobreLucro());
			
		}catch(Exception e) {
//			TODO tratar: pensar como lidar com essa situação.
		}
		
		return multiplos;
	}
	
	public Balanco getLastBalancoByEmpresaId(long empresaId) {
		List<Balanco> balancos = balancoRepository.findNotDailyUpdatedByEmpresaId(empresaId);
		return balancos != null && !balancos.isEmpty() ? balancos.get(0) : null;
	}
	
	public Balanco salvaBalancoDailyUpdated(Balanco balancoDailyUpdate, long acaoId) throws ParseException, B3ClientInfoException, BalancoNotFoundException  {
		Balanco ultimoBalanco = getLastBalancoByEmpresaId(balancoDailyUpdate.getEmpresa().getId());
		
		if(ultimoBalanco == null)
			throw new BalancoNotFoundException("Não existe Balanço cadastrado. ");
		
		balancoDailyUpdate.setCaixaDisponivel(ultimoBalanco.getCaixaDisponivel());
		balancoDailyUpdate.setDailyUpdated(true);
		balancoDailyUpdate.setTrimestre(ultimoBalanco.getTrimestre());
		balancoDailyUpdate.setAno(ultimoBalanco.getAno());
		balancoDailyUpdate.setDividaBruta(ultimoBalanco.getDividaBruta());
		balancoDailyUpdate.setLucroLiquidoAnual(ultimoBalanco.getLucroLiquidoAnual());
		balancoDailyUpdate.setLucroLiquidoTrimestral(ultimoBalanco.getLucroLiquidoTrimestral());
		balancoDailyUpdate.setPatrimonioLiquido(ultimoBalanco.getPatrimonioLiquido());
		
		DesempenhoFinanceiro desempenho = balancoDailyUpdate.getDesempenhoFinanceiro() != null ? balancoDailyUpdate.getDesempenhoFinanceiro() : new DesempenhoFinanceiro();
		desempenho.setEvolucaoLucroLiquidoAnual(ultimoBalanco.getDesempenhoFinanceiro().getEvolucaoLucroLiquidoAnual());
		desempenho.setEvolucaoLucroLiquidoTrimestral(ultimoBalanco.getDesempenhoFinanceiro().getEvolucaoLucroLiquidoTrimestral());
		desempenho.setHasCrescimentoLucroLiquidoTresAnos(ultimoBalanco.getDesempenhoFinanceiro().getHasCrescimentoLucroLiquidoTresAnos());
		desempenho.setBalanco(balancoDailyUpdate);
		balancoDailyUpdate.setDesempenhoFinanceiro(desempenho);
		
		Set<MultiplosFundamentalistas> multiplosFundamentalistas = new HashSet<MultiplosFundamentalistas>();
		if(balancoDailyUpdate.getMultiplosFundamentalistas() == null || balancoDailyUpdate.getMultiplosFundamentalistas().isEmpty()) {
			balancoDailyUpdate.getEmpresa().getAcoes().stream().forEach(a ->{
				try {
					Cotacao cotacaDailyUpdated = getCotacaoDailyUpdated(a);
					a.setCotacao(cotacaDailyUpdated);
					
					multiplosFundamentalistas.add(buildMultiplosFundamentalistasDailyUpdated(new MultiplosFundamentalistas(), 
							  balancoDailyUpdate, 
							  getUltimoMultiplosFundamentalistaByAcao(ultimoBalanco, a),
							  cotacaDailyUpdated.getValor(),
							  a));
				}catch(B3ClientInfoException e) {
					LOG.error(e.getMessage() + " - " + a.getCodigo());
				}
			} );
			
		}else {
			balancoDailyUpdate.getEmpresa().getAcoes().stream().forEach(a -> {
				try {
					Cotacao cotacaDailyUpdated = getCotacaoDailyUpdated(a);
					a.setCotacao(cotacaDailyUpdated);
					
					MultiplosFundamentalistas multiploByAcaoId = getUltimoMultiplosFundamentalistaByAcao(ultimoBalanco, a);
					MultiplosFundamentalistas multiplos = balancoDailyUpdate.getMultiplosFundamentalistas().stream().filter(m -> m.getAcao().getId() == a.getId()).findFirst().orElse(null);
					multiplosFundamentalistas.add(buildMultiplosFundamentalistasDailyUpdated(multiplos == null ? new MultiplosFundamentalistas() : multiplos, 
																							 balancoDailyUpdate, 
																							 multiploByAcaoId,
																							 cotacaDailyUpdated.getValor(),
																							 a));
					
				}catch(B3ClientInfoException e) {
					LOG.error(e.getMessage() + " - " + a.getCodigo());
				}
			});
		}
		
		balancoDailyUpdate.setMultiplosFundamentalistas(multiplosFundamentalistas);
		balancoDailyUpdate.getEmpresa().getAcoes().stream().filter(a -> balancoDailyUpdate.getMultiplosFundamentalistasByAcaoId(a.getId()) != null).forEach(a -> {
			Avaliacao avaliacao = calculadoraFundamentalista.getNota(balancoDailyUpdate, balancoDailyUpdate.getMultiplosFundamentalistasByAcaoId(a.getId()));
			balancoDailyUpdate.getMultiplosFundamentalistasByAcaoId(a.getId()).setNota(avaliacao.getNota());
			balancoDailyUpdate.getMultiplosFundamentalistasByAcaoId(a.getId()).setJustificativaNota(avaliacao.getJustificativa());
		});
		
		return balancoRepository.save(balancoDailyUpdate);
	}
	
	private Cotacao getCotacaoDailyUpdated(Acao acao) throws B3ClientInfoException {
		Cotacao cotacaoDailyUpdated = acao.getCotacaoDailyUpdated() != null ? acao.getCotacaoDailyUpdated() : new Cotacao();
		
		LstQtn cotacaoAtual = b3Client.getCotacaoMaisAtualByCodigoAcao(acao.getCodigo());
		double precoAtual   = cotacaoAtual.getClosPric();
		
		cotacaoDailyUpdated.setAcao(acao);
		cotacaoDailyUpdated.setDailyUpdated(true);
		cotacaoDailyUpdated.setData(LocalDate.now());
		cotacaoDailyUpdated.setValor(precoAtual);
		return cotacaoDailyUpdated;
	}
	
	private MultiplosFundamentalistas getUltimoMultiplosFundamentalistaByAcao(Balanco balanco, Acao acao) {
		MultiplosFundamentalistas multiplos = null;
		try {
			multiplos = balanco.getMultiplosFundamentalistas().stream().filter(m -> m.getAcao().getId() == acao.getId()).findFirst().orElse(null);
		}catch(Exception e) {
			LOG.error("ERROR >>> balanco: " + balanco.getId(), e);
		}
		
		return multiplos;
	}
	
	/*	TODO analisar se isto fica 
	 * private Acao getAcao(List<Balanco> balancos, long acaoId) {
		if(balancos != null && !balancos.isEmpty())
			return balancos.get(0).getAcao();
		else
			return acaoRepository.findById(acaoId);
	}*/
	
	/*private String getTrimestre(LocalDate data) {
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
	}*/

}
