package com.bolsavalores.resources;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bolsavalores.helpers.CalculadoraFundamentalista;
import com.bolsavalores.helpers.DataBalancoUtils;
import com.bolsavalores.helpers.JsonConverter;
import com.bolsavalores.models.Acao;
import com.bolsavalores.models.Balanco;
import com.bolsavalores.models.Cotacao;
import com.bolsavalores.models.DesempenhoFinanceiro;
import com.bolsavalores.models.Empresa;
import com.bolsavalores.models.MultiplosFundamentalistas;
import com.bolsavalores.models.Provento;
import com.bolsavalores.models.exceptions.B3ClientInfoException;
import com.bolsavalores.models.exceptions.BalancoNotFoundException;
import com.bolsavalores.models.exceptions.StockmarketException;
import com.bolsavalores.repositories.AcaoRepository;
import com.bolsavalores.repositories.BalancoRepository;
import com.bolsavalores.repositories.MultiplosFundamentalistasRepository;
import com.bolsavalores.services.BalancoService;

import lombok.AllArgsConstructor;
import lombok.Getter;

@CrossOrigin
@RestController
@RequestMapping(value="/balancos")
public class BalancoResource {

    private static final Logger LOG = LoggerFactory.getLogger(BalancoResource.class);
	
	@Autowired
	BalancoRepository balancoRepository;
	
	@Autowired
	MultiplosFundamentalistasRepository multiplosFundamentalistasRepository;
	
	@Autowired
	AcaoRepository acaoRepository;
	
	@Autowired
	BalancoService balancoService;
	
	@Autowired
	JsonConverter jsonConverter;
	
	@Autowired
	DataBalancoUtils dataBalancoUtils;

	@Autowired
	CalculadoraFundamentalista calculadoraFundamentalista;
	
