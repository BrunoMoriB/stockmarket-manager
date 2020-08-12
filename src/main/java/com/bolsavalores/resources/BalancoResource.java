package com.bolsavalores.resources;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import com.bolsavalores.helpers.JsonConverter;
import com.bolsavalores.models.Balanco;
import com.bolsavalores.models.DesempenhoFinanceiro;
import com.bolsavalores.models.Empresa;
import com.bolsavalores.models.MultiplosFundamentalistas;
import com.bolsavalores.repositories.BalancoRepository;
import com.bolsavalores.services.BalancoService;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@CrossOrigin
@RestController
@RequestMapping(value="/balancos")
public class BalancoResource {

	@Autowired
	BalancoRepository balancoRepository;
	
	@Autowired
	BalancoService balancoService;
	
	@Autowired
	JsonConverter jsonConverter;
	
	@GetMapping("/{id}")
	public ResponseEntity<String> getBalanco(@PathVariable long id) {
		try{
			Balanco balanco = balancoRepository.findById(id);
			
	//		if(balanco == null)
	//			TODO tratar
			
			BalancoResponse balancoResponse = new BalancoResponse(balanco.getId(), 
																  getEmpresaResponse(balanco.getEmpresa()), 
																  getMultiplosFundamentalistaResponse(balanco.getMultiplosFundamentalistas()), 
																  getDesempenhoFinanceiroResponse(balanco.getDesempenhoFinanceiro()), 
																  balanco.getData(), 
																  balanco.getLucroLiquidoTrimestral(), 
																  balanco.getLucroLiquidoAnual(), 
																  balanco.getPatrimonioLiquido(), 
																  balanco.getDividaBruta(), 
																  balanco.getCaixaDisponivel(), 
																  balanco.getQtdPapeis(), 
																  balanco.isDailyUpdated(), 
																  balanco.getCotacao(), 
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
																	  getEmpresaResponse(b.getEmpresa()), 
																	  getMultiplosFundamentalistaResponse(b.getMultiplosFundamentalistas()), 
																	  getDesempenhoFinanceiroResponse(b.getDesempenhoFinanceiro()), 
																	  b.getData(), 
																	  b.getLucroLiquidoTrimestral(), 
																	  b.getLucroLiquidoAnual(), 
																	  b.getPatrimonioLiquido(), 
																	  b.getDividaBruta(), 
																	  b.getCaixaDisponivel(), 
																	  b.getQtdPapeis(), 
																	  b.isDailyUpdated(), 
																	  b.getCotacao(), 
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
			
			List<BalancoResponse> balancosResponse = new ArrayList<BalancoResponse>();
			balancos.stream().forEach(b -> balancosResponse.add(new BalancoResponse(b.getId(), 
																					getEmpresaResponse(b.getEmpresa()), 
																					getMultiplosFundamentalistaResponse(b.getMultiplosFundamentalistas()), 
																					getDesempenhoFinanceiroResponse(b.getDesempenhoFinanceiro()), 
																					b.getData(), 
																					b.getLucroLiquidoTrimestral(), 
																					b.getLucroLiquidoAnual(), 
																					b.getPatrimonioLiquido(), 
																					b.getDividaBruta(), 
																					b.getCaixaDisponivel(), 
																					b.getQtdPapeis(), 
																					b.isDailyUpdated(), 
																					b.getCotacao(), 
																					b.getTrimestre())));
			Collections.sort(balancos);
			return ResponseEntity.ok(jsonConverter.toJson(balancosResponse));
		} catch (Exception e) {
			e.printStackTrace();
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
																								getEmpresaResponse(b.getEmpresa()), 
																								getMultiplosFundamentalistaResponse(b.getMultiplosFundamentalistas()), 
																								getDesempenhoFinanceiroResponse(b.getDesempenhoFinanceiro()), 
																								b.getData(), 
																								b.getLucroLiquidoTrimestral(), 
																								b.getLucroLiquidoAnual(), 
																								b.getPatrimonioLiquido(), 
																								b.getDividaBruta(), 
																								b.getCaixaDisponivel(), 
																								b.getQtdPapeis(), 
																								b.isDailyUpdated(), 
																								b.getCotacao(), 
																								b.getTrimestre())));
			
			return ResponseEntity.ok(jsonConverter.toJson(balancosResponse));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Não foi possível recalcular os Balancos. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping()
	public ResponseEntity<String> salvaBalanco(@RequestBody Balanco balanco) {
		try {
			balanco = balancoService.salvaBalanco(balanco);
			BalancoResponse balancoResponse = new BalancoResponse(balanco.getId(), 
																  getEmpresaResponse(balanco.getEmpresa()), 
																  getMultiplosFundamentalistaResponse(balanco.getMultiplosFundamentalistas()), 
																  getDesempenhoFinanceiroResponse(balanco.getDesempenhoFinanceiro()), 
																  balanco.getData(), 
																  balanco.getLucroLiquidoTrimestral(), 
																  balanco.getLucroLiquidoAnual(), 
																  balanco.getPatrimonioLiquido(), 
																  balanco.getDividaBruta(), 
																  balanco.getCaixaDisponivel(), 
																  balanco.getQtdPapeis(), 
																  balanco.isDailyUpdated(), 
																  balanco.getCotacao(), 
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
	
	private EmpresaResponse getEmpresaResponse(Empresa empresa) {
		Set<SetorResponse> setoresResponse = new HashSet<SetorResponse>();
		Set<AcaoResponse> acoesResponse = new HashSet<AcaoResponse>();
		empresa.getSetores().stream().forEach(s -> setoresResponse.add(new SetorResponse(s.getId(), s.getNome())));
		empresa.getAcoes().stream().forEach(a -> acoesResponse.add(new AcaoResponse(a.getId(), a.getCodigo())));
		
		return new EmpresaResponse(empresa.getId(), empresa.getRazaoSocial(), empresa.getNomePregao(), empresa.getCnpj(), empresa.getQuantidade(), acoesResponse, setoresResponse);
	}
	
	private Set<MultiplosFundamentalistasResponse> getMultiplosFundamentalistaResponse(List<MultiplosFundamentalistas> listMultiplosFundamentalistas) {
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
																															mf.getAcao().getId())));
		
		return listMultFundResponse;
	}
	
	private DesempenhoFinanceiroResponse getDesempenhoFinanceiroResponse(DesempenhoFinanceiro desempenhoFinanceiroResponse) {
		return new DesempenhoFinanceiroResponse(desempenhoFinanceiroResponse.getId(),
												desempenhoFinanceiroResponse.getEvolucaoLucroLiquidoTrimestral(), 
												desempenhoFinanceiroResponse.getEvolucaoLucroLiquidoAnual(), 
												desempenhoFinanceiroResponse.getHasCrescimentoLucroLiquidoTresAnos());
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
		Long qtdPapeis;
		boolean dailyUpdated;
		Double cotacao;
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
		long idAcao;
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
    }
}
