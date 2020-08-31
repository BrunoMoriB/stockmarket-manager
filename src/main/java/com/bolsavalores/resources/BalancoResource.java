package com.bolsavalores.resources;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.bolsavalores.helpers.JsonConverter;
import com.bolsavalores.models.Acao;
import com.bolsavalores.models.Balanco;
import com.bolsavalores.models.Cotacao;
import com.bolsavalores.models.DesempenhoFinanceiro;
import com.bolsavalores.models.Empresa;
import com.bolsavalores.models.MultiplosFundamentalistas;
import com.bolsavalores.models.Provento;
import com.bolsavalores.models.exceptions.StockmarketException;
import com.bolsavalores.repositories.AcaoRepository;
import com.bolsavalores.repositories.BalancoRepository;
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
	AcaoRepository acaoRepository;
	
	@Autowired
	BalancoService balancoService;
	
	@Autowired
	JsonConverter jsonConverter;

	@Autowired
	CalculadoraFundamentalista calculadoraFundamentalista;
	
	@GetMapping("/{id}")
	public ResponseEntity<String> getBalanco(@PathVariable long id) {
		try{
			Balanco balanco = balancoRepository.findById(id);
			
	//		if(balanco == null)
	//			TODO tratar
			
			BalancoResponse balancoResponse = new BalancoResponse(balanco.getId(), 
																  getEmpresaResponse(balanco.getEmpresa(), balanco.getData()), 
																  getMultiplosFundamentalistaResponse(balanco.getMultiplosFundamentalistas(), balanco.getData()), 
																  getDesempenhoFinanceiroResponse(balanco.getDesempenhoFinanceiro()), 
																  balanco.getData(), 
																  balanco.getLucroLiquidoTrimestral(), 
																  balanco.getLucroLiquidoAnual(), 
																  balanco.getPatrimonioLiquido(), 
																  balanco.getDividaBruta(), 
																  balanco.getCaixaDisponivel(), 
																  balanco.isDailyUpdated(), 
																  balanco.getTrimestre());
			
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
																	  getEmpresaResponse(b.getEmpresa(), b.getData()), 
																	  getMultiplosFundamentalistaResponse(b.getMultiplosFundamentalistas(), b.getData()), 
																	  getDesempenhoFinanceiroResponse(b.getDesempenhoFinanceiro()), 
																	  b.getData(), 
																	  b.getLucroLiquidoTrimestral(), 
																	  b.getLucroLiquidoAnual(), 
																	  b.getPatrimonioLiquido(), 
																	  b.getDividaBruta(), 
																	  b.getCaixaDisponivel(), 
																	  b.isDailyUpdated(), 
																	  b.getTrimestre())));
			
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
																					getEmpresaResponse(b.getEmpresa(), b.getData()), 
																					getMultiplosFundamentalistaResponse(b.getMultiplosFundamentalistas(), b.getData()), 
																					getDesempenhoFinanceiroResponse(b.getDesempenhoFinanceiro()), 
																					b.getData(), 
																					b.getLucroLiquidoTrimestral(), 
																					b.getLucroLiquidoAnual(), 
																					b.getPatrimonioLiquido(), 
																					b.getDividaBruta(), 
																					b.getCaixaDisponivel(), 
																					b.isDailyUpdated(), 
																					b.getTrimestre())));			
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
																								getEmpresaResponse(b.getEmpresa(), b.getData()), 
																								getMultiplosFundamentalistaResponse(b.getMultiplosFundamentalistas(), b.getData()), 
																								getDesempenhoFinanceiroResponse(b.getDesempenhoFinanceiro()), 
																								b.getData(), 
																								b.getLucroLiquidoTrimestral(), 
																								b.getLucroLiquidoAnual(), 
																								b.getPatrimonioLiquido(), 
																								b.getDividaBruta(), 
																								b.getCaixaDisponivel(), 
																								b.isDailyUpdated(), 
																								b.getTrimestre())));
			return ResponseEntity.ok(jsonConverter.toJson(balancosResponse));	
		} catch (Exception e) {
			LOG.error("Não foi possível recalcular os Balancos. ", e);
			return new ResponseEntity<String>("Não foi possível recalcular os Balancos. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/buscaTodosBalancosDailyUpdatedRecalculados")
	public ResponseEntity<String> getTodosBalancosDailyUpdatedRecalculados(){
		LOG.info("Iniciando processo de atualização dos Balanços das empresas.");
		
		try {
			List<Acao> acoes  = acaoRepository.findAll();
			acoes.forEach(a -> {
					try {
						balancoService.getBalancosRecalculadosByAcaoId(a.getId());
					} catch (StockmarketException | ParseException e) {
						LOG.error("Não foi possível recalcular os Balancos para Ação(id " + a.getId() + "). " + e.getMessage());
					}
				});
			
			List<Balanco> balancosRecalculados = balancoRepository.findBalancosDailyUpdated();
			
			LOG.info("Processo de atualização dos Balanços das empresas foi finalizado!");
			
			return ResponseEntity.ok(jsonConverter.toJson(balancosRecalculados));
		} catch (Exception e) {
			LOG.error("Não foi possível recalcular os Balancos. ", e);
			return new ResponseEntity<String>("Não foi possível recalcular os Balancos. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/buscaBalancosDailyUpdatedRequisitosMinimos")
	public ResponseEntity<String> getBalancosDailyUpdatedRequisitosMinimos(){
		try {
			List<Balanco> balancosDailyUpdated = balancoRepository.findBalancosDailyUpdated();
			
			if(balancosDailyUpdated == null || balancosDailyUpdated.isEmpty())
				throw new StockmarketException("Nenhum balanço daily updated encontrado. ");
			/*
			balancosDailyUpdated.removeIf( b -> !calculadoraFundamentalista.isDadosBalancoValidos(b.getMultiplosFundamentalistas(), b.getDesempenhoFinanceiro()) ||
					!calculadoraFundamentalista.validaRequisitosMinimos(b.getMultiplosFundamentalistas(), b.getDesempenhoFinanceiro()));
					
			Collections.sort(balancosDailyUpdated, Balanco.Comparators.NOTA);
			*/
			return ResponseEntity.ok(jsonConverter.toJson(balancosDailyUpdated));
		} catch (Exception e) {
			LOG.error("Não foi pssível encontrar os Balanços daily updated. ", e);
			return new ResponseEntity<String>("Não foi possível encontrar os Balanços daily updated. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@PostMapping()
	public ResponseEntity<String> salvaBalanco(@RequestBody Balanco balanco) {
		try {
			balanco = balancoService.salvaBalanco(balanco);
			BalancoResponse balancoResponse = new BalancoResponse(balanco.getId(), 
																  getEmpresaResponse(balanco.getEmpresa(), balanco.getData()), 
																  getMultiplosFundamentalistaResponse(balanco.getMultiplosFundamentalistas(), balanco.getData()), 
																  getDesempenhoFinanceiroResponse(balanco.getDesempenhoFinanceiro()), 
																  balanco.getData(), 
																  balanco.getLucroLiquidoTrimestral(), 
																  balanco.getLucroLiquidoAnual(), 
																  balanco.getPatrimonioLiquido(), 
																  balanco.getDividaBruta(), 
																  balanco.getCaixaDisponivel(), 
																  balanco.isDailyUpdated(), 
																  balanco.getTrimestre());
			
			return ResponseEntity.ok(jsonConverter.toJson(balancoResponse));
		} catch (Exception e) {
			e.printStackTrace();
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
	
	private EmpresaResponse getEmpresaResponse(Empresa empresa, LocalDate data) {
		Set<SetorResponse> setoresResponse = new HashSet<SetorResponse>();
		Set<AcaoResponse> acoesResponse = new HashSet<AcaoResponse>();
		empresa.getSetores().forEach(s -> setoresResponse.add(new SetorResponse(s.getId(), s.getNome())));
		empresa.getAcoes().forEach(a -> acoesResponse.add(new AcaoResponse(a.getId(), a.getCodigo(), getCotacaoResponse(a.getCotacoes(), data), getProventosResponse(a.getProventos()))));
		
		return new EmpresaResponse(empresa.getId(), empresa.getRazaoSocial(), empresa.getNomePregao(), empresa.getCnpj(), empresa.getQuantidadePapeis(), acoesResponse, setoresResponse);
	}
	
	private Set<MultiplosFundamentalistasResponse> getMultiplosFundamentalistaResponse(List<MultiplosFundamentalistas> listMultiplosFundamentalistas, LocalDate data) {
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
																																			 getCotacaoResponse(mf.getAcao().getCotacoes(), data),
																																			 getProventosResponse(mf.getAcao().getProventos())))));
		
		return listMultFundResponse;
	}
	
	private DesempenhoFinanceiroResponse getDesempenhoFinanceiroResponse(DesempenhoFinanceiro desempenhoFinanceiroResponse) {
		return new DesempenhoFinanceiroResponse(desempenhoFinanceiroResponse.getId(),
												desempenhoFinanceiroResponse.getEvolucaoLucroLiquidoTrimestral(), 
												desempenhoFinanceiroResponse.getEvolucaoLucroLiquidoAnual(), 
												desempenhoFinanceiroResponse.getHasCrescimentoLucroLiquidoTresAnos());
	}
	
	private CotacaoResponse getCotacaoResponse(List<Cotacao> cotacoes, LocalDate data) {
		Cotacao cotacao = cotacoes.stream().filter(c -> c.getData() == data).findFirst().orElse(null);
		return cotacao != null ? new CotacaoResponse(cotacao.getId(), cotacao.getData(), cotacao.getValor()) : null;
	}
	
	private List<ProventoResponse> getProventosResponse(List<Provento> proventos){
		List<ProventoResponse> proventosResponse = new ArrayList<ProventoResponse>();
		proventos.forEach(p -> proventosResponse.add(new ProventoResponse(p.getId(), p.getTipo(), p.getValor(), p.getDataEx(), p.getDataPagamento())));
		return proventosResponse;
	}
	
	@Getter
    @AllArgsConstructor
	static class BalancoResponse{
		long id;
		EmpresaResponse empresa;
		Set<MultiplosFundamentalistasResponse> multiplosFundamentalistas;
		DesempenhoFinanceiroResponse desempenhoFinanceiro;
		LocalDate data;
		Long lucroLiquidoTrimestral;
		Long lucroLiquidoAnual;
		Long patrimonioLiquido;
		Long dividaBruta;
		Long caixaDisponivel;
		boolean dailyUpdated;
		String trimestre;
	}
	
	@Getter
    @AllArgsConstructor
	static class EmpresaResponse{
		long id;
		String razaoSocial;
		String nomePregao;
		String cnpj;
		long quantidade;
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
		CotacaoResponse cotacao;
		List<ProventoResponse> proventos;
    }
	
	@Getter
	@AllArgsConstructor
	static class CotacaoResponse{
		long id;
		LocalDate data;
		double valor;
	}
	
	@Getter
	@AllArgsConstructor
	static class ProventoResponse{
		long id;
		String tipo;
		double valor;
		LocalDate dataEx;
		LocalDate dataPagamento;
	}
}