	@GetMapping("/{id}")
	public ResponseEntity<String> getBalanco(@PathVariable long id) {
		try{
			Balanco balanco = balancoRepository.findById(id);
			
	//		if(balanco == null)
	//			TODO tratar
			
			BalancoResponse balancoResponse = new BalancoResponse(balanco.getId(), 
																  getEmpresaResponse(balanco.getEmpresa(), balanco.getTrimestre(), balanco.getAno(), balanco.isDailyUpdated()), 
																  getMultiplosFundamentalistaResponse(balanco.getMultiplosFundamentalistas(), 
																		  							  balanco.getTrimestre(), 
																		  							  balanco.getAno(), 
																		  							  balanco.getEmpresa().getId(),
																		  							  balanco.isDailyUpdated()), 
																  getDesempenhoFinanceiroResponse(balanco.getDesempenhoFinanceiro()), 
																  balanco.getLucroLiquidoTrimestral(), 
																  balanco.getLucroLiquidoAnual(), 
																  balanco.getPatrimonioLiquido(), 
																  balanco.getDividaBruta(), 
																  balanco.getCaixaDisponivel(), 
																  balanco.isDailyUpdated(), 
																  balanco.getTrimestre(),
																  balanco.getAno());
			
			return ResponseEntity.ok(jsonConverter.toJson(balancoResponse));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Não foi possível buscar o Balanco. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping()
	public ResponseEntity<String> getBalancos(){
		try {
			List<Balanco> balancos = balancoRepository.findAll();

	//		if(balancos == null || balancos.isEmpty())
	//			TODO tratar

			List<BalancoResponse> balancosResponse = new ArrayList<BalancoResponse>();
			balancos.stream().forEach(b -> balancosResponse.add(new BalancoResponse(b.getId(), 
																	  getEmpresaResponse(b.getEmpresa(), 
																			  			 b.getTrimestre(), 
																			  			 b.getAno(), 
																			  			 b.isDailyUpdated()), 
																	  getMultiplosFundamentalistaResponse(b.getMultiplosFundamentalistas(), 
																			  							  b.getTrimestre(), 
																			  							  b.getAno(), 
																			  							  b.getEmpresa().getId(),
																			  							  b.isDailyUpdated()), 
																	  getDesempenhoFinanceiroResponse(b.getDesempenhoFinanceiro()), 
																	  b.getLucroLiquidoTrimestral(), 
																	  b.getLucroLiquidoAnual(), 
																	  b.getPatrimonioLiquido(), 
																	  b.getDividaBruta(), 
																	  b.getCaixaDisponivel(), 
																	  b.isDailyUpdated(), 
																	  b.getTrimestre(),
																	  b.getAno())));
			
			return ResponseEntity.ok(jsonConverter.toJson(balancosResponse));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Não foi possível buscar os Balancos. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/buscaPorAcaoId")
	public ResponseEntity<String> buscaBalancosByAcaoId(@RequestParam long acaoId){
		try {
			List<Balanco> balancos = balancoRepository.findByAcaoId(acaoId);
			
//			if(balancos == null || balancos.isEmpty())
//				TODO tratar
			
			Collections.sort(balancos);
			List<BalancoResponse> balancosResponse = new ArrayList<BalancoResponse>();
			balancos.stream().forEach(b -> balancosResponse.add(new BalancoResponse(b.getId(), 
																					getEmpresaResponse(b.getEmpresa(), 
																									   b.getTrimestre(), 
																									   b.getAno(),
																									   b.isDailyUpdated()), 
																					getMultiplosFundamentalistaResponse(new HashSet<MultiplosFundamentalistas>(Arrays.asList(b.getMultiplosFundamentalistasByAcaoId(acaoId))), 
																														b.getTrimestre(), 
																														b.getAno(), 
																														b.getEmpresa().getId(),
																														b.isDailyUpdated()), 
																					getDesempenhoFinanceiroResponse(b.getDesempenhoFinanceiro()), 
																					b.getLucroLiquidoTrimestral(), 
																					b.getLucroLiquidoAnual(), 
																					b.getPatrimonioLiquido(), 
																					b.getDividaBruta(), 
																					b.getCaixaDisponivel(), 
																					b.isDailyUpdated(), 
																					b.getTrimestre(),
																					b.getAno())));			
			return ResponseEntity.ok(jsonConverter.toJson(balancosResponse));		
		} catch (Exception e) {
			LOG.error("Não foi possível buscar os Balancos. ", e);
			return new ResponseEntity<String>("Não foi possível buscar os Balancos. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/buscaBalancosRecalculados")
	public ResponseEntity<String> getBalancosRecalculados(@RequestParam long acaoId){
		try {
			List<Balanco> balancosRecalculados = balancoService.getBalancosRecalculadosByAcaoId(acaoId);
			
//			if(balancosRecalculados == null || balancosRecalculados.isEmpty())
//				TODO tratar
			
			List<BalancoResponse> balancosResponse = new ArrayList<BalancoResponse>();
			balancosRecalculados.stream().forEach(b -> balancosResponse.add(new BalancoResponse(b.getId(), 
																								getEmpresaResponse(b.getEmpresa(), 
																												   b.getTrimestre(), 
																												   b.getAno(),
																												   b.isDailyUpdated()), 
																								getMultiplosFundamentalistaResponse(b.getMultiplosFundamentalistas(), 
																																	b.getTrimestre(), 
																																	b.getAno(), 
																																	b.getEmpresa().getId(),
																																	b.isDailyUpdated()), 
																								getDesempenhoFinanceiroResponse(b.getDesempenhoFinanceiro()), 
																								b.getLucroLiquidoTrimestral(), 
																								b.getLucroLiquidoAnual(), 
																								b.getPatrimonioLiquido(), 
																								b.getDividaBruta(), 
																								b.getCaixaDisponivel(), 
																								b.isDailyUpdated(), 
																								b.getTrimestre(),
																								b.getAno())));
			return ResponseEntity.ok(jsonConverter.toJson(balancosResponse));	
		} catch (Exception e) {
			LOG.error("Não foi possível recalcular os Balancos. ", e);
			return new ResponseEntity<String>("Não foi possível recalcular os Balancos. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/buscaTodosBalancosDailyUpdatedRecalculados")
	public ResponseEntity<String> getTodosBalancosDailyUpdatedRecalculados(){
		try {
			
			LOG.info("[Recalculo Geral Balanços] Iniciando processo de atualização dos Balanços das empresas.");

			List<Acao> acoes  = acaoRepository.findAll();
			
			acoes.forEach(a -> {
				LOG.info("Iniciando fluxo do Recalculo para Ação " + a.getCodigo() + " (id " + a.getId() + "). ");
				try {
					balancoService.getBalancosRecalculadosByAcaoId(a.getId());
					LOG.info("Finalizado fluxo do Recalculo para Ação " + a.getCodigo() + " (id " + a.getId() + "). ");
				} catch (B3ClientInfoException | BalancoNotFoundException | ParseException e) {
					LOG.error("Não foi possível realizar o fluxo do Recalculo para Ação " + a.getCodigo() + " (id " + a.getId() + "). ",  e);
				}
			});
			
			LOG.info("[Recalculo Geral Balanços] Processo de atualização dos Balanços das empresas foi finalizado!");
			
			List<Balanco> balancosRecalculados = balancoRepository.findBalancosDailyUpdated();
			
			List<BalancoResponse> balancosRecalculadosResponse = new ArrayList<BalancoResponse>();
			balancosRecalculados.forEach(br -> balancosRecalculadosResponse.add(new BalancoResponse(br.getId(), 
					                                                                                getEmpresaResponse(br.getEmpresa(), 
					                                                                                				   br.getTrimestre(), 
					                                                                                				   br.getAno(),
					                                                                                				   br.isDailyUpdated()), 
					                                                                                getMultiplosFundamentalistaResponse(br.getMultiplosFundamentalistas(), 
					                                                                                									br.getTrimestre(), 
					                                                                                									br.getAno(), 
					                                                                                									br.getEmpresa().getId(),
					                                                                                									br.isDailyUpdated()), 
					                                                                                getDesempenhoFinanceiroResponse(br.getDesempenhoFinanceiro()), 
					                                                                                br.getLucroLiquidoTrimestral(), 
					                                                                                br.getLucroLiquidoAnual(), 
					                                                                                br.getPatrimonioLiquido(), 
					                                                                                br.getDividaBruta(), 
					                                                                                br.getCaixaDisponivel(), 
					                                                                                br.isDailyUpdated(), 
					                                                                                br.getTrimestre(),
					                                                                                br.getAno())));
			
			return ResponseEntity.ok(jsonConverter.toJson(balancosRecalculadosResponse));
		} catch (Exception e) {
			LOG.error("Não foi possível recalcular os Balancos. ", e);
			return new ResponseEntity<String>("Não foi possível recalcular os Balancos. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/buscaBalancosDailyUpdatedRequisitosMinimos")
	public ResponseEntity<String> getBalancosDailyUpdatedRequisitosMinimos(){
		try {
			List<MultiplosFundamentalistas> multiplosDailyUpdated = multiplosFundamentalistasRepository.findMultiplosFundamentalistasDailyUpdated();
			
			if(multiplosDailyUpdated == null || multiplosDailyUpdated.isEmpty())
				throw new StockmarketException("Nenhum balanço daily updated encontrado. ");
			
			multiplosDailyUpdated.removeIf(mf -> !calculadoraFundamentalista.isDadosBalancoValidos(mf, mf.getBalanco().getDesempenhoFinanceiro()) ||
					!calculadoraFundamentalista.validaRequisitosMinimos(mf, mf.getBalanco().getDesempenhoFinanceiro()));
					
//			Collections.sort(multiplosDailyUpdated, Balanco.Comparators.NOTA);
			
			List<BalancoResponse> balancosRecalculadosResponse = new ArrayList<BalancoResponse>();
			multiplosDailyUpdated.forEach(mf -> balancosRecalculadosResponse.add(new BalancoResponse(mf.getBalanco().getId(), 
																									 getEmpresaResponse(mf.getBalanco().getEmpresa(), 
																											 			mf.getBalanco().getTrimestre(), 
																											 			mf.getBalanco().getAno(), 
																											 			mf.getBalanco().isDailyUpdated()),
																									 getMultiplosFundamentalistaResponse(new HashSet<MultiplosFundamentalistas>(Arrays.asList(mf)), 
																											 						     mf.getBalanco().getTrimestre(), 
																											 						     mf.getBalanco().getAno(),  
																											 						     mf.getBalanco().getEmpresa().getId(), 
																											 						     mf.getBalanco().isDailyUpdated()),
																									 getDesempenhoFinanceiroResponse(mf.getBalanco().getDesempenhoFinanceiro()), 
																									 mf.getBalanco().getLucroLiquidoTrimestral(), 
																									 mf.getBalanco().getLucroLiquidoAnual(), 
																									 mf.getBalanco().getPatrimonioLiquido(), 
																									 mf.getBalanco().getDividaBruta(), 
																									 mf.getBalanco().getCaixaDisponivel(), 
																									 mf.getBalanco().isDailyUpdated(), 
																									 mf.getBalanco().getTrimestre(), 
																									 mf.getBalanco().getAno())));
			return ResponseEntity.ok(jsonConverter.toJson(balancosRecalculadosResponse));
		} catch (Exception e) {
			LOG.error("Não foi pssível encontrar os Balanços daily updated. ", e);
			return new ResponseEntity<String>("Não foi possível encontrar os Balanços daily updated. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@PostMapping()
	public ResponseEntity<String> salvaBalanco(@RequestBody Balanco balanco) {
		try {
			LOG.info("Iniciando o fluxo para salvar o Balanço. ");
			balanco = balancoService.salvaBalanco(balanco);
			BalancoResponse balancoResponse = new BalancoResponse(balanco.getId(), 
																  getEmpresaResponse(balanco.getEmpresa(), 
																		  			 balanco.getTrimestre(), 
																		  			 balanco.getAno(),
																		  			 balanco.isDailyUpdated()), 
																  getMultiplosFundamentalistaResponse(balanco.getMultiplosFundamentalistas(), 
																		  							  balanco.getTrimestre(), 
																		  							  balanco.getAno(), 
																		  							  balanco.getEmpresa().getId(),
																		  							  balanco.isDailyUpdated()), 
																  getDesempenhoFinanceiroResponse(balanco.getDesempenhoFinanceiro()), 
																  balanco.getLucroLiquidoTrimestral(), 
																  balanco.getLucroLiquidoAnual(), 
																  balanco.getPatrimonioLiquido(), 
																  balanco.getDividaBruta(), 
																  balanco.getCaixaDisponivel(), 
																  balanco.isDailyUpdated(), 
																  balanco.getTrimestre(),
																  balanco.getAno());
			
			LOG.info("O fluxo para salvar o Balanço foi finalizado. ");
			return ResponseEntity.ok(jsonConverter.toJson(balancoResponse));
		} catch (Exception e) {
			LOG.error("Não foi possível salvar o Balanço. ", e);
			return new ResponseEntity<String>("Não foi possível salvar o Balanco. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping()
	public ResponseEntity<?> deletaBalanco(@RequestParam long id) {
		try {
			balancoRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Não foi possível deletar o Balanço. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	private EmpresaResponse getEmpresaResponse(Empresa empresa, int trimestre, int ano, boolean isDailyUpdated) {
		Set<SetorResponse> setoresResponse = new HashSet<SetorResponse>();
		Set<AcaoResponse> acoesResponse = new HashSet<AcaoResponse>();
		empresa.getSetores().forEach(s -> setoresResponse.add(new SetorResponse(s.getId(), s.getNome())));
		empresa.getAcoes().forEach(a -> acoesResponse.add(new AcaoResponse(a.getId(), 
																		   a.getCodigo(), 
																		   getCotacaoResponse(a.getId(), 
																				   			  a.getCotacoes(), 
																				   			  trimestre, 
																				   			  ano,
																				   			  isDailyUpdated), 
																		   getProventosResponse(a.getId(), a.getProventos()), 
																		   new EmpresaResponse(empresa.getId(), "", "", "", 0L, null, null),
																		   a.isUnit() ? new UnitsResponse(a.getUnits().getId(), a.getUnits().getQtdOn(), a.getUnits().getQtdPn(), a.getUnits().getMultiplicador()) : null,
																		   a.isUnit())));
		
		return new EmpresaResponse(empresa.getId(), empresa.getRazaoSocial(), empresa.getNomePregao(), empresa.getCnpj(), empresa.getQuantidadePapeis(), acoesResponse, setoresResponse);
	}
	
	private Set<MultiplosFundamentalistasResponse> getMultiplosFundamentalistaResponse(Set<MultiplosFundamentalistas> listMultiplosFundamentalistas, int trimestre, int ano, long empresaId, boolean isDailyUpdated) {
		Set<MultiplosFundamentalistasResponse> listMultFundResponse = new HashSet<MultiplosFundamentalistasResponse>();
		listMultiplosFundamentalistas.stream().forEach(mf -> listMultFundResponse.add(new MultiplosFundamentalistasResponse(mf.getId(), 
																															mf.getPrecoSobreLucro(), 
																															mf.getMediaPrecoSobreLucro(), 
																															mf.getPrecoSobreValorPatrimonial(), 
																															mf.getMediaPrecoSobreValorPatrimonial(), 
																															mf.getRoe(), 
																															mf.getDividaBrutaSobrePatrimonioLiquido(), 
																															mf.getCaixaDisponivelSobreDividaBruta(), 
																															mf.getNota(),
																															mf.getJustificativaNota(),
																															new AcaoResponse(mf.getAcao().getId(), 
																																			 mf.getAcao().getCodigo(), 
																																			 getCotacaoResponse(mf.getAcao().getId(), 
																																					 	        mf.getAcao().getCotacoes(), 
																																					 	        trimestre, 
																																					 	        ano,
																																					 	        isDailyUpdated),
																																			 getProventosResponse(mf.getAcao().getId(), mf.getAcao().getProventos()),
																																			 new EmpresaResponse(empresaId, "", "", "", 0L, null, null),
																																			 mf.getAcao().isUnit() ? new UnitsResponse(mf.getAcao().getUnits().getId(), 
																																											 		   mf.getAcao().getUnits().getQtdOn(), 
																																											 		   mf.getAcao().getUnits().getQtdPn(), 
																																											 		   mf.getAcao().getUnits().getMultiplicador()): null,
																																			 mf.getAcao().isUnit()))));
		
		return listMultFundResponse;
	}
	
	private DesempenhoFinanceiroResponse getDesempenhoFinanceiroResponse(DesempenhoFinanceiro desempenhoFinanceiroResponse) {
		return desempenhoFinanceiroResponse != null ? new DesempenhoFinanceiroResponse(desempenhoFinanceiroResponse.getId(),
																					   desempenhoFinanceiroResponse.getEvolucaoLucroLiquidoTrimestral(), 
																					   desempenhoFinanceiroResponse.getEvolucaoLucroLiquidoAnual(), 
																					   desempenhoFinanceiroResponse.getHasCrescimentoLucroLiquidoTresAnos()) : null;
	}
	
	private List<CotacaoResponse> getCotacaoResponse(long acaoId, Set<Cotacao> cotacoes, int trimestre, int ano, boolean isDailyUpdated) {
		Cotacao cotacao = cotacoes.stream().filter(c -> (isDailyUpdated && isDailyUpdated == c.isDailyUpdated()) || (!isDailyUpdated && comparaDatas(c.getData(), trimestre, ano))).findFirst().orElse(null);
		return cotacao != null ? Arrays.asList(new CotacaoResponse(cotacao.getId(), cotacao.getData(), cotacao.getValor(), new AcaoResponse(acaoId, "", null, null, null, null, null))) : null;
	}
	
	private boolean comparaDatas(LocalDate dataCotacao, int trimestre, int ano) {
		Month mes = dataBalancoUtils.getMonthByTrimestre(trimestre);
		return dataCotacao.getMonth() == mes && dataCotacao.getYear() == ano;
	}
	
	private List<ProventoResponse> getProventosResponse(long acaoId, Set<Provento> proventos){
		List<ProventoResponse> proventosResponse = new ArrayList<ProventoResponse>();
		proventos.forEach(p -> proventosResponse.add(new ProventoResponse(p.getId(), p.getTipo(), p.getValor(), p.getDataEx(), p.getDataPagamento(), new AcaoResponse(acaoId, "", null, null, null, null, null))));
		return proventosResponse;
	}
	
	@Getter
    @AllArgsConstructor
	static class BalancoResponse{
		long id;
		EmpresaResponse empresa;
		Set<MultiplosFundamentalistasResponse> multiplosFundamentalistas;
		DesempenhoFinanceiroResponse desempenhoFinanceiro;
		Long lucroLiquidoTrimestral;
		Long lucroLiquidoAnual;
		Long patrimonioLiquido;
		Long dividaBruta;
		Long caixaDisponivel;
		boolean dailyUpdated;
		int trimestre;
		int ano;
	}
	
	@Getter
    @AllArgsConstructor
	static class EmpresaResponse{
		long id;
		String razaoSocial;
		String nomePregao;
		String cnpj;
		long quantidadePapeis;
		Set<AcaoResponse> acoes;
		Set<SetorResponse> setores;
	}
	
	@Getter
    @AllArgsConstructor
	static class MultiplosFundamentalistasResponse{
		long id;
		Double precoSobreLucro;
		Double mediaPrecoSobreLucro;
		Double precoSobreValorPatrimonial;
		Double mediaPrecoSobreValorPatrimonial;
		Double roe;
		Double dividaBrutaSobrePatrimonioLiquido;
		Double caixaDisponivelSobreDividaBruta;
		int nota;
		String justificativaNota;
		AcaoResponse acao;
	}
	
	@Getter
    @AllArgsConstructor
	static class DesempenhoFinanceiroResponse{
		long id;
		Double evolucaoLucroLiquidoTrimestral;
		Double evolucaoLucroLiquidoAnual;
		Boolean hasCrescimentoLucroLiquidoTresAnos;
	}
	
	@Getter
    @AllArgsConstructor
	static class SetorResponse{
		long id;
		String nome;
	}
	
	@Getter
    @AllArgsConstructor
    static class AcaoResponse {
		long id;
		String codigo;
		List<CotacaoResponse> cotacoes;
		List<ProventoResponse> proventos;
		EmpresaResponse empresa;
		UnitsResponse units;
		Boolean isUnit;
    }
	
	@Getter
	@AllArgsConstructor
	static class CotacaoResponse{
		long id;
		LocalDate data;
		double valor;
		AcaoResponse acao;
	}
	
	@Getter
	@AllArgsConstructor
	static class ProventoResponse{
		long id;
		String tipo;
		double valor;
		LocalDate dataEx;
		LocalDate dataPagamento;
		AcaoResponse acao;
	}
	
	@Getter
	@AllArgsConstructor
	static class UnitsResponse{
		long id;
		long qtdOn;
		long qtdPn;
		long multiplicador;
	}
}